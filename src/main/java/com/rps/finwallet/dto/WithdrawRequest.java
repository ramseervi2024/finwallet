package com.rps.finwallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class WithdrawRequest {

    @NotBlank(message = "Destination bank account number is mandatory")
    private String bankAccountNumber;

    @NotNull(message = "Withdrawal amount is mandatory")
    @DecimalMin(value = "1.00", message = "Amount must be at least 1.00")
    private BigDecimal amount;

    private String notes;

    public WithdrawRequest() {
    }

    public WithdrawRequest(String bankAccountNumber, BigDecimal amount, String notes) {
        this.bankAccountNumber = bankAccountNumber;
        this.amount = amount;
        this.notes = notes;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
