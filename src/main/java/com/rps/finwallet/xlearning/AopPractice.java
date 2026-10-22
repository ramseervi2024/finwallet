package com.rps.finwallet.xlearning;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * ============================================================================
 * 🛡️ EXECUTABLE SPRING AOP ENGINE & STEP-BY-STEP LOGIC GUIDE
 * ============================================================================
 * Is class me Spring AOP ka internal working logic line-by-line detailed
 * Hinglish comments ke sath explain kiya gaya hai.
 *
 * AOP KA MAIN FUNDA:
 * 1. Caller (User/Controller) kabhi direct Target Service ko call nahi karta.
 * 2. Caller hamesha Spring ke banaye PROXY WRAPPER ko call karta hai.
 * 3. Proxy method call ko intercept karta hai:
 * -> Pehle @Before aur @Around(Part 1) chalata hai.
 * -> Fir real target method ko proceed() karta hai.
 * -> Success par @AfterReturning chalata hai.
 * -> Error par @AfterThrowing chalata hai.
 * -> Finally @After aur @Around(Part 2) chalata hai.
 * ============================================================================
 */
public class AopPractice implements PracticeRunner {

    @Override
    public String getTitle() {
        return "Spring AOP Live Executable Code (Working Proxy, Advices & Real Interception)";
    }

    // =========================================================================
    // STEP 1: BUSINESS SERVICE INTERFACE (Contract)
    // =========================================================================
    // Hinglish: Spring AOP do tarah ke proxies banata hai:
    // 1. JDK Dynamic Proxy: Agar class kisi interface ko implement karti hai
    // (Default).
    // 2. CGLIB Proxy: Agar class koi interface implement nahi karti (subclassing
    // method).
    public interface SimpleWalletService {
        double deposit(String accountNumber, double amount);

        double withdraw(String accountNumber, double amount) throws Exception;
    }

    // =========================================================================
    // STEP 2: TARGET OBJECT (Pure Business Logic - No Logging/Audit Pollution)
    // =========================================================================
    // Hinglish: Dekho is class me koi logging, timing, security ya audit code nahi
    // hai.
    // Business logic 100% clean hai! Sab cross-cutting logic Aspect sambhalega.
    public static class SimpleWalletServiceImpl implements SimpleWalletService {
        private double balance = 1000.0;

        @Override
        public double deposit(String accountNumber, double amount) {
            // Sirf core business calculation
            System.out
                    .println("   [TARGET EXECUTION] Adding ₹" + amount + " to DB balance for account " + accountNumber);
            this.balance += amount;
            return this.balance;
        }

        @Override
        public double withdraw(String accountNumber, double amount) throws Exception {
            // Sirf balance validation aur deduction
            System.out.println(
                    "   [TARGET EXECUTION] Attempting to deduct ₹" + amount + " from account " + accountNumber);
            if (amount > this.balance) {
                throw new IllegalArgumentException(
                        "Insufficient Balance! Current: ₹" + balance + ", Requested: ₹" + amount);
            }
            this.balance -= amount;
            return this.balance;
        }
    }

    // =========================================================================
    // STEP 3: AOP PROXY & INVOCATION HANDLER (Aspect Logic + Interception)
    // =========================================================================
    // Hinglish: Ye class Spring ke '@Aspect' aur 'Weaving Engine' ka role play
    // karti hai.
    // Jab bhi proxyService ka koi method call hoga, control pehle is 'invoke()'
    // method me aayega!
    public static class AopInvocationHandler implements InvocationHandler {

        // Target: Actual business object jisko wrap kiya gaya hai
        // (SimpleWalletServiceImpl)
        private final Object target;

        public AopInvocationHandler(Object target) {
            this.target = target;
        }

