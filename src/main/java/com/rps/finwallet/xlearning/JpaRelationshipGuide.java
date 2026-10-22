package com.rps.finwallet.xlearning;

/**
 * ============================================================================
 * 🤝 JPA RELATIONSHIP MAPPINGS GUIDE (PURE JPA / ZERO NATIVE QUERY)
 * ============================================================================
 * Is class me @OneToOne, @ManyToOne, @OneToMany, aur @ManyToMany ke
 * core annotations, owning sides, aur pure JPQL queries ko
 * console me deeply visually explain kiya gaya hai.
 */
public class JpaRelationshipGuide implements PracticeRunner {

    @Override
    public String getTitle() {
        return "JPA Relationships Deep Dive (@OneToOne, @ManyToOne, @OneToMany, @ManyToMany)";
    }

    @Override
    public void run() {
        explainRelationshipMatrix();
        explainOneToOne();
        explainManyToOneAndOneToMany();
        explainManyToMany();
        explainPureJpqlCollectionFunctions();
    }

    private void explainRelationshipMatrix() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║          JPA RELATIONSHIPS MATRIX: OWNING SIDE vs INVERSE SIDE (ZERO NATIVE SQL)             ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("┌─────────────┬──────────────────────────┬──────────────────────────┬──────────────────────────┐");
        System.out.println("│ Type        │ Owning Side (FK Holder)  │ Inverse Side (mappedBy)  │ DB Representation        │");
        System.out.println("├─────────────┼──────────────────────────┼──────────────────────────┼──────────────────────────┤");
        System.out.println("│ @OneToOne   │ @JoinColumn(name=\"...\")  │ mappedBy = \"...\"         │ Unique Foreign Key (FK)  │");
        System.out.println("│ @ManyToOne  │ Always Owning Side (FK)  │ N/A (or @OneToMany)      │ Foreign Key in Child row │");
        System.out.println("│ @OneToMany  │ N/A (Unless unidirectional│ mappedBy = \"...\"         │ Referenced by Child FK   │");
        System.out.println("│ @ManyToMany │ @JoinTable(...)          │ mappedBy = \"...\"         │ 3rd Junction Table (A_B) │");
        System.out.println("└─────────────┴──────────────────────────┴──────────────────────────┴──────────────────────────┘");
    }

    private void explainOneToOne() {
        System.out.println("\n📌 [1] @OneToOne RELATIONSHIP (Example: Wallet <──> Employee)");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("🔍 Code Pattern:");
        System.out.println("   // In Wallet.java (Owning Side):");
        System.out.println("   @OneToOne(fetch = FetchType.LAZY)");
        System.out.println("   @JoinColumn(name = \"employee_id\", unique = true, nullable = false)");
        System.out.println("   private Employee employee;");
        System.out.println();
        System.out.println("   // In Employee.java (Inverse Side - Optional):");
        System.out.println("   @OneToOne(mappedBy = \"employee\")");
        System.out.println("   private Wallet wallet;\n");
        System.out.println("💡 Pure JPQL Query (No Native SQL needed):");
        System.out.println("   @Query(\"SELECT w FROM Wallet w WHERE w.employee.email = :email\")");
        System.out.println("   Optional<Wallet> findWalletByEmployeeEmail(String email);");
    }

    private void explainManyToOneAndOneToMany() {
        System.out.println("\n📌 [2] @ManyToOne & @OneToMany (Example: Employee <──> Department)");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("🔍 Code Pattern:");
        System.out.println("   // In Employee.java (Many Employees belong to 1 Department - OWNING SIDE):");
        System.out.println("   @ManyToOne(fetch = FetchType.LAZY)");
        System.out.println("   @JoinColumn(name = \"department_id\")");
        System.out.println("   private Department department;");
        System.out.println();
        System.out.println("   // In Department.java (1 Department has Many Employees - INVERSE SIDE):");
        System.out.println("   @OneToMany(mappedBy = \"department\", cascade = CascadeType.ALL, orphanRemoval = true)");
        System.out.println("   private List<Employee> employees = new ArrayList<>();\n");
        System.out.println("💡 Pure JPQL Queries:");
        System.out.println("   1. Dot Navigation: @Query(\"SELECT e FROM Employee e WHERE e.department.name = :name\")");
        System.out.println("   2. Eager Loading : @Query(\"SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.employees\")");
    }

    private void explainManyToMany() {
        System.out.println("\n📌 [3] @ManyToMany (Example: Developer <───> Project)");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("🔍 Code Pattern:");
        System.out.println("   // In Developer.java (OWNING SIDE - Declares @JoinTable):");
        System.out.println("   @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})");
        System.out.println("   @JoinTable(");
        System.out.println("       name = \"learning_developer_projects\",");
        System.out.println("       joinColumns = @JoinColumn(name = \"developer_id\"),");
        System.out.println("       inverseJoinColumns = @JoinColumn(name = \"project_id\")");
        System.out.println("   )");
        System.out.println("   private Set<Project> projects = new HashSet<>();");
        System.out.println();
        System.out.println("   // In Project.java (INVERSE SIDE - Uses mappedBy):");
        System.out.println("   @ManyToMany(mappedBy = \"projects\")");
        System.out.println("   private Set<Developer> developers = new HashSet<>();\n");
        System.out.println("💡 Pure JPQL Query (Zero Native SQL):");
        System.out.println("   // Junction table ko mention karne ki zaroorat nahi hai, direct entity field join karein!");
        System.out.println("   @Query(\"SELECT p FROM Project p JOIN p.developers d WHERE d.email = :email\")");
        System.out.println("   List<Project> findProjectsAssignedToDeveloper(String email);");
    }

    private void explainPureJpqlCollectionFunctions() {
        System.out.println("\n📌 [4] PURE JPA COLLECTION OPERATORS (NO NATIVE SQL REQUIRED)");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("1. SIZE(collection):");
        System.out.println("   @Query(\"SELECT d FROM Department d WHERE SIZE(d.employees) >= :count\")\n");
        System.out.println("2. IS EMPTY / IS NOT EMPTY:");
        System.out.println("   @Query(\"SELECT d FROM Department d WHERE d.employees IS EMPTY\")\n");
        System.out.println("3. MEMBER OF:");
        System.out.println("   @Query(\"SELECT d FROM Developer d WHERE :project MEMBER OF d.projects\")\n");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println("🎯 FILES TO INSPECT:");
        System.out.println("   - Developer Entity : Developer.java (@ManyToMany Owning Side with @JoinTable)");
        System.out.println("   - Project Entity   : Project.java (@ManyToMany Inverse Side with mappedBy)");
        System.out.println("   - Pure Repository  : JpaPureRelationshipRepository.java (100% Pure JPQL)");
        System.out.println("════════════════════════════════════════════════════════════════════════════════\n");
    }
}
