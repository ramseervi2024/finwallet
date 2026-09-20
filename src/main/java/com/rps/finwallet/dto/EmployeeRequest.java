package com.rps.finwallet.dto;

import jakarta.validation.constraints.*;

public class EmployeeRequest {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    private String department;


}
