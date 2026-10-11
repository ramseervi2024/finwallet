package com.rps.finwallet.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to track and flag high-value financial movements (Anti-Money Laundering / Fraud Monitoring).
 * Any transaction exceeding the configured threshold will trigger a security audit log.
 *
 * Example:
 *   @TrackHighValueTransaction(threshold = 50000.0, action = "WALLET_TRANSFER")
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackHighValueTransaction {
    double threshold() default 50000.0; // Default threshold: ₹50,000 INR
    String action() default "FINANCIAL_TRANSACTION";
}
