package com.rps.finwallet.dto;

import jakarta.validation.constraints.*;

public class EmployeeRequest {

    @NotBlank(message="Name cannot be empty")
    private String name;

    @Email(message = "Invalid email formate")
    @NotBlank(message="Email is Mandotory")
    private String email;

    private String department;


}
