package com.rps.finwallet.xlearning;

import com.rps.finwallet.model.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service demonstrating how to communicate with JPA & Hibernate
 * for Complex Joins using both:
 * 1. Spring Data JPA Repository (@Query)
 * 2. Direct EntityManager (TypedQuery & Native Queries)
 */
@Service
@Transactional(readOnly = true)
public class JpaComplexQueryService {

    private final JpaComplexQueryRepository queryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public JpaComplexQueryService(JpaComplexQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    // =========================================================================
    // 1. Run All Complex Queries with Clean Formatted Logs
    // =========================================================================
    public void runAllDatabaseJoinExamples() {
        System.out.println("\n=======================================================");
        System.out.println("🗄️ [JPA REPOSITORY & HIBERNATE] DATABASE COMMUNICATOR");
        System.out.println("=======================================================");

        demonstrateLeftJoin();
        demonstrateJoinFetch();
        demonstrateGroupByHaving();
        demonstrateSubquery();
        demonstrateDtoProjection();
        demonstrateDirectEntityManagerUsage();

        System.out.println("\n=======================================================");
        System.out.println("✅ [JPA HIBERNATE] DATABASE QUERIES COMPLETED");
        System.out.println("=======================================================\n");
    }

    // =========================================================================
    // 2. Demo: LEFT JOIN
    // =========================================================================
    public void demonstrateLeftJoin() {
        System.out.println("\n--- [Query 1] LEFT JOIN: Employees with Department ---");
        List<Employee> employees = queryRepository.findAllEmployeesWithDeptLeftJoin();
        System.out.println("Total Employees Retrieved: " + employees.size());

        for (Employee emp : employees) {
            String deptName = (emp.getDepartment() != null) ? emp.getDepartment().getName() : "None";
            System.out.printf("   ID: %-3d | Name: %-12s | Dept: %-15s | Status: %s%n",
                    emp.getId(), emp.getName(), deptName, emp.getStatus());
        }
    }

    // =========================================================================
    // 3. Demo: JOIN FETCH (Prevents N+1 queries)
    // =========================================================================
    public void demonstrateJoinFetch() {
        System.out.println("\n--- [Query 2] JOIN FETCH (Eager Loading in 1 Single Query) ---");
        List<Employee> list = queryRepository.findAllEmployeesEagerlyWithJoinFetch();
        System.out.println("Retrieved " + list.size() + " employees eagerly with departments loaded.");
    }

    // =========================================================================
    // 4. Demo: GROUP BY + HAVING (Aggregations)
    // =========================================================================
    public void demonstrateGroupByHaving() {
        System.out.println("\n--- [Query 3] GROUP BY & HAVING: Dept Salary Expense >= ₹50,000 ---");
        List<Object[]> summaries = queryRepository.getDepartmentSalarySummaryHaving(BigDecimal.valueOf(50000));

        if (summaries.isEmpty()) {
            System.out.println("   No department exceeds the minimum expense threshold.");
        } else {
            for (Object[] row : summaries) {
                String deptName = (String) row[0];
                Long count = (Long) row[1];
                Double avgSalary = (Double) row[2];
                BigDecimal totalSalary = (BigDecimal) row[3];

                System.out.printf("   Dept: %-12s | Employees: %-2d | Avg: ₹%-9.2f | Total: ₹%s%n",
                        deptName, count, avgSalary, totalSalary);
            }
        }
    }

    // =========================================================================
    // 5. Demo: SUBQUERY (Above Average Salary)
    // =========================================================================
    public void demonstrateSubquery() {
        System.out.println("\n--- [Query 4] SUBQUERY: Employees Earning > Company Average Salary ---");
        List<Employee> topEarners = queryRepository.findEmployeesEarningAboveAverage();

        for (Employee emp : topEarners) {
            System.out.printf("   Employee: %-12s | Base Salary: ₹%s%n", emp.getName(), emp.getBaseSalary());
        }
    }

    // =========================================================================
    // 6. Demo: DTO Constructor Projection (3 Tables: Employee + Dept + Wallet)
    // =========================================================================
    public void demonstrateDtoProjection() {
        System.out.println("\n--- [Query 5] DTO Projection (Employee + Dept + Wallet 3-Way Join) ---");
        List<EmployeeWalletDeptDto> dtoList = queryRepository.fetchEmployeeWalletSummaryDto();

        for (EmployeeWalletDeptDto dto : dtoList) {
            System.out.println("   " + dto);
        }
    }

    // =========================================================================
    // 7. Demo: Direct EntityManager Communication (No Repository required)
    // =========================================================================
    public void demonstrateDirectEntityManagerUsage() {
        System.out.println("\n--- [Query 6] Direct EntityManager Communication ---");

        String jpql = "SELECT e FROM Employee e WHERE e.baseSalary >= :minSalary";
        TypedQuery<Employee> query = entityManager.createQuery(jpql, Employee.class);
        query.setParameter("minSalary", BigDecimal.valueOf(30000));
        query.setMaxResults(5); // LIMIT 5

        List<Employee> resultList = query.getResultList();
        System.out.println("   EntityManager TypedQuery returned " + resultList.size() + " high-salary employees.");
    }
}
