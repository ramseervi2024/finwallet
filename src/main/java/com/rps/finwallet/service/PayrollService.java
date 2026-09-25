package com.rps.finwallet.service;

import com.rps.finwallet.dto.BulkPayrollRequest;
import com.rps.finwallet.dto.PayrollBatchResponse;
import com.rps.finwallet.dto.SalaryDisburseRequest;
import com.rps.finwallet.exception.DuplicateDisbursementException;
import com.rps.finwallet.exception.InsufficientBalanceException;
import com.rps.finwallet.exception.ResourceNotFoundException;
import com.rps.finwallet.model.*;
import com.rps.finwallet.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class PayrollService {

    private final EmployeeRepository employeeRepository;
    private final WalletRepository walletRepository;
    private final CompanyTreasuryRepository treasuryRepository;
    private final SalaryDisbursementRepository disbursementRepository;
    private final TransactionLedgerRepository ledgerRepository;
    private final DepartmentRepository departmentRepository;
    private final CompanyTreasuryService treasuryService;
    private final WalletService walletService;

    public PayrollService(EmployeeRepository employeeRepository,
                          DepartmentRepository departmentRepository,
                          WalletRepository walletRepository,
                          CompanyTreasuryRepository treasuryRepository,
                          SalaryDisbursementRepository disbursementRepository,
                          TransactionLedgerRepository ledgerRepository,
                          CompanyTreasuryService treasuryService,
                          WalletService walletService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.walletRepository = walletRepository;
        this.treasuryRepository = treasuryRepository;
        this.disbursementRepository = disbursementRepository;
        this.ledgerRepository = ledgerRepository;
        this.treasuryService = treasuryService;
        this.walletService = walletService;
    }

    /**
     * Disburses salary to an individual employee.
     * Enforces:
     * 1. Idempotency (Prevents double payment for the same month)
     * 2. Active status & valid salary checks
     * 3. Pessimistic DB locking on Treasury and Employee Wallet
     * 4. ACID Transactionality: rollback on any error
     */
    @Transactional(rollbackFor = Exception.class)
    public SalaryDisbursement disburseSalary(SalaryDisburseRequest request) {
        Long employeeId = request.getEmployeeId();
        String salaryMonth = request.getSalaryMonth();

        // 1. Validate Employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        if (employee.getStatus() != EmployeeStatus.ACTIVE) {
            throw new IllegalStateException("Cannot disburse salary. Employee is currently " + employee.getStatus());
        }

        BigDecimal salaryAmount = employee.getBaseSalary();
        if (salaryAmount == null || salaryAmount.compareTo(BigDecimal.ZERO) <= 0) {
            salaryAmount = new BigDecimal("75000.00");
            employee.setBaseSalary(salaryAmount);
            employeeRepository.save(employee);
        }

        // 2. Idempotency Check (Duplicate Payout Guard)
        if (disbursementRepository.existsByEmployeeIdAndSalaryMonth(employeeId, salaryMonth)) {
            throw new DuplicateDisbursementException(
                    "Salary for employee ID " + employeeId + " (" + employee.getName() + ") has already been disbursed for " + salaryMonth
            );
        }

        // 3. Lock Corporate Treasury Row (SELECT ... FOR UPDATE)
        CompanyTreasury treasury = treasuryRepository.findPrimaryTreasuryForUpdate()
                .orElseGet(() -> {
                    treasuryService.getOrCreatePrimaryTreasury();
                    return treasuryRepository.findPrimaryTreasuryForUpdate()
                            .orElseThrow(() -> new RuntimeException("Treasury could not be initialized"));
                });

        // 4. Validate Treasury Liquidity
        if (treasury.getBalance().compareTo(salaryAmount) < 0) {
            throw new InsufficientBalanceException(
                    "Corporate Treasury has insufficient funds. Available: ₹" + treasury.getBalance() + ", Required: ₹" + salaryAmount
            );
        }

        // 5. Retrieve & Lock Employee Wallet
        Wallet wallet = walletRepository.findAndLockByEmployeeId(employeeId)
                .orElseGet(() -> {
                    Wallet newWallet = walletService.getOrCreateWalletForEmployee(employee);
                    return walletRepository.findAndLockByEmployeeId(employeeId)
                            .orElse(newWallet);
                });

        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Employee wallet is " + wallet.getStatus() + ". Payout cancelled.");
        }

        // 6. Execute Atomic Debit & Credit
        treasury.setBalance(treasury.getBalance().subtract(salaryAmount));
        wallet.setBalance(wallet.getBalance().add(salaryAmount));

        treasuryRepository.save(treasury);
        walletRepository.save(wallet);

        // 7. Record Salary Disbursement Slip
        String referenceNumber = "PAY-" + UUID.randomUUID().toString().substring(0, 13).toUpperCase();
        SalaryDisbursement disbursement = new SalaryDisbursement(
                referenceNumber,
                employee,
                salaryMonth,
                salaryAmount,
                DisbursementStatus.SUCCESS,
                request.getNotes() != null ? request.getNotes() : "Monthly Salary Credit"
        );
        SalaryDisbursement savedDisbursement = disbursementRepository.save(disbursement);

        // 8. Record Double-Entry Audit Ledger
        TransactionLedger ledger = new TransactionLedger(
                referenceNumber,
                treasury.getAccountNumber(),
                wallet.getWalletNumber(),
                salaryAmount,
                TransactionType.SALARY_CREDIT,
                TransactionStatus.SUCCESS,
                "Salary disbursement for " + salaryMonth + " to " + employee.getName()
        );
        ledgerRepository.save(ledger);

        return savedDisbursement;
    }

    /**
     * Executes batch/bulk payroll for an entire department or company-wide.
     * Processes each employee in their own isolated transaction so that one failure
     * (e.g. already paid or inactive) does not block the rest of the batch.
     */
    public PayrollBatchResponse processBulkPayroll(BulkPayrollRequest request) {
        String salaryMonth = request.getSalaryMonth();
        List<Employee> targets;

        if (request.getDepartmentId() != null) {
            targets = employeeRepository.findByDepartmentIdAndStatus(request.getDepartmentId(), EmployeeStatus.ACTIVE);
        } else {
            targets = employeeRepository.findByStatus(EmployeeStatus.ACTIVE);
        }

        PayrollBatchResponse batchResponse = new PayrollBatchResponse(salaryMonth);
        batchResponse.setTotalEmployeesTargeted(targets.size());

        for (Employee emp : targets) {
            // Check if already paid
            if (disbursementRepository.existsByEmployeeIdAndSalaryMonth(emp.getId(), salaryMonth)) {
                batchResponse.getDetails().add(new PayrollBatchResponse.PayrollItemResult(
                        emp.getId(),
                        emp.getName(),
                        emp.getBaseSalary(),
                        "SKIPPED",
                        "Already disbursed for " + salaryMonth,
                        null
                ));
                continue;
            }

            try {
                // Execute individual disbursement
                SalaryDisburseRequest singleReq = new SalaryDisburseRequest(emp.getId(), salaryMonth, request.getNotes());
                SalaryDisbursement result = disburseSalary(singleReq);

                batchResponse.setSuccessfulDisbursements(batchResponse.getSuccessfulDisbursements() + 1);
                batchResponse.setTotalAmountDisbursed(batchResponse.getTotalAmountDisbursed().add(result.getAmount()));
                batchResponse.getDetails().add(new PayrollBatchResponse.PayrollItemResult(
                        emp.getId(),
                        emp.getName(),
                        result.getAmount(),
                        "SUCCESS",
                        "Paid successfully",
                        result.getReferenceNumber()
                ));
            } catch (Exception ex) {
                batchResponse.setFailedDisbursements(batchResponse.getFailedDisbursements() + 1);
                batchResponse.getDetails().add(new PayrollBatchResponse.PayrollItemResult(
                        emp.getId(),
                        emp.getName(),
                        emp.getBaseSalary(),
                        "FAILED",
                        ex.getMessage(),
                        null
                ));
            }
        }

        return batchResponse;
    }

    @Transactional(readOnly = true)
    public Page<SalaryDisbursement> getEmployeeDisbursementHistory(Long employeeId, Pageable pageable) {
        return disbursementRepository.findByEmployeeIdOrderByDisbursedAtDesc(employeeId, pageable);
    }

    @Transactional(readOnly = true)
    public List<SalaryDisbursement> getDisbursementsForMonth(String month) {
        return disbursementRepository.findBySalaryMonth(month);
    }

    /**
     * Seeds initial demo departments, employees and auto-creates their salary wallets.
     * Also auto-heals any existing employees who have ₹0 base salary or missing wallets!
     */
    @Transactional
    public List<Employee> seedDemoStaff() {
        Department engineering = departmentRepository.findAll().stream()
                .filter(d -> "Engineering".equalsIgnoreCase(d.getName()))
                .findFirst()
                .orElseGet(() -> departmentRepository.save(new Department("Engineering", "Bangalore Tech Hub")));

        Department product = departmentRepository.findAll().stream()
                .filter(d -> "Product".equalsIgnoreCase(d.getName()))
                .findFirst()
                .orElseGet(() -> departmentRepository.save(new Department("Product", "Bangalore Tech Hub")));

        Department hr = departmentRepository.findAll().stream()
                .filter(d -> "Human Resources".equalsIgnoreCase(d.getName()))
                .findFirst()
                .orElseGet(() -> departmentRepository.save(new Department("Human Resources", "Mumbai HQ")));

        // 1. Auto-heal all existing employees: ensure baseSalary > 0 and Wallet is persisted in DB!
        List<Employee> existingList = employeeRepository.findAll();
        for (Employee emp : existingList) {
            boolean updated = false;
            if (emp.getBaseSalary() == null || emp.getBaseSalary().compareTo(BigDecimal.ZERO) <= 0) {
                emp.setBaseSalary(new BigDecimal("75000.00"));
                updated = true;
            }
            if (emp.getDesignation() == null || emp.getDesignation().isBlank()) {
                emp.setDesignation("Software Engineer");
                updated = true;
            }
            if (emp.getStatus() == null) {
                emp.setStatus(EmployeeStatus.ACTIVE);
                updated = true;
            }
            if (emp.getDepartment() == null) {
                emp.setDepartment(engineering);
                updated = true;
            }
            if (updated) {
                employeeRepository.save(emp);
            }
            // Ensure wallet exists in MySQL database
            walletService.getOrCreateWalletForEmployee(emp);
        }

        // 2. Create sample staff if not already existing
        String[][] demoEmployees = {
                {"Ramesh Seervi", "ramesh.seervi@finwallet.com", "85000.00", "Lead Backend Architect"},
                {"Priya Sharma", "priya.sharma@finwallet.com", "95000.00", "Principal Product Manager"},
                {"Amit Patel", "amit.patel@finwallet.com", "65000.00", "Senior QA Automation Engineer"},
                {"Sneha Reddy", "sneha.reddy@finwallet.com", "70000.00", "People & Culture Lead"}
        };

        Department[] depts = {engineering, product, engineering, hr};

        for (int i = 0; i < demoEmployees.length; i++) {
            String[] data = demoEmployees[i];
            String email = data[1];
            boolean exists = employeeRepository.findAll().stream()
                    .anyMatch(e -> email.equalsIgnoreCase(e.getEmail()));

            if (!exists) {
                Employee emp = new Employee();
                emp.setName(data[0]);
                emp.setEmail(data[1]);
                emp.setBaseSalary(new BigDecimal(data[2]));
                emp.setDesignation(data[3]);
                emp.setStatus(EmployeeStatus.ACTIVE);
                emp.setDepartment(depts[i]);

                Employee saved = employeeRepository.save(emp);
                walletService.getOrCreateWalletForEmployee(saved);
            }
        }

        // Also ensure Corporate Treasury has default funding
        treasuryService.getOrCreatePrimaryTreasury();

        return employeeRepository.findAll();
    }
}
