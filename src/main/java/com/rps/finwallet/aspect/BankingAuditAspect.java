package com.rps.finwallet.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * ============================================================================
 * AOP CONCEPT: Declarative Banking Audit with @Before, @AfterReturning, @AfterThrowing
 * ============================================================================
 *
 * WHAT PROBLEM DOES THIS SOLVE?
 * Without AOP: You would have to write 5-10 lines of logging/auditing in EVERY service method.
 * With AOP: Your business logic stays 100% clean, and this single Aspect intercepts every
 * call automatically without touching a single line of service code.
 *
 * AOP TERMINOLOGY:
 * 1. Aspect: This class (@Aspect)
 * 2. Pointcut: The expression defining which methods to target (e.g., all methods in com.rps.finwallet.service)
 * 3. JoinPoint: The specific method invocation happening right now.
 * 4. Advice:
 *    - @Before: Runs before target method
 *    - @AfterReturning: Runs only when target method succeeds
 *    - @AfterThrowing: Runs only when target method throws an exception
 */
@Aspect
@Component
public class BankingAuditAspect {

    private static final Logger log = LoggerFactory.getLogger(BankingAuditAspect.class);

    /**
     * POINTCUT DEFINITION:
     * Targets all public methods in any class under the `com.rps.finwallet.service` package.
     *
     * Pointcut syntax breakdown:
     * - `execution`: method execution join point
     * - `*`: any return type (void, Employee, Wallet, etc.)
     * - `com.rps.finwallet.service..*`: any class in package `com.rps.finwallet.service` or subpackages
     * - `.*`: any method name
     * - `(..)`: any number and type of arguments
     */
    @Pointcut("execution(* com.rps.finwallet.service..*(..))")
    public void allServiceMethods() {}

    /**
     * POINTCUT for all controller endpoints (REST entry points)
     */
    @Pointcut("execution(* com.rps.finwallet.controller..*(..))")
    public void allControllerEndpoints() {}

    /**
     * @Before ADVICE:
     * Executes immediately BEFORE the target method begins execution.
     * Captures incoming method arguments for security and audit trail.
     */
    @Before("allServiceMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("📥 [AUDIT >> START] {}.{}() | Args: {}", className, methodName, Arrays.toString(args));
    }

    /**
     * @AfterReturning ADVICE:
     * Executes ONLY if the target method completes normally without throwing any exception.
     * `returning = "result"` binds the returned object to the parameter `Object result`.
     */
    @AfterReturning(pointcut = "allServiceMethods()", returning = "result")
    public void logMethodSuccess(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        String resultSummary = result != null ? result.getClass().getSimpleName() : "void/null";
        log.info("✅ [AUDIT >> SUCCESS] {}.{}() | Returned: {}", className, methodName, resultSummary);
    }

    /**
     * @AfterThrowing ADVICE:
     * Executes ONLY if the target method throws an exception.
     * `throwing = "exception"` captures the thrown Throwable / Exception.
     */
    @AfterThrowing(pointcut = "allServiceMethods()", throwing = "exception")
    public void logMethodException(JoinPoint joinPoint, Throwable exception) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.error("❌ [AUDIT >> FAILURE] {}.{}() | Exception: {} | Reason: {}",
                className, methodName, exception.getClass().getSimpleName(), exception.getMessage());
    }
}
