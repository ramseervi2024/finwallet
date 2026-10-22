package com.rps.finwallet.xlearning;

/**
 * ============================================================================
 * 🎓 JPA & HIBERNATE COMPLETE MASTER GUIDE (ZERO DOUBT COMPENDIUM)
 * ============================================================================
 * Is class me JPA / Hibernate ke saare fundamental aur advanced concepts
 * crystal-clear Hinglish me detail ke saath console me demonstrate kiye gaye hain.
 */
public class JpaMasterGuide implements PracticeRunner {

    @Override
    public String getTitle() {
        return "JPA & Hibernate Master Guide (Lifecycle, Cache, Locking, Joins, Projections)";
    }

    @Override
    public void run() {
        printHeader();
        explainEntityLifecycleStates();
        explainEntityManagerMethods();
        explainCascadeAndOrphanRemoval();
        explainFetchTypeAndLazyException();
        explainOptimisticVsPessimisticLocking();
        explainFirstAndSecondLevelCache();
        explainJpqlVsNativeVsCriteria();
        printFooter();
    }

    private void printHeader() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║          MASTER JPA & HIBERNATE ARCHITECTURE & LOGIC (ZERO DOUBTS)           ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
    }

    // =========================================================================
    // 1. Entity Lifecycle & 4 States
    // =========================================================================
    private void explainEntityLifecycleStates() {
        System.out.println("\n📌 [PART 1] JPA ENTITY LIFECYCLE (4 STATES)");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("1. TRANSIENT (New):");
        System.out.println("   - 'Employee emp = new Employee();'");
        System.out.println("   - Ye object sirf Java heap memory me hai. DB me koi row nahi hai, koi ID nahi hai.");
        System.out.println("   - Hibernate isko track nahi kar raha.\n");

        System.out.println("2. MANAGED (Persistent):");
        System.out.println("   - 'entityManager.persist(emp);' ya 'repo.findById(1L);'");
        System.out.println("   - Object Hibernate ke Persistence Context (Session) ke andar hai.");
        System.out.println("   - 🌟 DIRTY CHECKING MAGIC: Agar aap 'emp.setName(\"Rohan\");' set karoge,");
        System.out.println("     to aapko explicit repo.save() call karne ki zaroorat nahi hai!");
        System.out.println("     Transaction commit hone par Hibernate khud UPDATE SQL query chala dega.\n");

        System.out.println("3. DETACHED:");
        System.out.println("   - 'entityManager.detach(emp);' ya 'entityManager.clear();'");
        System.out.println("   - DB me row exist karti hai, ID bhi hai, lekin ye Persistence Context se bahar nikal chuka hai.");
        System.out.println("   - Isme changes karne par DB me auto-update nahi hoga jab tak merge() na kiya jaye.\n");

        System.out.println("4. REMOVED:");
        System.out.println("   - 'entityManager.remove(emp);' ya 'repo.delete(emp);'");
        System.out.println("   - Object DB se delete hone ke liye mark ho chuka hai. Transaction commit hote hi DELETE SQL chalega.");
    }

    // =========================================================================
    // 2. Core EntityManager Methods
    // =========================================================================
    private void explainEntityManagerMethods() {
        System.out.println("\n📌 [PART 2] CORE ENTITYMANAGER METHODS & LOGIC");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("┌───────────────┬──────────────────────────────────────────────────────────────┐");
        System.out.println("│ Method        │ What it does (Kab use karein)                                │");
        System.out.println("├───────────────┼──────────────────────────────────────────────────────────────┤");
        System.out.println("│ persist()     │ Transient object ko Managed state me laata hai (INSERT SQL). │");
        System.out.println("│ merge()       │ Detached object ki copies ko Managed banata hai (UPDATE/INS).│");
        System.out.println("│ remove()      │ Managed entity ko delete karne ke liye mark karta hai.        │");
        System.out.println("│ flush()       │ Sare pending queries turant DB par push karta hai (No commit)│");
        System.out.println("│ refresh()     │ DB se fresh data lake entity ko overwrite karta hai.         │");
        System.out.println("│ clear()       │ Persistence Context ke sabhi objects ko Detach kar deta hai. │");
        System.out.println("│ detach()      │ Kisi specific entity ko Persistence Context se bahar nikalta │");
        System.out.println("└───────────────┴──────────────────────────────────────────────────────────────┘");
    }

    // =========================================================================
    // 3. Cascade Types & Orphan Removal
    // =========================================================================
    private void explainCascadeAndOrphanRemoval() {
        System.out.println("\n📌 [PART 3] CASCADE TYPES & ORPHAN REMOVAL");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("CascadeType ka matlab: 'Parent par jo action hoga, wahi Child par propagate hoga.'");
        System.out.println(" - CascadeType.PERSIST : Parent save hua -> Child bhi auto-save hoga.");
        System.out.println(" - CascadeType.REMOVE  : Parent delete hua -> Child bhi delete hoga.");
        System.out.println(" - CascadeType.ALL     : Persist, Merge, Remove, Refresh sab kuch propagate hoga.\n");

        System.out.println("🔥 Difference: CascadeType.REMOVE vs orphanRemoval = true");
        System.out.println(" - CascadeType.REMOVE sirf tab chalta hai jab aap POORA PARENT delete karte ho.");
        System.out.println(" - orphanRemoval = true tab bhi chalta hai jab aap parent ki list se child ko remove karte ho!");
        System.out.println("   Example: department.getEmployees().remove(0);");
        System.out.println("   -> Agar orphanRemoval=true hai, to wo child DB se DELETE ho jayega!");
    }

    // =========================================================================
    // 4. FetchType & LazyInitializationException
    // =========================================================================
    private void explainFetchTypeAndLazyException() {
        System.out.println("\n📌 [PART 4] FETCHTYPE (LAZY vs EAGER) & LAZYINITIALIZATIONEXCEPTION");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("FetchType.EAGER: Entity load hote hi related child ko turant load kar leta hai.");
        System.out.println(" - Khatra: Unnecessary JOINs aur memory waste hoti hai.\n");

        System.out.println("FetchType.LAZY: Child ko sirf tab load karta hai jab aap 'getDepartment()' call karte ho.");
        System.out.println(" - Best Practice: Default hamesha LAZY use karna chahiye.\n");

        System.out.println("💣 Why LazyInitializationException happens?");
        System.out.println(" - Jab transaction khatam ho jati hai (@Transactional method return ho gaya) aur Session CLOSE ho gaya,");
        System.out.println(" - Fir Controller ya Thymeleaf view me aap 'emp.getDepartment().getName()' call karte ho,");
        System.out.println(" - Tab Hibernate DB se data lane ke liye koi open session nahi pata -> EXCEPTION throws!");
        System.out.println(" - 💡 Solution: JPQL 'JOIN FETCH' use karo ya DTO Projection me data pehle hi fetch kar lo.");
    }

    // =========================================================================
    // 5. Optimistic vs Pessimistic Locking
    // =========================================================================
    private void explainOptimisticVsPessimisticLocking() {
        System.out.println("\n📌 [PART 5] CONCURRENCY & LOCKING (WALLET / FINANCE APPS)");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Scenario: Do users ek sath ₹1000 balance wale wallet se ₹800 withdraw karne ki koshish karein.");
        System.out.println("Bina locking ke: Race Condition -> Balance minus me chala jayega (Double Spending)!\n");

        System.out.println("1. OPTIMISTIC LOCKING (@Version):");
        System.out.println("   - Har row me ek 'version' number hota hai (e.g. 1).");
        System.out.println("   - User A update commit karta hai -> version ban jata hai 2.");
        System.out.println("   - User B update karne aata hai with version 1 -> Hibernate throws OptimisticLockException!");
        System.out.println("   - Best for: Read-heavy applications jahan collisions kam hote hain.\n");

        System.out.println("2. PESSIMISTIC LOCKING (@Lock(LockModeType.PESSIMISTIC_WRITE)):");
        System.out.println("   - Database row level lock lagata hai: 'SELECT ... FOR UPDATE'.");
        System.out.println("   - Jab tak User A ki transaction khatam nahi hoti, User B wait (block) karega.");
        System.out.println("   - Best for: FinWallet jaise critical banking transfers aur financial disbursements.");
    }

    // =========================================================================
    // 6. First-Level vs Second-Level Cache
    // =========================================================================
    private void explainFirstAndSecondLevelCache() {
        System.out.println("\n📌 [PART 6] HIBERNATE CACHING ARCHITECTURE");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("1. First-Level Cache (Session Cache):");
        System.out.println("   - Scope: Ek hi Transaction / EntityManager ke andar hota hai.");
        System.out.println("   - By default always ON rehta hai.");
        System.out.println("   - Agar aap ek hi method me do baar 'repo.findById(10L)' call karoge, to SQL sirf 1 baar chalega!");
        System.out.println("   - Dusri baar data first-level cache se uth kar aayega.\n");

        System.out.println("2. Second-Level Cache (Process / Cluster Cache):");
        System.out.println("   - Scope: Entire Application (Sari transactions ke beech share hota hai).");
        System.out.println("   - Redis ya Ehcache ke through configure hota hai.");
        System.out.println("   - Master data (jaise Country list, State list, Currency codes) ke liye perfect hai.");
    }

    // =========================================================================
    // 7. When to use What: JPQL vs Native SQL vs Criteria API
    // =========================================================================
    private void explainJpqlVsNativeVsCriteria() {
        System.out.println("\n📌 [PART 7] WHEN TO USE: JPQL vs NATIVE SQL vs CRITERIA API");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("1. Use JPQL (Default choice):");
        System.out.println("   - Entity classes aur fields par operate karta hai. Database-independent hota hai.");
        System.out.println("   - Example: 'SELECT e FROM Employee e WHERE e.department.name = :dept'");

        System.out.println("2. Use Native SQL (Only when needed):");
        System.out.println("   - Jab DB-specific features chahiye (jaise MySQL CTE, Window functions DENSE_RANK, bulk copy).");
        System.out.println("   - Example: nativeQuery = true");

        System.out.println("3. Use Criteria API / Specifications:");
        System.out.println("   - Jab runtime par dynamic filters chahiye (jaise E-commerce search filters:");
        System.out.println("     user price select kare to price filter lage, brand select kare to brand filter lage).");
    }

    private void printFooter() {
        System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
        System.out.println("🎯 INSTRUCTION: In sabhi methods ka real working implementation dekhne ke liye:");
        System.out.println("   - Check Repository : JpaComplexQueryRepository.java");
        System.out.println("   - Check Service    : JpaComplexQueryService.java");
        System.out.println("   - Check DTO        : EmployeeWalletDeptDto.java & EmployeeNameDeptProjection.java");
        System.out.println("════════════════════════════════════════════════════════════════════════════════\n");
    }
}
