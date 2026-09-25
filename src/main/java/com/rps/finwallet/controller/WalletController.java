package com.rps.finwallet.controller;

import com.rps.finwallet.common.ApiResponse;
import com.rps.finwallet.dto.TransferRequest;
import com.rps.finwallet.dto.WithdrawRequest;
import com.rps.finwallet.model.TransactionLedger;
import com.rps.finwallet.model.Wallet;
import com.rps.finwallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
@Tag(name = "Employee Salary Wallets", description = "Endpoints for employee wallet balances, transfers, withdrawals, and passbook")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get Employee Wallet Details & Balance")
    public ResponseEntity<ApiResponse<Wallet>> getEmployeeWallet(@PathVariable Long employeeId) {
        Wallet wallet = walletService.getWalletByEmployeeId(employeeId);
        return ResponseEntity.ok(new ApiResponse<>("Wallet retrieved successfully", 200, wallet));
    }

    @PostMapping("/{employeeId}/transfer")
    @Operation(summary = "Transfer Funds from Salary Wallet to Another Employee Wallet")
    public ResponseEntity<ApiResponse<TransactionLedger>> transferMoney(
            @PathVariable Long employeeId,
            @Valid @RequestBody TransferRequest request) {
        TransactionLedger ledger = walletService.transferFunds(employeeId, request);
        return ResponseEntity.ok(new ApiResponse<>("Transfer completed successfully", 200, ledger));
    }

    @PostMapping("/{employeeId}/withdraw")
    @Operation(summary = "Withdraw Funds from Salary Wallet to Bank Account")
    public ResponseEntity<ApiResponse<TransactionLedger>> withdrawMoney(
            @PathVariable Long employeeId,
            @Valid @RequestBody WithdrawRequest request) {
        TransactionLedger ledger = walletService.withdrawFunds(employeeId, request);
        return ResponseEntity.ok(new ApiResponse<>("Withdrawal completed successfully", 200, ledger));
    }

    @GetMapping("/{employeeId}/passbook")
    @Operation(summary = "Get Paginated Wallet Passbook / Transaction Statement")
    public ResponseEntity<ApiResponse<Page<TransactionLedger>>> getPassbook(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionLedger> passbook = walletService.getWalletPassbook(employeeId, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Passbook retrieved successfully", 200, passbook));
    }
}
