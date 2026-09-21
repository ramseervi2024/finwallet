package com.rps.finwallet.dto;

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


}
