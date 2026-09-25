package com.rps.finwallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class BulkPayrollRequest {

    private Long departmentId; // Optional: If null, process payroll for all active employees across company

    @NotBlank(message = "Salary month is mandatory")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "Salary month must follow 'YYYY-MM' format (e.g. 2026-03)")
    private String salaryMonth;

    private String notes;

    public BulkPayrollRequest() {
    }

    public BulkPayrollRequest(Long departmentId, String salaryMonth, String notes) {
        this.departmentId = departmentId;
        this.salaryMonth = salaryMonth;
        this.notes = notes;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getSalaryMonth() {
        return salaryMonth;
    }

    public void setSalaryMonth(String salaryMonth) {
        this.salaryMonth = salaryMonth;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
