package com.rps.finwallet.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to measure and log the execution time of critical methods.
 * Applied on methods like bulk payroll, wallet transfers, or heavy DB queries.
 *
 * CONCEPT: Custom Annotation Pointcut
 * Target: METHOD
 * Retention: RUNTIME (accessible via reflection/Spring AOP proxies at runtime)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
    String value() default "";
}
