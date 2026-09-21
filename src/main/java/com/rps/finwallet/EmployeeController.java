package com.rps.finwallet;

import com.rps.finwallet.dto.EmployeeRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    
//    // In-memory list to store employees temporarily (act as a fake database)
//    private final List<Employee> employees = new ArrayList<>();

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService=employeeService;
    }

    // 1. GET API - Fetch all employees
    @GetMapping
    public ApiResponse getEmployees(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return new ApiResponse("Employees fetched successfully", 200, employeeService.getAllEmployees(page , size));
    }

    // 2. GET API - Fetch employee by ID
    @GetMapping("/{id}")
    public ApiResponse getEmployeeById(@PathVariable Long id) {
//        Employee employee = employees.stream()
//                .filter(emp -> emp.getId().equals(id))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
//
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
//        Employee employee = employees.stream()
//                .filter(emp -> emp.getId().equals(id))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
//
//        // Updating the existing employee's details
//        employee.setName(updatedEmployee.getName());
//        employee.setEmail(updatedEmployee.getEmail());
//        employee.setDepartment(updatedEmployee.getDepartment());
//
        return new ApiResponse("Employee ID: " + id + " updated successfully", 200, employeeService.updateEmployee(id, updatedEmployee));
    }

    // 5. DELETE API - Delete an employee
    @DeleteMapping("/{id}")
    public ApiResponse deleteEmployee(@PathVariable Long id) {
//        Employee employee = employees.stream()
//                .filter(emp -> emp.getId().equals(id))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
//
        // Removing from the list
//        employees.remove(employee);
        employeeService.deleteEmployee(id);
        return new ApiResponse("Employee deleted successfully: " + id, 200);
    }
}
