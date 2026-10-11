package com.rps.finwallet.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ============================================================================
 * AOP CONCEPT: Performance Profiling with @Around Advice
 * ============================================================================
 *
 * WHAT IS @Around ADVICE?
 * - @Around is the most powerful AOP advice.
 * - It wraps around the target method execution like a sandwich:
 *      1. Runs code BEFORE the method (starts timer).
 *      2. Calls proceedingJoinPoint.proceed() to let the REAL method run.
 *      3. Runs code AFTER the method (calculates elapsed time).
 * - It can also catch/modify exceptions, change arguments, or alter return values.
 *
 * WHAT IS ProceedingJoinPoint?
 * - An object representing the intercepted method in flight.
 * - Provides method signature, incoming arguments, and the .proceed() executor.
 */
@Aspect
@Component
public class PerformanceAspect {

    private static final Logger log = LoggerFactory.getLogger(PerformanceAspect.class);

    /**
     * Pointcut + Advice:
     * Intercepts any method annotated with @LogExecutionTime.
     */
    @Around("@annotation(logExecutionTime)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint, LogExecutionTime logExecutionTime) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String customLabel = logExecutionTime.value().isBlank() ? methodName : logExecutionTime.value();

        long startTime = System.currentTimeMillis();

        try {
            // Proceed with the actual target method execution
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - startTime;

            if (duration > 500) {
                log.warn("⚠️ [PERFORMANCE ALERT] Slow method detected: {} executed in {} ms (threshold: 500 ms)", customLabel, duration);
            } else {
                log.info("⏱️ [PERFORMANCE] {} executed in {} ms", customLabel, duration);
            }

            return result;
        } catch (Throwable throwable) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("⏱️ [PERFORMANCE] {} failed after {} ms with error: {}", customLabel, duration, throwable.getMessage());
            throw throwable; // Re-throw so business exception handling works normally
        }
    }
}
