package com.rps.finwallet.xlearning;

import com.rps.finwallet.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spring Data JPA Repository containing real-world Complex Joins,
 * Aggregations, Subqueries, and DTO Projections.
 */
@Repository
public interface JpaComplexQueryRepository extends JpaRepository<Employee, Long> {

    // =========================================================================
    // 1. INNER JOIN (Only employees who have an assigned Department)
    // =========================================================================
    @Query("SELECT e FROM Employee e JOIN e.department d WHERE d.name = :deptName")
    List<Employee> findEmployeesByDeptNameInnerJoin(@Param("deptName") String deptName);

    // =========================================================================
    // 2. LEFT JOIN (All employees, including those with NULL department)
    // =========================================================================
    @Query("SELECT e FROM Employee e LEFT JOIN e.department d ORDER BY e.name ASC")
    List<Employee> findAllEmployeesWithDeptLeftJoin();

    // =========================================================================
    // 3. JOIN FETCH (Solves Hibernate N+1 Query Problem!)
    // Loads Employee AND Department in 1 single SQL SELECT without separate queries.
    // =========================================================================
    @Query("SELECT DISTINCT e FROM Employee e JOIN FETCH e.department")
    List<Employee> findAllEmployeesEagerlyWithJoinFetch();

    // =========================================================================
    // 4. COMPLEX AGGREGATION: GROUP BY, COUNT, SUM, HAVING
    // Calculates total salary expense per department, filtering groups with HAVING
    // =========================================================================
    @Query("""
        SELECT d.name, COUNT(e.id), AVG(e.baseSalary), SUM(e.baseSalary)
        FROM Employee e JOIN e.department d
        GROUP BY d.id, d.name
        HAVING SUM(e.baseSalary) >= :minTotalSalary
    """)
    List<Object[]> getDepartmentSalarySummaryHaving(@Param("minTotalSalary") BigDecimal minTotalSalary);

    // =========================================================================
    // 5. SUBQUERY: Find employees earning more than the company's average salary
    // =========================================================================
    @Query("""
        SELECT e FROM Employee e
        WHERE e.baseSalary > (SELECT AVG(sub.baseSalary) FROM Employee sub)
    """)
    List<Employee> findEmployeesEarningAboveAverage();

    // =========================================================================
    // 6. DTO CONSTRUCTOR PROJECTION (3-Table Join: Employee + Dept + Wallet)
    // Fetches only needed columns into a custom POJO for optimal memory & speed
    // =========================================================================
    @Query("""
        SELECT new com.rps.finwallet.xlearning.EmployeeWalletDeptDto(
            e.id,
            e.name,
            d.name,
            e.baseSalary,
            w.walletNumber,
            w.balance
        )
        FROM Employee e
        LEFT JOIN e.department d
        LEFT JOIN com.rps.finwallet.model.Wallet w ON w.employee = e
        ORDER BY e.id ASC
    """)
    List<EmployeeWalletDeptDto> fetchEmployeeWalletSummaryDto();

    // =========================================================================
    // 7. NATIVE SQL JOIN (Raw MySQL syntax using nativeQuery = true)
    // =========================================================================
    @Query(value = """
        SELECT e.name AS emp_name,
               COALESCE(d.name, 'Unassigned') AS dept_name,
               e.base_salary AS salary
        FROM employees e
        LEFT JOIN departments d ON e.department_id = d.id
        WHERE e.status = 'ACTIVE'
        ORDER BY e.base_salary DESC
    """, nativeQuery = true)
    List<Object[]> findActiveEmployeesNativeSql();
}
