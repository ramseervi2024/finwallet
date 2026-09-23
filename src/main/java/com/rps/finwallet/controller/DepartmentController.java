package com.rps.finwallet.controller;

import com.rps.finwallet.common.ApiResponse;
import com.rps.finwallet.model.*;
import com.rps.finwallet.service.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    private  final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService){
        this.departmentService=departmentService;
    }

    @PostMapping
    public Department createDepartment(@RequestBody Department department){
        return departmentService.createDepartment(department);
    }

    @GetMapping
    public List<Department> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteDepartment(@PathVariable Long id){
        departmentService.deleteDepartment(id);
        return new ApiResponse("Department ID " + id + " and all its employees deleted successfully!", 200);
    }

    @PutMapping("/{id}")
    public Department updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }
}