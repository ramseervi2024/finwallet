package com.rps.finwallet.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PayrollBatchResponse {

    private String salaryMonth;
    private int totalEmployeesTargeted;
    private int successfulDisbursements;
    private int failedDisbursements;
    private BigDecimal totalAmountDisbursed = BigDecimal.ZERO;
    private List<PayrollItemResult> details = new ArrayList<>();

    public static class PayrollItemResult {
        private Long employeeId;
        private String employeeName;
        private BigDecimal amount;
        private String status; // "SUCCESS", "FAILED", "SKIPPED_ALREADY_PAID"
        private String message;
        private String referenceNumber;

        public PayrollItemResult() {
        }

        public PayrollItemResult(Long employeeId, String employeeName, BigDecimal amount, String status, String message, String referenceNumber) {
            this.employeeId = employeeId;
            this.employeeName = employeeName;
            this.amount = amount;
            this.status = status;
            this.message = message;
            this.referenceNumber = referenceNumber;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Long employeeId) {
            this.employeeId = employeeId;
        }

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getReferenceNumber() {
            return referenceNumber;
        }

        public void setReferenceNumber(String referenceNumber) {
            this.referenceNumber = referenceNumber;
        }
    }

    public PayrollBatchResponse() {
    }

    public PayrollBatchResponse(String salaryMonth) {
        this.salaryMonth = salaryMonth;
    }

    public String getSalaryMonth() {
        return salaryMonth;
    }

    public void setSalaryMonth(String salaryMonth) {
        this.salaryMonth = salaryMonth;
    }

    public int getTotalEmployeesTargeted() {
        return totalEmployeesTargeted;
    }

    public void setTotalEmployeesTargeted(int totalEmployeesTargeted) {
        this.totalEmployeesTargeted = totalEmployeesTargeted;
    }

    public int getSuccessfulDisbursements() {
        return successfulDisbursements;
    }

    public void setSuccessfulDisbursements(int successfulDisbursements) {
        this.successfulDisbursements = successfulDisbursements;
    }

    public int getFailedDisbursements() {
        return failedDisbursements;
    }

    public void setFailedDisbursements(int failedDisbursements) {
        this.failedDisbursements = failedDisbursements;
    }

    public BigDecimal getTotalAmountDisbursed() {
        return totalAmountDisbursed;
    }

    public void setTotalAmountDisbursed(BigDecimal totalAmountDisbursed) {
        this.totalAmountDisbursed = totalAmountDisbursed;
    }

    public List<PayrollItemResult> getDetails() {
        return details;
    }

    public void setDetails(List<PayrollItemResult> details) {
        this.details = details;
    }
}
