package com.rps.finwallet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.rps.finwallet.dto.EmployeeRequest;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository=employeeRepository;
    }

    public Page<Employee> getAllEmployees(int pageNumber, int pageSize){
        Pageable pageable= PageRequest.of(pageNumber, pageSize);
        return employeeRepository.findAll(pageable);
    }

    public Employee getEmployeeById(Long id){
        return employeeRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Employee not fount with ID: " + id));
    }

    public Employee createEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, EmployeeRequest updatedEmployee){
        Employee employee = getEmployeeById(id);

        employee.setName(updatedEmployee.getName());
        employee.setEmail(updatedEmployee.getEmail());
        employee.setDepartment(updatedEmployee.getDepartment());

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id){
        Employee employee =getEmployeeById(id);
        employeeRepository.delete(employee);
    }

}
