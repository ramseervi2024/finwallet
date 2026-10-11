package com.rps.finwallet.aspect;

import com.rps.finwallet.model.SalaryDisbursement;
import com.rps.finwallet.model.TransactionLedger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * ============================================================================
 * AOP CONCEPT: Financial Fraud & Anti-Money Laundering (AML) Tracking Aspect
 * ============================================================================
 *
 * REAL-WORLD SCENARIO:
 * Banking regulations (RBI, FINCEN, FCA) require financial institutions to flag
 * large fund movements (e.g. transfers, payroll, or withdrawals over ₹50,000).
 *
 * Rather than writing if(amount > 50000) inside every controller or service,
 * we use a declarative AOP Aspect @TrackHighValueTransaction to inspect transactions
 * automatically as they commit.
 */
@Aspect
@Component
public class HighValueTransactionAspect {

    private static final Logger log = LoggerFactory.getLogger(HighValueTransactionAspect.class);

    @AfterReturning(pointcut = "@annotation(trackAnnotation)", returning = "result")
    public void inspectHighValueMovement(JoinPoint joinPoint, TrackHighValueTransaction trackAnnotation, Object result) {
        BigDecimal threshold = BigDecimal.valueOf(trackAnnotation.threshold());
        BigDecimal amount = null;
        String refId = "UNKNOWN";
        String fromAccount = "-";
        String toAccount = "-";

        if (result instanceof TransactionLedger ledger) {
            amount = ledger.getAmount();
            refId = ledger.getTransactionRef();
            fromAccount = ledger.getFromAccount();
            toAccount = ledger.getToAccount();
        } else if (result instanceof SalaryDisbursement disbursement) {
            amount = disbursement.getAmount();
            refId = disbursement.getReferenceNumber();
            fromAccount = "CORP-TREASURY";
            toAccount = disbursement.getEmployee() != null ? disbursement.getEmployee().getName() : "EMPLOYEE";
        }

        if (amount != null && amount.compareTo(threshold) >= 0) {
            log.warn("""
                \n============================================================================
                🚨 [FINANCIAL SECURITY & AML ALERT] HIGH VALUE TRANSACTION DETECTED
                ----------------------------------------------------------------------------
                Action Type  : {}
                Reference    : {}
                Amount       : ₹{}
                Threshold    : ₹{}
                From Account : {}
                To Account   : {}
                Method       : {}
                Timestamp    : {}
                ============================================================================""",
                    trackAnnotation.action(),
                    refId,
                    amount,
                    threshold,
                    fromAccount,
                    toAccount,
                    joinPoint.getSignature().toShortString(),
                    java.time.LocalDateTime.now()
            );
        }
    }
}
