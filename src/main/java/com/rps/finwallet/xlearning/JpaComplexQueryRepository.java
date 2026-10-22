package com.rps.finwallet.xlearning;

import com.rps.finwallet.model.Employee;
import com.rps.finwallet.model.EmployeeStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * 🚀 JPA & HIBERNATE COMPLETE ADVANCED QUERY REPOSITORY
 * ============================================================================
 * Is file me basic se lekar advanced limit tak ke saare query patterns hain:
 * 1. Derived Queries (Naming convention se automatic SQL)
 * 2. Joins (INNER, LEFT, RIGHT, JOIN FETCH)
 * 3. Projections (DTO Constructor & Interface Projections)
 * 4. Aggregations (COUNT, SUM, AVG, MIN, MAX + GROUP BY + HAVING)
 * 5. Conditional Logic (CASE WHEN THEN ELSE END, COALESCE)
 * 6. Subqueries (EXISTS, NOT EXISTS, Scalar Subquery)
 * 7. Pagination & Slicing (Page vs Slice)
 * 8. Bulk Operations (@Modifying updates/deletes)
 * 9. Concurrency & Locking (Pessimistic Locking / SELECT FOR UPDATE)
 * 10. Native SQL with Advanced MySQL Window Functions
 * ============================================================================
 */
@Repository
public interface JpaComplexQueryRepository extends JpaRepository<Employee, Long> {

    // =========================================================================
    // 1. DERIVED QUERIES (JPA Method Name se Automatic SQL banna)
    // =========================================================================
    // Hinglish: Method name dekh kar Spring Data JPA khud SQL generate kar deta hai.
    // SQL: SELECT * FROM employees WHERE status = ? AND base_salary >= ? ORDER BY name ASC;
    List<Employee> findByStatusAndBaseSalaryGreaterThanEqualOrderByNameAsc(
            EmployeeStatus status, BigDecimal minSalary);

    // SQL: SELECT * FROM employees WHERE email LIKE '%@gmail.com'
    List<Employee> findByEmailEndingWithIgnoreCase(String domain);

    // =========================================================================
    // 2. INNER JOIN (Dono tables me jo match karega sirf wahi aayega)
    // =========================================================================
    // Hinglish: Jis employee ka department assigned nahi hai wo filter out ho jayega.
    @Query("SELECT e FROM Employee e JOIN e.department d WHERE d.name = :deptName")
    List<Employee> findEmployeesByDeptNameInnerJoin(@Param("deptName") String deptName);

    // =========================================================================
    // 3. LEFT JOIN (Left table ke saare records + Right ke matching records)
    // =========================================================================
    // Hinglish: Har employee aayega, bhale hi uska koi department assigned na ho (null department).
    @Query("SELECT e FROM Employee e LEFT JOIN e.department d ORDER BY e.name ASC")
    List<Employee> findAllEmployeesWithDeptLeftJoin();

    // =========================================================================
    // 4. JOIN FETCH (Hibernate N+1 Problem ka Ultimate Solution)
    // =========================================================================
    // Hinglish: Normal JOIN me department lazy load hota hai (100 employee ke liye 101 queries).
    // JOIN FETCH likhne se Hibernate 1 single query me Employee aur Department sath me load kar deta hai!
    @Query("SELECT DISTINCT e FROM Employee e JOIN FETCH e.department")
    List<Employee> findAllEmployeesEagerlyWithJoinFetch();

    // =========================================================================
    // 5. COMPLEX AGGREGATION: GROUP BY, COUNT, SUM, AVG, HAVING
    // =========================================================================
    // Hinglish:
    // WHERE = individual rows filter karta hai (grouping se pehle).
    // HAVING = grouped values par filter lagata hai (grouping ke baad).
    @Query("""
        SELECT d.name, COUNT(e.id), AVG(e.baseSalary), SUM(e.baseSalary)
        FROM Employee e JOIN e.department d
        GROUP BY d.id, d.name
        HAVING SUM(e.baseSalary) >= :minTotalSalary
    """)
    List<Object[]> getDepartmentSalarySummaryHaving(@Param("minTotalSalary") BigDecimal minTotalSalary);

    // =========================================================================
    // 6. SUBQUERIES: Scalar Subquery, EXISTS & NOT EXISTS
    // =========================================================================
    // Hinglish 6A: Subquery jo average salary se upar kamane wale employees nikaalti hai
    @Query("""
        SELECT e FROM Employee e
        WHERE e.baseSalary > (SELECT AVG(sub.baseSalary) FROM Employee sub)
    """)
    List<Employee> findEmployeesEarningAboveAverage();

