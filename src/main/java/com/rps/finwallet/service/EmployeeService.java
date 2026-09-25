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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import com.rps.finwallet.dto.EmployeeRequest;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final WalletService walletService;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository,
                           WalletService walletService){
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.walletService = walletService;
    }

    /**
     * @Cacheable: On FIRST call, hits the DB and stores result in cache named "employees".
     * On SECOND call with same params, returns from cache WITHOUT hitting DB.
     * key="'all'" → a fixed string key (note the single quotes inside double quotes)
     *
     * PROOF: You will see ">>> FROM DATABASE" printed in console ONLY on the first call.
     * On the second call, nothing is printed — data came from cache!
     */
    @Cacheable(value = "employees", key = "#pageNumber + '_' + #pageSize")
    public Page<Employee> getAllEmployees(int pageNumber, int pageSize){
        System.out.println(">>> CACHE MISS: Fetching employees page=" + pageNumber + " size=" + pageSize + " FROM DATABASE");
        Pageable pageable= PageRequest.of(pageNumber, pageSize);
        return employeeRepository.findAll(pageable);
    }

    /**
     * @Cacheable: Caches each employee individually by their ID.
     * key="#id" → uses the actual method parameter as the cache key.
     * Cache stores: employees[1]=Employee{Ramesh}, employees[2]=Employee{Suresh}
     */
    @Cacheable(value = "employees", key = "#id")
    public Employee getEmployeeById(Long id){
        System.out.println(">>> CACHE MISS: Fetching employee ID=" + id + " FROM DATABASE");
        return employeeRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Employee not fount with ID: " + id));
    }

    /**
     * @CacheEvict: After creating a new employee, the cached "all employees" list
     * is now STALE (it's missing the new employee). So we evict it to force a
     * fresh DB fetch on the next getAllEmployees() call.
     */
    @CacheEvict(value = "employees", allEntries = true)
    public Employee createEmployee(EmployeeRequest request){
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        if (request.getBaseSalary() != null) {
            employee.setBaseSalary(request.getBaseSalary());
        }
        if (request.getDesignation() != null) {
            employee.setDesignation(request.getDesignation());
        }
        if (request.getStatus() != null) {
            employee.setStatus(request.getStatus());
        }

        if(request.getDepartmentId() != null){
            Department dept = departmentRepository.findById(request.getDepartmentId()).orElseThrow(()->
                    new RuntimeException("Department not found!"));
            employee.setDepartment(dept);
        }

        Employee saved = employeeRepository.save(employee);
        // Automatically create and link a salary wallet for this employee
        walletService.getOrCreateWalletForEmployee(saved);

        return saved;
    }

    /**
     * @Caching: We need TWO cache operations together:
     *   1. @CachePut: Update the individual cache entry for this employee ID with new data.
     *      (So getEmployeeById(id) next time returns updated data from cache, NOT old data)
     *   2. @CacheEvict: Invalidate the "all" list since one employee's data changed.
     */
    @Caching(
        put = { @CachePut(value = "employees", key = "#id") },
        evict = { @CacheEvict(value = "employees", allEntries = true) }
    )
    public Employee updateEmployee(Long id, EmployeeRequest updatedEmployee){
        Employee employee = getEmployeeById(id);

        employee.setName(updatedEmployee.getName());
        employee.setEmail(updatedEmployee.getEmail());
        if (updatedEmployee.getBaseSalary() != null) {
            employee.setBaseSalary(updatedEmployee.getBaseSalary());
        }
        if (updatedEmployee.getDesignation() != null) {
            employee.setDesignation(updatedEmployee.getDesignation());
        }
        if (updatedEmployee.getStatus() != null) {
            employee.setStatus(updatedEmployee.getStatus());
        }

        if(updatedEmployee.getDepartmentId() != null){
            Department dept = departmentRepository.findById(updatedEmployee.getDepartmentId()).orElseThrow(()->
                    new RuntimeException("Department not found!"));
            employee.setDepartment(dept);
        }

        return employeeRepository.save(employee);
    }

    /**
     * @Caching with multiple @CacheEvict: On delete, remove BOTH cache entries:
     *   1. The individual employee's entry (key="#id")
     *   2. The full "all employees" list (key="'all'")
     */
    @Caching(evict = {
        @CacheEvict(value = "employees", key = "#id"),
        @CacheEvict(value = "employees", allEntries = true)
    })
    public void deleteEmployee(Long id){
        Employee employee =getEmployeeById(id);
        employeeRepository.delete(employee);
    }


    public List<Employee> getEmployeesByDepartment(Long departmentId){
        return employeeRepository.findByDepartmentId(departmentId);
    }

    public List<Employee> searchEmployees(String keyword){
        return employeeRepository.searchEmployeesByName(keyword);
    }
}
