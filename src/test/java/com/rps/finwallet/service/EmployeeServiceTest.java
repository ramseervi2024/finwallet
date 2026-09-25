package com.rps.finwallet.service;

import com.rps.finwallet.dto.EmployeeRequest;
import com.rps.finwallet.model.Department;
import com.rps.finwallet.model.Employee;
import com.rps.finwallet.repository.DepartmentRepository;
import com.rps.finwallet.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for EmployeeService
 *
 * KEY CONCEPTS:
 * - @ExtendWith(MockitoExtension.class): Tells JUnit 5 to use Mockito for this test class
 * - @Mock: Creates a FAKE (mock) object. It does NOT hit the real database.
 * - @InjectMocks: Creates the REAL object (EmployeeService) and injects the @Mock objects into it.
 * - when(...).thenReturn(...): Configures the mock to return specific data
 * - verify(...): Confirms that a method was actually called on the mock
 * - assertEquals(...): Asserts the expected value matches the actual value
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    // @Mock creates FAKE versions of these repositories - no real DB calls happen
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private WalletService walletService;

    // @InjectMocks creates a REAL EmployeeService and injects the above mocks into it
    @InjectMocks
    private EmployeeService employeeService;

    // Test data - reused across tests
    private Employee employee1;
    private Employee employee2;
    private Department department;

    /**
     * @BeforeEach runs before EVERY single test method.
     * Use it to set up fresh test data so tests don't affect each other.
     */
    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Engineering");

        employee1 = new Employee(1L, "Ramesh", "ramesh@test.com", department);
        employee2 = new Employee(2L, "Suresh", "suresh@test.com", department);
    }

    // ==============================
    // TEST 1: Get All Employees
    // ==============================
    @Test
    @DisplayName("Should return a paginated list of all employees")
    void testGetAllEmployees_Success() {
        // ARRANGE: Tell the mock what to return when findAll is called
        List<Employee> employeeList = Arrays.asList(employee1, employee2);
        Page<Employee> mockPage = new PageImpl<>(employeeList, PageRequest.of(0, 5), 2);
        when(employeeRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        // ACT: Call the real service method
        Page<Employee> result = employeeService.getAllEmployees(0, 5);

        // ASSERT: Verify the result is correct
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Ramesh", result.getContent().get(0).getName());

        // VERIFY: Confirm that the repository was called exactly once
        verify(employeeRepository, times(1)).findAll(any(PageRequest.class));
    }

    // ==============================
    // TEST 2: Get Employee by ID - Found
    // ==============================
    @Test
    @DisplayName("Should return an employee when a valid ID is provided")
    void testGetEmployeeById_Found() {
        // ARRANGE: Mock the repository to return our test employee
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        // ACT
        Employee result = employeeService.getEmployeeById(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ramesh", result.getName());
        assertEquals("ramesh@test.com", result.getEmail());
    }

    // ==============================
    // TEST 3: Get Employee by ID - NOT Found (Exception Test)
    // ==============================
    @Test
    @DisplayName("Should throw RuntimeException when employee ID does not exist")
    void testGetEmployeeById_NotFound_ThrowsException() {
        // ARRANGE: Mock the repository to return empty (no employee found)
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT: Verify that our service throws the correct exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            employeeService.getEmployeeById(99L);
        });

        // Also verify the exception message is helpful
        assertTrue(exception.getMessage().contains("99"));
    }

    // ==============================
    // TEST 4: Create Employee - Success
    // ==============================
    @Test
    @DisplayName("Should create and save a new employee successfully")
    void testCreateEmployee_Success() {
        // ARRANGE: Build a request object
        EmployeeRequest request = new EmployeeRequest();
        request.setName("New Employee");
        request.setEmail("new@test.com");
        request.setDepartmentId(null); // No department

        Employee savedEmployee = new Employee(3L, "New Employee", "new@test.com", null);

        // Mock the save operation
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        // ACT
        Employee result = employeeService.createEmployee(request);

        // ASSERT
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("New Employee", result.getName());

        // VERIFY: save() was called exactly once
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    // ==============================
    // TEST 5: Create Employee with Department
    // ==============================
    @Test
    @DisplayName("Should create employee and link to department when departmentId is provided")
    void testCreateEmployee_WithDepartment_Success() {
        // ARRANGE
        EmployeeRequest request = new EmployeeRequest();
        request.setName("Ramesh");
        request.setEmail("ramesh@test.com");
        request.setDepartmentId(1L);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);

        // ACT
        Employee result = employeeService.createEmployee(request);

        // ASSERT
        assertNotNull(result);
        assertEquals("Engineering", result.getDepartment().getName());

        // VERIFY: department was looked up once
        verify(departmentRepository, times(1)).findById(1L);
    }

    // ==============================
    // TEST 6: Delete Employee - Success
    // ==============================
    @Test
    @DisplayName("Should delete an employee when a valid ID is provided")
    void testDeleteEmployee_Success() {
        // ARRANGE: Employee exists
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        doNothing().when(employeeRepository).delete(any(Employee.class));

        // ACT
        employeeService.deleteEmployee(1L);

        // VERIFY: delete() was called exactly once with the correct employee
        verify(employeeRepository, times(1)).delete(employee1);
    }

    // ==============================
    // TEST 7: Search Employees
    // ==============================
    @Test
    @DisplayName("Should return employees matching the search keyword")
    void testSearchEmployees_Success() {
        // ARRANGE
        when(employeeRepository.searchEmployeesByName("Ramesh"))
                .thenReturn(List.of(employee1));

        // ACT
        List<Employee> result = employeeService.searchEmployees("Ramesh");

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Ramesh", result.get(0).getName());
    }
}
