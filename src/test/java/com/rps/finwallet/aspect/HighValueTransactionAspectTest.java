package com.rps.finwallet.aspect;

import com.rps.finwallet.model.TransactionLedger;
import com.rps.finwallet.model.TransactionStatus;
import com.rps.finwallet.model.TransactionType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HighValueTransactionAspectTest {

    @InjectMocks
    private HighValueTransactionAspect highValueAspect;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private Signature signature;

    @Mock
    private TrackHighValueTransaction trackAnnotation;

    @Test
    @DisplayName("Should detect and log high-value transaction exceeding threshold")
    void inspectHighValueMovement_ExceedsThreshold() {
        TransactionLedger ledger = new TransactionLedger(
                "TXN-HIGH-001",
                "WAL-EMP-0001",
                "WAL-EMP-0002",
                new BigDecimal("75000.00"),
                TransactionType.WALLET_TRANSFER,
                TransactionStatus.SUCCESS,
                "Big Bonus Transfer"
        );

        when(trackAnnotation.threshold()).thenReturn(50000.0);
        when(trackAnnotation.action()).thenReturn("P2P_WALLET_TRANSFER");
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn("WalletService.transferFunds(..)");

        assertDoesNotThrow(() -> highValueAspect.inspectHighValueMovement(joinPoint, trackAnnotation, ledger));
    }

    @Test
    @DisplayName("Should silently pass when transaction is below threshold")
    void inspectHighValueMovement_BelowThreshold() {
        TransactionLedger ledger = new TransactionLedger(
                "TXN-LOW-001",
                "WAL-EMP-0001",
                "WAL-EMP-0002",
                new BigDecimal("500.00"),
                TransactionType.WALLET_TRANSFER,
                TransactionStatus.SUCCESS,
                "Coffee split"
        );

        when(trackAnnotation.threshold()).thenReturn(50000.0);

        assertDoesNotThrow(() -> highValueAspect.inspectHighValueMovement(joinPoint, trackAnnotation, ledger));
    }
}
