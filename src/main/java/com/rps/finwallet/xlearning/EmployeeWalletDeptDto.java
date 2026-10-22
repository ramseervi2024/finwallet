package com.rps.finwallet.xlearning;

import java.math.BigDecimal;

/**
 * DTO Projection: Used to fetch only specific joined columns
 * instead of loading entire heavy Entity objects into memory.
 */
public class EmployeeWalletDeptDto {

    private Long employeeId;
    private String employeeName;
    private String departmentName;
    private BigDecimal baseSalary;
    private String walletNumber;
    private BigDecimal walletBalance;

    // JPQL Constructor Expression requires exact matching constructor
    public EmployeeWalletDeptDto(Long employeeId, String employeeName, String departmentName,
                                 BigDecimal baseSalary, String walletNumber, BigDecimal walletBalance) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.departmentName = departmentName != null ? departmentName : "No Department";
        this.baseSalary = baseSalary != null ? baseSalary : BigDecimal.ZERO;
        this.walletNumber = walletNumber != null ? walletNumber : "No Wallet";
        this.walletBalance = walletBalance != null ? walletBalance : BigDecimal.ZERO;
    }

    public Long getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public String getDepartmentName() { return departmentName; }
    public BigDecimal getBaseSalary() { return baseSalary; }
    public String getWalletNumber() { return walletNumber; }
    public BigDecimal getWalletBalance() { return walletBalance; }

    @Override
    public String toString() {
        return String.format("Emp: %-10s | Dept: %-12s | Salary: ₹%-8s | Wallet: %-12s | Bal: ₹%s",
                employeeName, departmentName, baseSalary, walletNumber, walletBalance);
    }
}
