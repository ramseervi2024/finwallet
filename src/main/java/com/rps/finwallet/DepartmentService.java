package com.rps.finwallet;

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
