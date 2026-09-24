package com.rps.finwallet.controller;

import com.rps.finwallet.common.ApiResponse;
import com.rps.finwallet.service.*;

import com.rps.finwallet.dto.EmployeeRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // 1. GET API - Fetch all employees
    @GetMapping
    public ApiResponse getEmployees(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return new ApiResponse("Employees fetched successfully", 200, employeeService.getAllEmployees(page , size));
    }

    // 2. GET API - Fetch employee by ID
    @GetMapping("/{id}")
    public ApiResponse getEmployeeById(@PathVariable Long id) {
        return new ApiResponse("Employee fetched successfully", 200, employeeService.getEmployeeById(id));
    }

    // 3. POST API - Create a new employee
    @PostMapping
    public ApiResponse createEmployee(@Valid @RequestBody EmployeeRequest request) {
        return new ApiResponse("Employee created successfully", 200, employeeService.createEmployee(request));
    }

    // 4. PUT API - Update an existing employee
    @PutMapping("/{id}")
    public ApiResponse updateEmployee(@PathVariable Long id,@Valid @RequestBody EmployeeRequest updatedEmployee) {
        return new ApiResponse("Employee ID: " + id + " updated successfully", 200, employeeService.updateEmployee(id, updatedEmployee));
    }

    // 5. DELETE API - Delete an employee
    @DeleteMapping("/{id}")
    public ApiResponse deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ApiResponse("Employee deleted successfully: " + id, 200);
    }


    @GetMapping("/department/{departmentId}")
    public ApiResponse getEmployeesByDepartment(@PathVariable Long departmentId){
        return new ApiResponse("Fetched employees for department", 200, employeeService.getEmployeesByDepartment(departmentId));
    }

    @GetMapping("/search")
    public ApiResponse searchEmployees(@RequestParam String name){
        return  new ApiResponse("Search results", 200, employeeService.searchEmployees(name));
    }
}
