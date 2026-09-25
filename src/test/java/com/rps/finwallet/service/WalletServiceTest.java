package com.rps.finwallet.service;

import com.rps.finwallet.dto.TransferRequest;
import com.rps.finwallet.dto.WithdrawRequest;
import com.rps.finwallet.exception.InsufficientBalanceException;
import com.rps.finwallet.model.*;
import com.rps.finwallet.repository.EmployeeRepository;
import com.rps.finwallet.repository.TransactionLedgerRepository;
import com.rps.finwallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TransactionLedgerRepository ledgerRepository;

    @InjectMocks
    private WalletService walletService;

    private Employee senderEmp;
    private Employee receiverEmp;
    private Wallet senderWallet;
    private Wallet receiverWallet;

    @BeforeEach
    void setUp() {
        senderEmp = new Employee(1L, "Ramesh", "ramesh@finwallet.com", null);
        receiverEmp = new Employee(2L, "Suresh", "suresh@finwallet.com", null);

        senderWallet = new Wallet("WAL-EMP-0001", senderEmp, new BigDecimal("5000.00"), "INR");
        receiverWallet = new Wallet("WAL-EMP-0002", receiverEmp, new BigDecimal("1000.00"), "INR");
    }

    @Test
    @DisplayName("Should successfully transfer funds between two employee wallets")
    void transferFunds_Success() {
        TransferRequest request = new TransferRequest("WAL-EMP-0002", new BigDecimal("2000.00"), "Lunch bill split");

        when(walletRepository.findAndLockByEmployeeId(1L)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findAndLockByWalletNumber("WAL-EMP-0002")).thenReturn(Optional.of(receiverWallet));
        when(ledgerRepository.save(any(TransactionLedger.class))).thenAnswer(i -> i.getArgument(0));

        TransactionLedger ledger = walletService.transferFunds(1L, request);

        assertNotNull(ledger);
        assertEquals(new BigDecimal("2000.00"), ledger.getAmount());
        assertEquals(TransactionType.WALLET_TRANSFER, ledger.getTransactionType());
        assertEquals(new BigDecimal("3000.00"), senderWallet.getBalance());
        assertEquals(new BigDecimal("3000.00"), receiverWallet.getBalance());

        verify(walletRepository, times(1)).save(senderWallet);
        verify(walletRepository, times(1)).save(receiverWallet);
    }

    @Test
    @DisplayName("Should reject transfer when balance is insufficient")
    void transferFunds_InsufficientBalance_ThrowsException() {
        TransferRequest request = new TransferRequest("WAL-EMP-0002", new BigDecimal("9999.00"), "Overdraft attempt");

        when(walletRepository.findAndLockByEmployeeId(1L)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findAndLockByWalletNumber("WAL-EMP-0002")).thenReturn(Optional.of(receiverWallet));

        assertThrows(InsufficientBalanceException.class, () -> walletService.transferFunds(1L, request));

        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    @DisplayName("Should reject self-transfer to the same wallet")
    void transferFunds_SelfTransfer_ThrowsException() {
        TransferRequest request = new TransferRequest("WAL-EMP-0001", new BigDecimal("100.00"), "Self loop");

        when(walletRepository.findAndLockByEmployeeId(1L)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findAndLockByWalletNumber("WAL-EMP-0001")).thenReturn(Optional.of(senderWallet));

        assertThrows(IllegalArgumentException.class, () -> walletService.transferFunds(1L, request));
    }

    @Test
    @DisplayName("Should successfully withdraw funds to bank account")
    void withdrawFunds_Success() {
        WithdrawRequest request = new WithdrawRequest("HDFC0001234", new BigDecimal("1500.00"), "ATM withdrawal");

        when(walletRepository.findAndLockByEmployeeId(1L)).thenReturn(Optional.of(senderWallet));
        when(ledgerRepository.save(any(TransactionLedger.class))).thenAnswer(i -> i.getArgument(0));

        TransactionLedger ledger = walletService.withdrawFunds(1L, request);

        assertNotNull(ledger);
        assertEquals(new BigDecimal("1500.00"), ledger.getAmount());
        assertEquals(TransactionType.WITHDRAWAL, ledger.getTransactionType());
        assertEquals(new BigDecimal("3500.00"), senderWallet.getBalance());
    }
}
