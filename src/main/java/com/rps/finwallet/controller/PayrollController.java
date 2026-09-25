package com.rps.finwallet.controller;

import com.rps.finwallet.common.ApiResponse;
import com.rps.finwallet.dto.BulkPayrollRequest;
import com.rps.finwallet.dto.PayrollBatchResponse;
import com.rps.finwallet.dto.SalaryDisburseRequest;
import com.rps.finwallet.model.SalaryDisbursement;
import com.rps.finwallet.service.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@Tag(name = "Payroll & Salary Disbursement", description = "Endpoints for managing monthly employee salary disbursements")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping("/disburse")
    @Operation(summary = "Disburse Salary to Individual Employee (Idempotent & Concurrency Safe)")
    public ResponseEntity<ApiResponse<SalaryDisbursement>> disburseIndividualSalary(
            @Valid @RequestBody SalaryDisburseRequest request) {
        SalaryDisbursement disbursement = payrollService.disburseSalary(request);
        return new ResponseEntity<>(
                new ApiResponse<>("Salary disbursed successfully", 201, disbursement),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/bulk-disburse")
    @Operation(summary = "Run Bulk Payroll for Department or Entire Company")
    public ResponseEntity<ApiResponse<PayrollBatchResponse>> runBulkPayroll(
            @Valid @RequestBody BulkPayrollRequest request) {
        PayrollBatchResponse batchResponse = payrollService.processBulkPayroll(request);
        return ResponseEntity.ok(
                new ApiResponse<>("Bulk payroll processed", 200, batchResponse)
        );
    }

    @GetMapping("/history/{employeeId}")
    @Operation(summary = "Get Employee Salary Disbursement History / Payslips")
    public ResponseEntity<ApiResponse<Page<SalaryDisbursement>>> getEmployeeSalaryHistory(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SalaryDisbursement> history = payrollService.getEmployeeDisbursementHistory(employeeId, pageable);
        return ResponseEntity.ok(
                new ApiResponse<>("Salary history retrieved", 200, history)
        );
    }

    @GetMapping("/month/{month}")
    @Operation(summary = "Get All Salary Disbursements for a Given Month (e.g. 2026-03)")
    public ResponseEntity<ApiResponse<List<SalaryDisbursement>>> getDisbursementsForMonth(
            @PathVariable String month) {
        List<SalaryDisbursement> list = payrollService.getDisbursementsForMonth(month);
        return ResponseEntity.ok(
                new ApiResponse<>("Disbursements for " + month + " retrieved", 200, list)
        );
    }

    @PostMapping("/seed-demo-staff")
    @Operation(summary = "Seed Demo Departments, Employees, and Corporate Treasury")
    public ResponseEntity<ApiResponse<Object>> seedDemoStaff() {
        Object staff = payrollService.seedDemoStaff();
        return ResponseEntity.ok(
                new ApiResponse<>("Demo staff and wallets seeded successfully", 200, staff)
        );
    }
}
