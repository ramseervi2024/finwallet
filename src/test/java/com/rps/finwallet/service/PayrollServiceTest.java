package com.rps.finwallet.service;

import com.rps.finwallet.dto.SalaryDisburseRequest;
import com.rps.finwallet.exception.DuplicateDisbursementException;
import com.rps.finwallet.exception.InsufficientBalanceException;
import com.rps.finwallet.model.*;
import com.rps.finwallet.repository.*;
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
class PayrollServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private CompanyTreasuryRepository treasuryRepository;

    @Mock
    private SalaryDisbursementRepository disbursementRepository;

    @Mock
    private TransactionLedgerRepository ledgerRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private CompanyTreasuryService treasuryService;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private PayrollService payrollService;

    private Employee employee;
    private CompanyTreasury treasury;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "Ramesh Seervi", "ramesh@finwallet.com", null);
        employee.setBaseSalary(new BigDecimal("75000.00"));
        employee.setStatus(EmployeeStatus.ACTIVE);

        treasury = new CompanyTreasury("CORP-TREASURY-01", "FinWallet Corp", new BigDecimal("500000.00"), "INR");
        wallet = new Wallet("WAL-EMP-0001", employee, new BigDecimal("10000.00"), "INR");
    }

    @Test
    @DisplayName("Should successfully disburse monthly salary to active employee")
    void disburseSalary_Success() {
        SalaryDisburseRequest request = new SalaryDisburseRequest(1L, "2026-03", "March Salary");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(disbursementRepository.existsByEmployeeIdAndSalaryMonth(1L, "2026-03")).thenReturn(false);
        when(treasuryRepository.findPrimaryTreasuryForUpdate()).thenReturn(Optional.of(treasury));
        when(walletRepository.findAndLockByEmployeeId(1L)).thenReturn(Optional.of(wallet));
        when(disbursementRepository.save(any(SalaryDisbursement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SalaryDisbursement result = payrollService.disburseSalary(request);

        assertNotNull(result);
        assertEquals(new BigDecimal("75000.00"), result.getAmount());
        assertEquals("2026-03", result.getSalaryMonth());
        assertEquals(DisbursementStatus.SUCCESS, result.getStatus());

        // Treasury should be debited by 75,000 (500,000 - 75,000 = 425,000)
        assertEquals(new BigDecimal("425000.00"), treasury.getBalance());
        // Wallet should be credited by 75,000 (10,000 + 75,000 = 85,000)
        assertEquals(new BigDecimal("85000.00"), wallet.getBalance());

        verify(disbursementRepository, times(1)).save(any(SalaryDisbursement.class));
        verify(ledgerRepository, times(1)).save(any(TransactionLedger.class));
    }

    @Test
    @DisplayName("Should reject disbursement when salary has already been paid for the month (Idempotency)")
    void disburseSalary_DuplicateMonth_ThrowsException() {
        SalaryDisburseRequest request = new SalaryDisburseRequest(1L, "2026-03", "March Salary");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(disbursementRepository.existsByEmployeeIdAndSalaryMonth(1L, "2026-03")).thenReturn(true);

        assertThrows(DuplicateDisbursementException.class, () -> payrollService.disburseSalary(request));

        verify(treasuryRepository, never()).findPrimaryTreasuryForUpdate();
        verify(walletRepository, never()).findAndLockByEmployeeId(anyLong());
    }

    @Test
    @DisplayName("Should reject disbursement when company treasury has insufficient funds")
    void disburseSalary_InsufficientTreasury_ThrowsException() {
        treasury.setBalance(new BigDecimal("10000.00")); // Less than employee salary 75,000

        SalaryDisburseRequest request = new SalaryDisburseRequest(1L, "2026-03", "March Salary");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(disbursementRepository.existsByEmployeeIdAndSalaryMonth(1L, "2026-03")).thenReturn(false);
        when(treasuryRepository.findPrimaryTreasuryForUpdate()).thenReturn(Optional.of(treasury));

        assertThrows(InsufficientBalanceException.class, () -> payrollService.disburseSalary(request));

        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    @DisplayName("Should reject disbursement for inactive employee")
    void disburseSalary_InactiveEmployee_ThrowsException() {
        employee.setStatus(EmployeeStatus.INACTIVE);
        SalaryDisburseRequest request = new SalaryDisburseRequest(1L, "2026-03", "March Salary");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThrows(IllegalStateException.class, () -> payrollService.disburseSalary(request));
    }
}
