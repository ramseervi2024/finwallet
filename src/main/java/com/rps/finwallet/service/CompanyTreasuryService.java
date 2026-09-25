package com.rps.finwallet.service;

import com.rps.finwallet.dto.TreasuryDepositRequest;
import com.rps.finwallet.model.*;
import com.rps.finwallet.repository.CompanyTreasuryRepository;
import com.rps.finwallet.repository.TransactionLedgerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CompanyTreasuryService {

    private final CompanyTreasuryRepository treasuryRepository;
    private final TransactionLedgerRepository ledgerRepository;

    public CompanyTreasuryService(CompanyTreasuryRepository treasuryRepository,
                                  TransactionLedgerRepository ledgerRepository) {
        this.treasuryRepository = treasuryRepository;
        this.ledgerRepository = ledgerRepository;
    }

    /**
     * Initializes or fetches the primary corporate payroll treasury account.
     */
    @Transactional
    public CompanyTreasury getOrCreatePrimaryTreasury() {
        return treasuryRepository.findFirstByOrderByIdAsc().orElseGet(() -> {
            CompanyTreasury treasury = new CompanyTreasury(
                    "CORP-TREASURY-01",
                    "FinWallet Corporate Treasury",
                    BigDecimal.valueOf(1000000.00), // Default 1,000,000 INR initial corporate funding
                    "INR"
            );
            return treasuryRepository.save(treasury);
        });
    }

    /**
     * Capitalizes/deposits funds into the corporate treasury.
     * Uses Pessimistic Locking to ensure serial consistency on the treasury row.
     */
    @Transactional
    public CompanyTreasury depositToTreasury(TreasuryDepositRequest request) {
        CompanyTreasury treasury;
        if (request.getAccountNumber() != null && !request.getAccountNumber().isBlank()) {
            treasury = treasuryRepository.findAndLockByAccountNumber(request.getAccountNumber())
                    .orElseThrow(() -> new RuntimeException("Treasury account not found: " + request.getAccountNumber()));
        } else {
            treasury = treasuryRepository.findPrimaryTreasuryForUpdate()
                    .orElseGet(this::getOrCreatePrimaryTreasury);
        }

        BigDecimal newBalance = treasury.getBalance().add(request.getAmount());
        treasury.setBalance(newBalance);
        CompanyTreasury updatedTreasury = treasuryRepository.save(treasury);

        // Record in transaction ledger
        String ref = "TXN-" + UUID.randomUUID().toString().substring(0, 13).toUpperCase();
        TransactionLedger ledger = new TransactionLedger(
                ref,
                "EXTERNAL_BANK",
                treasury.getAccountNumber(),
                request.getAmount(),
                TransactionType.DEPOSIT,
                TransactionStatus.SUCCESS,
                request.getDescription() != null ? request.getDescription() : "Corporate Treasury Fund Deposit"
        );
        ledgerRepository.save(ledger);

        return updatedTreasury;
    }

    @Transactional(readOnly = true)
    public CompanyTreasury getTreasuryDetails() {
        return getOrCreatePrimaryTreasury();
    }
}
