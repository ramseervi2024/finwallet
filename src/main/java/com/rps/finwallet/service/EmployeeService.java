package com.rps.finwallet.service;

import com.rps.finwallet.model.*;
import com.rps.finwallet.repository.*;
import com.rps.finwallet.service.*;
import com.rps.finwallet.controller.*;
import com.rps.finwallet.dto.*;
import com.rps.finwallet.exception.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.rps.finwallet.dto.EmployeeRequest;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository){
        this.employeeRepository=employeeRepository;
        this.departmentRepository=departmentRepository;
    }

    public Page<Employee> getAllEmployees(int pageNumber, int pageSize){
        Pageable pageable= PageRequest.of(pageNumber, pageSize);
        return employeeRepository.findAll(pageable);
    }

    public Employee getEmployeeById(Long id){
        return employeeRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Employee not fount with ID: " + id));
    }

    public Employee createEmployee(EmployeeRequest request){
        Employee employee=new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());

        if(request.getDepartmentId() !=null){
            Department dept=departmentRepository.findById(request.getDepartmentId()).orElseThrow(()->
                    new RuntimeException("Department not found!"));
            employee.setDepartment(dept);
        }

        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, EmployeeRequest updatedEmployee){
        Employee employee = getEmployeeById(id);

        employee.setName(updatedEmployee.getName());
        employee.setEmail(updatedEmployee.getEmail());
        if(updatedEmployee.getDepartmentId() !=null){
            Department dept=departmentRepository.findById(updatedEmployee.getDepartmentId()).orElseThrow(()->
                    new RuntimeException("Department not found!"));
            employee.setDepartment(dept);
        }

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id){
        Employee employee =getEmployeeById(id);
        employeeRepository.delete(employee);
    }


    public List<Employee> getEmployeesByDepartment(Long departmentId){
        return employeeRepository.findByDepartmentId(departmentId);
    }

    public  List<Employee> searchEmployees(String keyword){
        return employeeRepository.searchEmployeesByName(keyword);
    }
}
