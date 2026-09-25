package com.rps.finwallet.controller;

import com.rps.finwallet.common.ApiResponse;
import com.rps.finwallet.dto.TreasuryDepositRequest;
import com.rps.finwallet.model.CompanyTreasury;
import com.rps.finwallet.service.CompanyTreasuryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/treasury")
@Tag(name = "Corporate Treasury", description = "Endpoints for managing company treasury funds for payroll")
public class CompanyTreasuryController {

    private final CompanyTreasuryService treasuryService;

    public CompanyTreasuryController(CompanyTreasuryService treasuryService) {
        this.treasuryService = treasuryService;
    }

    @GetMapping
    @Operation(summary = "Get Corporate Treasury Balance & Status")
    public ResponseEntity<ApiResponse<CompanyTreasury>> getTreasuryDetails() {
        CompanyTreasury treasury = treasuryService.getTreasuryDetails();
        return ResponseEntity.ok(new ApiResponse<>("Corporate treasury retrieved successfully", 200, treasury));
    }

    @PostMapping("/deposit")
    @Operation(summary = "Deposit / Capitalize Funds into Corporate Treasury")
    public ResponseEntity<ApiResponse<CompanyTreasury>> depositFunds(@Valid @RequestBody TreasuryDepositRequest request) {
        CompanyTreasury updated = treasuryService.depositToTreasury(request);
        return ResponseEntity.ok(new ApiResponse<>("Funds deposited to treasury successfully", 200, updated));
    }
}
