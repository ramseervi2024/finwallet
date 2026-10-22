package com.rps.finwallet.xlearning;

import java.util.ArrayList;
import java.util.List;

/**
 * Complete Guide & Interactive Simulation for:
 * - SQL Joins (INNER, LEFT, RIGHT, FULL)
 * - JPA / JPQL vs Native SQL
 * - Hibernate JOIN FETCH (Solving N+1 Problem)
 * - Complex SQL: GROUP BY, HAVING, Aggregations & Subqueries
 */
public class JpaSqlPractice implements PracticeRunner {

    @Override
    public String getTitle() {
        return "JPA / Hibernate & SQL Joins Guide (INNER, LEFT, RIGHT, FETCH & Complex SQL)";
    }

    @Override
    public void run() {
        explainJoinTypesOverview();
        demonstrateInnerJoin();
        demonstrateLeftJoin();
        demonstrateRightJoin();
        explainJpaJoinFetchAndNPlusOne();
        explainComplexSqlGroupByHaving();
        explainSubqueriesAndProjections();
    }

    // =========================================================================
    // 1. Overview of Join Types
    // =========================================================================
    private void explainJoinTypesOverview() {
        System.out.println("\n--- [1] SQL & JPA Join Types Cheat Sheet ---");
        System.out.println("┌──────────────────┬────────────────────────────────────────────────────────┐");
        System.out.println("│ Join Type        │ Meaning                                                │");
        System.out.println("├──────────────────┼────────────────────────────────────────────────────────┤");
        System.out.println("│ INNER JOIN       │ Only matching rows present in BOTH tables              │");
        System.out.println("│ LEFT JOIN        │ ALL rows from Left table + matched from Right (or NULL)│");
        System.out.println("│ RIGHT JOIN       │ ALL rows from Right table + matched from Left (or NULL)│");
        System.out.println("│ FULL OUTER JOIN  │ All rows from both tables, filling NULLs where no match│");
        System.out.println("│ JOIN FETCH (JPA) │ Loads related entities in 1 single SQL query (solves N+1)│");
        System.out.println("└──────────────────┴────────────────────────────────────────────────────────┘");
    }

    // In-memory dummy records to visually demonstrate Joins
    record DummyDept(Long id, String name) {}
    record DummyEmp(Long id, String name, Long deptId, double salary) {}

    private final List<DummyDept> sampleDepts = List.of(
            new DummyDept(1L, "Engineering"),
            new DummyDept(2L, "Finance"),
            new DummyDept(3L, "Marketing") // No employees in Marketing
    );

    private final List<DummyEmp> sampleEmps = List.of(
            new DummyEmp(101L, "Ramesh", 1L, 85000),
            new DummyEmp(102L, "Amit", 1L, 60000),
            new DummyEmp(103L, "Priya", 2L, 75000),
            new DummyEmp(104L, "Suresh", null, 40000) // Suresh has NO department
    );

    // =========================================================================
    // 2. INNER JOIN
    // =========================================================================
    private void demonstrateInnerJoin() {
        System.out.println("\n--- [2] INNER JOIN ---");
        System.out.println("📌 Native SQL:");
        System.out.println("   SELECT e.name, d.name FROM employees e INNER JOIN departments d ON e.department_id = d.id;");
        System.out.println("📌 JPQL (Spring Data JPA):");
        System.out.println("   @Query(\"SELECT e.name, d.name FROM Employee e JOIN e.department d\")");

        System.out.println("\n🔍 Execution Result (Only matched pairs; Suresh & Marketing excluded):");
        for (DummyEmp emp : sampleEmps) {
            for (DummyDept dept : sampleDepts) {
                if (emp.deptId() != null && emp.deptId().equals(dept.id())) {
                    System.out.printf("   [Match] Employee: %-8s | Department: %-12s%n", emp.name(), dept.name());
                }
            }
        }
    }

    // =========================================================================
    // 3. LEFT JOIN
    // =========================================================================
    private void demonstrateLeftJoin() {
        System.out.println("\n--- [3] LEFT (OUTER) JOIN ---");
        System.out.println("📌 Native SQL:");
        System.out.println("   SELECT e.name, d.name FROM employees e LEFT JOIN departments d ON e.department_id = d.id;");
        System.out.println("📌 JPQL (Spring Data JPA):");
        System.out.println("   @Query(\"SELECT e.name, d.name FROM Employee e LEFT JOIN e.department d\")");

        System.out.println("\n🔍 Execution Result (Every employee included, Dept is NULL if unassigned):");
        for (DummyEmp emp : sampleEmps) {
            String deptName = "NULL (Unassigned)";
            for (DummyDept dept : sampleDepts) {
                if (emp.deptId() != null && emp.deptId().equals(dept.id())) {
                    deptName = dept.name();
                    break;
                }
            }
            System.out.printf("   Employee: %-8s | Department: %-12s%n", emp.name(), deptName);
        }
    }

