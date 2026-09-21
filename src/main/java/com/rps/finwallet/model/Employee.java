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

    public Employee() {
    }

    public Employee(Long id, String name, String email, Department department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
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

}
