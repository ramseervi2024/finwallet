package com.rps.finwallet.dto;

import com.rps.finwallet.model.*;
import com.rps.finwallet.repository.*;
import com.rps.finwallet.service.*;
import com.rps.finwallet.controller.*;
import com.rps.finwallet.dto.*;
import com.rps.finwallet.exception.*;

import jakarta.validation.constraints.*;

public class EmployeeRequest {

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

    @NotBlank(message="Name cannot be empty")
    private String name;

    @Email(message = "Invalid email formate")
    @NotBlank(message="Email is Mandotory")
    private String email;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    private Long departmentId;

    private java.math.BigDecimal baseSalary;
    private String designation;
    private EmployeeStatus status;

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