    // =========================================================================
    // 4. RIGHT JOIN
    // =========================================================================
    private void demonstrateRightJoin() {
        System.out.println("\n--- [4] RIGHT (OUTER) JOIN ---");
        System.out.println("📌 Native SQL:");
        System.out.println("   SELECT d.name, e.name FROM employees e RIGHT JOIN departments d ON e.department_id = d.id;");
        System.out.println("📌 JPQL (Spring Data JPA):");
        System.out.println("   @Query(\"SELECT d.name, e.name FROM Employee e RIGHT JOIN e.department d\")");

        System.out.println("\n🔍 Execution Result (All departments included, even empty ones like Marketing):");
        for (DummyDept dept : sampleDepts) {
            List<String> assignedEmps = new ArrayList<>();
            for (DummyEmp emp : sampleEmps) {
                if (emp.deptId() != null && emp.deptId().equals(dept.id())) {
                    assignedEmps.add(emp.name());
                }
            }
            if (assignedEmps.isEmpty()) {
                System.out.printf("   Department: %-12s | Employee: NULL (No employees)%n", dept.name());
            } else {
                for (String empName : assignedEmps) {
                    System.out.printf("   Department: %-12s | Employee: %-8s%n", dept.name(), empName);
                }
            }
        }
    }

    // =========================================================================
    // 5. JPA JOIN FETCH (The N+1 Problem Solution)
    // =========================================================================
    private void explainJpaJoinFetchAndNPlusOne() {
        System.out.println("\n--- [5] JPA 'JOIN FETCH' vs Normal JOIN (N+1 Problem) ---");
        System.out.println("⚠️ The N+1 Problem:");
        System.out.println("   If you query: List<Employee> list = employeeRepo.findAll();");
        System.out.println("   1. Hibernate executes 1 query to get all 100 Employees.");
        System.out.println("   2. When you loop: emp.getDepartment().getName(), Hibernate fires 100 EXTRA queries!");
        System.out.println("   Total = 1 + 100 = 101 queries (Severe Performance Bottleneck!)\n");

        System.out.println("💡 The Solution: JPQL JOIN FETCH:");
        System.out.println("   @Query(\"SELECT e FROM Employee e JOIN FETCH e.department\")");
        System.out.println("   List<Employee> findAllWithDepartments();");
        System.out.println("   => Hibernate generates a SINGLE SQL query with INNER/LEFT JOIN and loads everything at once!");
    }

    // =========================================================================
    // 6. Complex SQL: GROUP BY, HAVING & Aggregations
    // =========================================================================
    private void explainComplexSqlGroupByHaving() {
        System.out.println("\n--- [6] Complex SQL: GROUP BY & HAVING ---");
        System.out.println("📌 Problem: Find departments having Total Salary Expense > 1,00,000:");
        System.out.println("   WHERE filters individual rows BEFORE grouping.");
        System.out.println("   HAVING filters aggregated groups AFTER grouping.\n");

        System.out.println("📌 Native SQL:");
        System.out.println("   SELECT d.name, COUNT(e.id) AS emp_count, SUM(e.base_salary) AS total_expense");
        System.out.println("   FROM departments d");
        System.out.println("   JOIN employees e ON d.id = e.department_id");
        System.out.println("   GROUP BY d.id, d.name");
        System.out.println("   HAVING SUM(e.base_salary) > 100000;");

        System.out.println("\n📌 JPQL Equivalent in Spring Data Repository:");
        System.out.println("   @Query(\"\"\"");
        System.out.println("       SELECT d.name, COUNT(e), SUM(e.baseSalary)");
        System.out.println("       FROM Employee e JOIN e.department d");
        System.out.println("       GROUP BY d.name");
        System.out.println("       HAVING SUM(e.baseSalary) > 100000");
        System.out.println("   \"\"\")");
    }

    // =========================================================================
    // 7. Subqueries & DTO Projections
    // =========================================================================
    private void explainSubqueriesAndProjections() {
        System.out.println("\n--- [7] Complex SQL: Subqueries & DTO Projections ---");
        System.out.println("📌 Subquery: Find employees whose salary is above company average:");
        System.out.println("   SELECT * FROM employees");
        System.out.println("   WHERE base_salary > (SELECT AVG(base_salary) FROM employees);\n");

        System.out.println("📌 DTO Constructor Projection in JPQL (Fetch only needed columns into a POJO):");
        System.out.println("   @Query(\"\"\"");
        System.out.println("       SELECT new com.rps.finwallet.dto.EmployeeSummaryDto(");
        System.out.println("           e.id, e.name, d.name, e.baseSalary");
        System.out.println("       )");
        System.out.println("       FROM Employee e JOIN e.department d");
        System.out.println("   \"\"\")");
        System.out.println("   List<EmployeeSummaryDto> fetchCustomSummaries();");
    }
}
