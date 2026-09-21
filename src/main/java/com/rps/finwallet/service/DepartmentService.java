package com.rps.finwallet.service;

import com.rps.finwallet.model.*;
import com.rps.finwallet.repository.*;
import com.rps.finwallet.service.*;
import com.rps.finwallet.controller.*;
import com.rps.finwallet.dto.*;
import com.rps.finwallet.exception.*;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository){
        this.departmentRepository=departmentRepository;
    }

    public Department createDepartment(Department department){
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments(){
        return departmentRepository.findAll();
    }

    public void deleteDepartment(Long id){
        Department department=departmentRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Department not found with Id : "+ id));
        departmentRepository.delete(department);
    }



}
