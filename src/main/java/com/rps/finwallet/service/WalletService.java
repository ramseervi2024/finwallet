package com.rps.finwallet.service;

import com.rps.finwallet.dto.TransferRequest;
import com.rps.finwallet.dto.WithdrawRequest;
import com.rps.finwallet.exception.InsufficientBalanceException;
import com.rps.finwallet.exception.ResourceNotFoundException;
import com.rps.finwallet.model.*;
import com.rps.finwallet.repository.EmployeeRepository;
import com.rps.finwallet.repository.TransactionLedgerRepository;
import com.rps.finwallet.repository.WalletRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final EmployeeRepository employeeRepository;
    private final TransactionLedgerRepository ledgerRepository;

    public WalletService(WalletRepository walletRepository,
                         EmployeeRepository employeeRepository,
                         TransactionLedgerRepository ledgerRepository) {
        this.walletRepository = walletRepository;
        this.employeeRepository = employeeRepository;
        this.ledgerRepository = ledgerRepository;
    }

    /**
     * Initializes a corporate salary wallet for an employee if it doesn't already exist.
     */
    @Transactional
    public Wallet getOrCreateWalletForEmployee(Employee employee) {
        return walletRepository.findByEmployeeId(employee.getId()).orElseGet(() -> {
            String walletNum = "WAL-EMP-" + String.format("%04d", employee.getId());
            Wallet wallet = new Wallet(walletNum, employee, BigDecimal.ZERO, "INR");
            return walletRepository.save(wallet);
        });
    }

    @Transactional
    public Wallet getWalletByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        return getOrCreateWalletForEmployee(employee);
    }

    /**
     * Peer-to-Peer Wallet Transfer (Employee to Employee).
     * Employs Pessimistic Locking on both wallets to prevent race conditions & double-spending.
     */
    @Transactional(rollbackFor = Exception.class)
    public TransactionLedger transferFunds(Long senderEmployeeId, TransferRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be strictly greater than zero");
        }

        // Lock sender wallet (or create if legacy employee had no wallet yet)
        Wallet senderWallet = walletRepository.findAndLockByEmployeeId(senderEmployeeId)
                .orElseGet(() -> {
                    Employee emp = employeeRepository.findById(senderEmployeeId)
                            .orElseThrow(() -> new ResourceNotFoundException("Sender employee not found with ID: " + senderEmployeeId));
                    return getOrCreateWalletForEmployee(emp);
                });

        if (senderWallet.getStatus() != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Sender wallet is " + senderWallet.getStatus() + ". Transfers are blocked.");
        }

        // Lock receiver wallet
        Wallet targetWallet = walletRepository.findAndLockByWalletNumber(request.getTargetWalletNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Target wallet not found: " + request.getTargetWalletNumber()));

        if (targetWallet.getStatus() != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Target wallet is not ACTIVE");
        }

        if (senderWallet.getWalletNumber().equals(targetWallet.getWalletNumber())) {
            throw new IllegalArgumentException("Cannot transfer funds to the same wallet");
        }

        // Validate Balance
        if (senderWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient wallet balance. Available: ₹" + senderWallet.getBalance() + ", Requested: ₹" + request.getAmount()
            );
        }

        // Execute Debit and Credit
        senderWallet.setBalance(senderWallet.getBalance().subtract(request.getAmount()));
        targetWallet.setBalance(targetWallet.getBalance().add(request.getAmount()));

        walletRepository.save(senderWallet);
        walletRepository.save(targetWallet);

        // Record Double-Entry Audit Ledger
        String ref = "TXN-" + UUID.randomUUID().toString().substring(0, 13).toUpperCase();
        TransactionLedger ledger = new TransactionLedger(
                ref,
                senderWallet.getWalletNumber(),
                targetWallet.getWalletNumber(),
                request.getAmount(),
                TransactionType.WALLET_TRANSFER,
                TransactionStatus.SUCCESS,
                request.getDescription() != null ? request.getDescription() : "Peer wallet transfer"
        );

        return ledgerRepository.save(ledger);
    }

    /**
     * Self Withdrawal to Bank Account.
     * Uses Pessimistic Lock to guarantee atomic balance deduction.
     */
    @Transactional(rollbackFor = Exception.class)
    public TransactionLedger withdrawFunds(Long employeeId, WithdrawRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be strictly greater than zero");
        }

        Wallet wallet = walletRepository.findAndLockByEmployeeId(employeeId)
                .orElseGet(() -> {
                    Employee emp = employeeRepository.findById(employeeId)
                            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
                    return getOrCreateWalletForEmployee(emp);
                });

        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Wallet is " + wallet.getStatus() + ". Withdrawals are blocked.");
        }

        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance for withdrawal. Available: ₹" + wallet.getBalance() + ", Requested: ₹" + request.getAmount()
            );
        }

        // Deduct from wallet
        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        walletRepository.save(wallet);

        // Record Ledger
        String ref = "WTH-" + UUID.randomUUID().toString().substring(0, 13).toUpperCase();
        TransactionLedger ledger = new TransactionLedger(
                ref,
                wallet.getWalletNumber(),
                "BANK-" + request.getBankAccountNumber(),
                request.getAmount(),
                TransactionType.WITHDRAWAL,
                TransactionStatus.SUCCESS,
                request.getNotes() != null ? request.getNotes() : "Self withdrawal to external bank account"
        );

        return ledgerRepository.save(ledger);
    }

    /**
     * Fetch passbook/statement of all transactions for this employee's wallet.
     */
    @Transactional(readOnly = true)
    public Page<TransactionLedger> getWalletPassbook(Long employeeId, Pageable pageable) {
        Wallet wallet = getWalletByEmployeeId(employeeId);
        return ledgerRepository.findByFromAccountOrToAccountOrderByCreatedAtDesc(
                wallet.getWalletNumber(), wallet.getWalletNumber(), pageable
        );
    }
}
