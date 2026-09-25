package com.rps.finwallet.model;

import com.rps.finwallet.model.*;
import com.rps.finwallet.repository.*;
import com.rps.finwallet.service.*;
import com.rps.finwallet.controller.*;
import com.rps.finwallet.dto.*;
import com.rps.finwallet.exception.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="employees")

public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message="Name cannot be empty")
    private String name;
    @Email(message="Invalid email formate")
    @NotBlank(message="Email is Mandatory")
    private String email;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "base_salary", precision = 15, scale = 2)
    private java.math.BigDecimal baseSalary = java.math.BigDecimal.ZERO;

    private String designation;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    public Employee() {
    }

    public Employee(Long id, String name, String email, Department department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.status = EmployeeStatus.ACTIVE;
        this.baseSalary = java.math.BigDecimal.ZERO;
    }

    public Employee(Long id, String name, String email, Department department, java.math.BigDecimal baseSalary, String designation, EmployeeStatus status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.baseSalary = baseSalary != null ? baseSalary : java.math.BigDecimal.ZERO;
        this.designation = designation;
        this.status = status != null ? status : EmployeeStatus.ACTIVE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public java.math.BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(java.math.BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }
}
