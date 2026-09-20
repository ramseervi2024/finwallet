package com.rps.finwallet;

import org.springframework.stereotype.Service;
import com.rps.finwallet.dto.EmployeeRequest;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository=employeeRepository;
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
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
