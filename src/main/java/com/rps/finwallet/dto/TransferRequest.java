package com.rps.finwallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransferRequest {

    @NotBlank(message = "Recipient wallet number is required")
    private String targetWalletNumber;

    @NotNull(message = "Transfer amount is required")
    @DecimalMin(value = "1.00", message = "Amount must be at least 1.00")
    private BigDecimal amount;

    private String description;

    public TransferRequest() {
    }

    public TransferRequest(String targetWalletNumber, BigDecimal amount, String description) {
        this.targetWalletNumber = targetWalletNumber;
        this.amount = amount;
        this.description = description;
    }

    public String getTargetWalletNumber() {
        return targetWalletNumber;
    }

    public void setTargetWalletNumber(String targetWalletNumber) {
        this.targetWalletNumber = targetWalletNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
