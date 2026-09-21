package com.rps.finwallet.repository;

import com.rps.finwallet.model.*;
import com.rps.finwallet.repository.*;
import com.rps.finwallet.service.*;
import com.rps.finwallet.controller.*;
import com.rps.finwallet.dto.*;
import com.rps.finwallet.exception.*;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EmployeeRepository extends  JpaRepository<Employee, Long>{
    List<Employee> findByDepartmentId(Long departmentId);

    @org.springframework.data.jpa.repository.Query(
            "SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List <Employee> searchEmployeesByName(@org.springframework.data.repository.query.Param("keyword") String keyword);



}