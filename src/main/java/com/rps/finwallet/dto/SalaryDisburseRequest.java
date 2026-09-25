package com.rps.finwallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class SalaryDisburseRequest {

    @NotNull(message = "Employee ID is mandatory")
    private Long employeeId;

    @NotBlank(message = "Salary month is mandatory")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "Salary month must follow 'YYYY-MM' format (e.g. 2026-03)")
    private String salaryMonth;

    private String notes;

    public SalaryDisburseRequest() {
    }

    public SalaryDisburseRequest(Long employeeId, String salaryMonth, String notes) {
        this.employeeId = employeeId;
        this.salaryMonth = salaryMonth;
        this.notes = notes;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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
