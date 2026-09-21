package com.rps.finwallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rps.finwallet.model.*;
import com.rps.finwallet.repository.*;
import com.rps.finwallet.service.*;
import com.rps.finwallet.controller.*;
import com.rps.finwallet.dto.*;
import com.rps.finwallet.exception.*;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="departments")
public class Department {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;

    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade=CascadeType.ALL)
    private List<Employee> employees;




}