    // Hinglish 6B: EXISTS Subquery - Sirf wo departments jinke paas kam se kam 1 active employee ho
    @Query("""
        SELECT d FROM Department d
        WHERE EXISTS (
            SELECT 1 FROM Employee e
            WHERE e.department = d AND e.status = com.rps.finwallet.model.EmployeeStatus.ACTIVE
        )
    """)
    List<com.rps.finwallet.model.Department> findDepartmentsWithActiveEmployees();

    // Hinglish 6C: NOT EXISTS Subquery - Wo employees jinka koi wallet record create nahi hua hai
    @Query("""
        SELECT e FROM Employee e
        WHERE NOT EXISTS (
            SELECT 1 FROM com.rps.finwallet.model.Wallet w WHERE w.employee = e
        )
    """)
    List<Employee> findEmployeesWithoutWallet();

    // =========================================================================
    // 7. CONDITIONAL LOGIC: CASE WHEN THEN ELSE END & COALESCE
    // =========================================================================
    // Hinglish: Query level par if-else lagana!
    @Query("""
        SELECT e.name,
               e.baseSalary,
               CASE
                   WHEN e.baseSalary >= 100000 THEN 'HIGH_TIER'
                   WHEN e.baseSalary >= 50000 THEN 'MID_TIER'
                   ELSE 'ENTRY_TIER'
               END,
               COALESCE(e.department.name, 'No Department Assigned')
        FROM Employee e
    """)
    List<Object[]> getEmployeeSalaryTierCategorization();

    // =========================================================================
    // 8. PROJECTIONS (Performance Optimization: Pure Entity ke bajaye chuni hui columns)
    // =========================================================================
    // Hinglish 8A: DTO Constructor Projection (new com.rps...DTO(...))
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

    // Hinglish 8B: Interface-based Dynamic Projection (Spring creates dynamic proxy)
    @Query("SELECT e.name AS name, e.email AS email, e.baseSalary AS baseSalary FROM Employee e WHERE e.department.name = :deptName")
    List<EmployeeNameDeptProjection> findProjectionsByDeptName(@Param("deptName") String deptName);

    // =========================================================================
    // 9. PAGINATION & SLICING (Bade data ko chunks me load karna)
    // =========================================================================
    // Hinglish 9A: Page<T> = Data + Total Count (total records aur total pages calculate karta hai - Admin tables ke liye best)
    @Query("SELECT e FROM Employee e WHERE e.status = :status")
    Page<Employee> findEmployeesWithPagination(@Param("status") EmployeeStatus status, Pageable pageable);

    // Hinglish 9B: Slice<T> = Sirf Data + hasNext() (Total count query nahi chalata! Mobile app infinite scroll ke liye best)
    @Query("SELECT e FROM Employee e ORDER BY e.id DESC")
    Slice<Employee> findEmployeesWithInfiniteScroll(Pageable pageable);

    // =========================================================================
    // 10. BULK UPDATES / DELETES (@Modifying + @Transactional)
    // =========================================================================
    // Hinglish:
    // clearAutomatically = true: Update hone ke baad Hibernate first-level cache ko clear kar deta hai,
    // taaki stale / purana data memory me na rahe!
    // flushAutomatically = true: Query chalne se pehle pending changes DB me flush kar deta hai.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Employee e SET e.baseSalary = e.baseSalary * :multiplier WHERE e.department.id = :deptId")
    int giveDepartmentSalaryHike(@Param("deptId") Long deptId, @Param("multiplier") BigDecimal multiplier);

    // =========================================================================
    // 11. PESSIMISTIC LOCKING (Concurrency & Race Condition Prevention)
    // =========================================================================
    // Hinglish: DB row par 'SELECT ... FOR UPDATE' lock lagata hai.
    // Jab tak transaction complete nahi hoti, koi dusra thread is row ko modify nahi kar sakta!
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Employee e WHERE e.id = :id")
    Optional<Employee> findAndLockEmployeeById(@Param("id") Long id);

    // =========================================================================
    // 12. ADVANCED NATIVE SQL WITH WINDOW FUNCTIONS (MySQL 8.0+)
    // =========================================================================
    // Hinglish: DENSE_RANK() window function se department ke andar highest salary ranking nikaalna!
    @Query(value = """
        SELECT e.name AS emp_name,
               d.name AS dept_name,
               e.base_salary AS salary,
               DENSE_RANK() OVER (PARTITION BY e.department_id ORDER BY e.base_salary DESC) as salary_rank
        FROM employees e
        INNER JOIN departments d ON e.department_id = d.id
        WHERE e.status = 'ACTIVE'
    """, nativeQuery = true)
    List<Object[]> findDepartmentWiseSalaryRanksNativeSql();
}