        /**
         * 🌟 THE HEART OF AOP: invoke() method
         *
         * @param proxy  : Spring dwara generate kiya gaya dynamic proxy object
         * @param method : JoinPoint (kaunsa method call hua, e.g. deposit ya withdraw)
         * @param args   : Method arguments (e.g. ["ACC-101", 500.0])
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            String argsStr = Arrays.toString(args);

            // -----------------------------------------------------------------
            // 🌟 1. @Before ADVICE LOGIC
            // -----------------------------------------------------------------
            // Hinglish: Target method execute hone se THEEK PEHLE chalta hai.
            // Best use: Authentication check, Security validation, Parameter audit.
            beforeAdvice(methodName, argsStr);

            // -----------------------------------------------------------------
            // 🌟 2. @Around ADVICE (PART 1: START TIMER)
            // -----------------------------------------------------------------
            // Hinglish: Method ko sandwich ki tarah gher kar timing calculate karna.
            long startTime = System.currentTimeMillis();
            Object result;

            try {
                // Realistic processing delay simulate kar rahe hain (50 ms)
                Thread.sleep(50);

                // -------------------------------------------------------------
                // 🌟 3. PROCEED / JOINPOINT EXECUTION
                // -------------------------------------------------------------
                // Hinglish: YEH LINE ACTUAL BUSINESS METHOD KO CHALATI HAI!
                // Spring AOP me ye 'proceedingJoinPoint.proceed()' ke barabar hai.
                result = method.invoke(target, args);

                // -------------------------------------------------------------
                // 🌟 4. @AfterReturning ADVICE LOGIC
                // -------------------------------------------------------------
                // Hinglish: Ye code SIRF TAB chalega jab method successfully execute ho jaye
                // (koi exception na aaye). Yahan hum return value (result) ko inspect kar sakte
                // hain.
                afterReturningAdvice(methodName, result);

                return result;

            } catch (Throwable throwable) {
                // Java reflection InvocationTargetException wrap karta hai, isliye actual
                // exception nikaalte hain
                Throwable actualCause = throwable.getCause() != null ? throwable.getCause() : throwable;

                // -------------------------------------------------------------
                // 🌟 5. @AfterThrowing ADVICE LOGIC
                // -------------------------------------------------------------
                // Hinglish: Ye code SIRF TAB chalega jab target method koi Exception fekta hai!
                // Best use: Error notification bhejna, security alert raise karna, rollback
                // logging.
                afterThrowingAdvice(methodName, actualCause);

                // Re-throw the exception taaki caller ko pata chale ki method fail hua
                throw actualCause;

            } finally {
                // -------------------------------------------------------------
                // 🌟 6. @After (FINALLY) & @Around (PART 2) LOGIC
                // -------------------------------------------------------------
                // Hinglish: Java ke finally block ki tarah — chahe SUCCESS ho ya EXCEPTION
                // aaye,
                // ye advice HAMESHA chalegi!
                // Best use: Resources cleanup, total elapsed time calculation.
                long duration = System.currentTimeMillis() - startTime;
                afterFinallyAdvice(methodName, duration);
            }
        }

        // --- ADVICE METHODS (Equivalent to methods inside an @Aspect class) ---

        private void beforeAdvice(String method, String args) {
            System.out.println("⚡ [@Before ADVICE] Security & Audit: Intercepted " + method + "() with Args: " + args);
        }

        private void afterReturningAdvice(String method, Object result) {
            System.out.println(
                    "✅ [@AfterReturning ADVICE] Success: " + method + "() completed. Return Value = ₹" + result);
        }

        private void afterThrowingAdvice(String method, Throwable error) {
            System.out.println(
                    "🚨 [@AfterThrowing ADVICE] Alert: " + method + "() failed with Error: " + error.getMessage());
        }

        private void afterFinallyAdvice(String method, long duration) {
            System.out.println(
                    "⏱️ [@After / @Around ADVICE] Finished " + method + "() execution in " + duration + " ms.");
        }
    }

    // =========================================================================
    // STEP 4: SPRING PROXY FACTORY (Weaving Simulation)
    // =========================================================================
    // Hinglish: Spring Boot startup par exactly yahi kaam karta hai:
    // Real Bean leta hai, use InvocationHandler me wrap karta hai, aur Proxy Object
    // return karta hai.
    @SuppressWarnings("unchecked")
    public static <T> T createAopProxy(T target, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[] { interfaceType },
                new AopInvocationHandler(target));
    }

    // =========================================================================
    // STEP 5: LIVE EXECUTION & SCENARIO TESTING
    // =========================================================================
    @Override
    public void run() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║            LIVE EXECUTABLE SPRING AOP PROXY DEMONSTRATION                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");

        // 1. Real target object banaya
        SimpleWalletService realService = new SimpleWalletServiceImpl();

        // 2. Spring AOP Proxy banaya (Caller will talk to this proxy!)
        SimpleWalletService proxyService = createAopProxy(realService, SimpleWalletService.class);

        // ---------------------------------------------------------------------
        // SCENARIO 1: Success Path
        // Expected Flow: @Before -> Target Method -> @AfterReturning -> @After (@Around
        // time)
        // ---------------------------------------------------------------------
        System.out.println("\n--- [SCENARIO 1] SUCCESSFUL TRANSACTION (Deposit ₹500) ---");
        System.out.println("Calling: proxyService.deposit(\"ACC-101\", 500.0);");
        System.out.println("Watch the live AOP lifecycle hooks fire around the method:\n");
        proxyService.deposit("ACC-101", 500.0);

        // ---------------------------------------------------------------------
        // SCENARIO 2: Failure Path (Exception thrown)
        // Expected Flow: @Before -> Target Method -> @AfterThrowing -> @After (@Around
        // time)
        // ---------------------------------------------------------------------
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("--- [SCENARIO 2] EXCEPTION HANDLING (Withdraw ₹5000 with only ₹1500 balance) ---");
        System.out.println("Calling: proxyService.withdraw(\"ACC-101\", 5000.0);");
        System.out.println("Watch @AfterThrowing intercept and handle the failure:\n");
        try {
            proxyService.withdraw("ACC-101", 5000.0);
        } catch (Exception e) {
            System.out.println("   [CALLER CAUGHT]: " + e.getMessage());
        }

        System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
        System.out.println("📂 YOUR REAL SPRING BOOT PRODUCTION ASPECTS IN THIS CODEBASE:");
        System.out.println("   1. BankingAuditAspect.java         -> @Before, @AfterReturning, @AfterThrowing");
        System.out.println(
                "   2. PerformanceAspect.java          -> @Around (Measures execution time of @LogExecutionTime)");
        System.out.println("   3. HighValueTransactionAspect.java -> AML Security check for transactions >= ₹50,000");
        System.out.println("════════════════════════════════════════════════════════════════════════════════\n");
    }
}
