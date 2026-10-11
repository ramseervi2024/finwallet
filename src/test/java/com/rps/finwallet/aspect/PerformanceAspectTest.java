package com.rps.finwallet.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerformanceAspectTest {

    @InjectMocks
    private PerformanceAspect performanceAspect;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private Signature signature;

    @Mock
    private LogExecutionTime logExecutionTime;

    @Test
    @DisplayName("PerformanceAspect should proceed with execution and return result")
    void measureExecutionTime_Success() throws Throwable {
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn("PayrollService.disburseSalary(..)");
        when(logExecutionTime.value()).thenReturn("Individual Salary Payout");
        when(proceedingJoinPoint.proceed()).thenReturn("SUCCESS_RESULT");

        Object result = performanceAspect.measureExecutionTime(proceedingJoinPoint, logExecutionTime);

        assertEquals("SUCCESS_RESULT", result);
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    @DisplayName("PerformanceAspect should re-throw exception if target method fails")
    void measureExecutionTime_ThrowsException() throws Throwable {
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn("WalletService.transferFunds(..)");
        when(logExecutionTime.value()).thenReturn("P2P Wallet Transfer");
        when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("Database Timeout"));

        assertThrows(RuntimeException.class, () -> performanceAspect.measureExecutionTime(proceedingJoinPoint, logExecutionTime));
        verify(proceedingJoinPoint, times(1)).proceed();
    }
}
