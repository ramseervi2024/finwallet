package com.rps.finwallet.xlearning;

import com.rps.finwallet.model.Department;
import com.rps.finwallet.model.Employee;
import com.rps.finwallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * 🎯 PURE JPA & JPQL REPOSITORY (ZERO NATIVE SQL QUERIES)
 * ============================================================================
 * Is file me saari 4 relationships (@OneToOne, @ManyToOne, @OneToMany, @ManyToMany)
 * ko sirf aur sirf PURE JPQL aur JPA annotations ke zariye query kiya gaya hai.
 * Yahan koi bhi "nativeQuery = true" use nahi kiya gaya hai!
 * ============================================================================
 */
@Repository
public interface JpaPureRelationshipRepository extends JpaRepository<Employee, Long> {

    // =========================================================================
    // 1. @OneToOne QUERIES (Pure JPQL Navigation & Fetching)
    // =========================================================================

    // Hinglish 1A: Dot notation (.) se 1-to-1 relationship navigate karna
    // JPQL automatically inner join banata hai!
    @Query("SELECT w FROM Wallet w WHERE w.employee.email = :email")
    Optional<Wallet> findWalletByEmployeeEmail(@Param("email") String email);

    // Hinglish 1B: @OneToOne JOIN FETCH - Wallet aur Employee dono 1 query me load hote hain
    @Query("SELECT w FROM Wallet w JOIN FETCH w.employee e WHERE w.balance >= :minBalance")
    List<Wallet> findWalletsWithEmployeeEagerly(@Param("minBalance") BigDecimal minBalance);

    // =========================================================================
    // 2. @ManyToOne QUERIES (Child entity se Parent ko query karna)
    // =========================================================================

    // Hinglish 2A: Direct Parent field ke through filter karna
    @Query("SELECT e FROM Employee e WHERE e.department.name = :departmentName")
    List<Employee> findEmployeesByDepartmentName(@Param("departmentName") String departmentName);

    // Hinglish 2B: Multiple conditions using JPA dot path
    @Query("SELECT e FROM Employee e WHERE e.department.location = :location AND e.baseSalary > :salary")
    List<Employee> findHighEarnersByDeptLocation(@Param("location") String location, @Param("salary") BigDecimal salary);

    // =========================================================================
    // 3. @OneToMany QUERIES (Parent entity se Child Collection ko query karna)
    // =========================================================================

    // Hinglish 3A: PURE JPA Collection function: SIZE()
    // Department jisme 5 se zyada employees hain:
    @Query("SELECT d FROM Department d WHERE SIZE(d.employees) >= :minCount")
    List<Department> findDepartmentsHavingMinEmployees(@Param("minCount") int minCount);

    // Hinglish 3B: PURE JPA Collection condition: IS EMPTY
    // Wo departments jinke paas ek bhi employee nahi hai:
    @Query("SELECT d FROM Department d WHERE d.employees IS EMPTY")
    List<Department> findEmptyDepartments();

    // Hinglish 3C: PURE JPA Collection condition: IS NOT EMPTY
    @Query("SELECT d FROM Department d WHERE d.employees IS NOT EMPTY")
    List<Department> findNonEmptyDepartments();

    // Hinglish 3D: @OneToMany JOIN FETCH (Loads Department + all Employees in 1 SQL query)
    @Query("SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.employees WHERE d.location = :location")
    List<Department> findDepartmentsWithEmployeesByLocation(@Param("location") String location);

    // =========================================================================
    // 4. @ManyToMany QUERIES (Developer <-> Project)
    // =========================================================================

    // Hinglish 4A: Developer ke email se uske saare Projects dhoondhna
    // Notice: JPQL me Developer aur Project entity ko direct JOIN kiya jata hai,
    // junction table ka naam lene ki koi zaroorat nahi hoti!
    @Query("SELECT p FROM Project p JOIN p.developers d WHERE d.email = :developerEmail")
    List<Project> findProjectsAssignedToDeveloper(@Param("developerEmail") String developerEmail);

    // Hinglish 4B: Project title se uspar kaam karne wale saare Developers nikaalna
    @Query("SELECT d FROM Developer d JOIN d.projects p WHERE p.title = :projectTitle")
    List<Developer> findDevelopersWorkingOnProject(@Param("projectTitle") String projectTitle);

    // Hinglish 4C: PURE JPA Operator: MEMBER OF (Collection membership check)
    // Kya ye specific Project is Developer ke project set me shamil hai?
    @Query("SELECT d FROM Developer d WHERE :project MEMBER OF d.projects")
    List<Developer> findDevelopersHavingProject(@Param("project") Project project);

    // Hinglish 4D: PURE JPA @ManyToMany JOIN FETCH (Developer + Projects eager loading)
    @Query("SELECT DISTINCT d FROM Developer d LEFT JOIN FETCH d.projects WHERE d.id = :developerId")
    Optional<Developer> findDeveloperWithProjectsEagerly(@Param("developerId") Long developerId);
}
