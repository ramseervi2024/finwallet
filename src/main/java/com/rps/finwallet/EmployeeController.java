package com.rps.finwallet;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    // GET API
    @GetMapping
    public ApiResponse getEmployess() {
        return new ApiResponse(
                "Get API hit successfully Ramesh RPS", 200);
    }
    @GetMapping("/getemployeeslist")
    public ApiResponse getEmployessList() {
        return new ApiResponse(
                "Get Employee by ID", 200);
    }
    @PostMapping
    public ApiResponse createEmployee(@RequestBody Employee employee) {
        return new ApiResponse("Employee Created Succefully" + employee.getName(), 200);
    }

    @PutMapping("/{id}")
    public ApiResponse updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return new ApiResponse("Employee ID : " + id + " updated Succefully" + employee.getName(), 200);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteEmployee(@PathVariable Long id) {
        return new ApiResponse("Employee Delete Successfully: " + id, 200);
    }

}
