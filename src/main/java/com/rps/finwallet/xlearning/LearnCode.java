package com.rps.finwallet.xlearning;

import java.util.List;

/**
 * Common Manager Class to execute all learning practice modules.
 */
public class LearnCode {

    public static void main(String[] args) {
        runAll();
    }

    /**
     * Runs all practice modules sequentially with formatted logs.
     */
    public static void runAll() {
        List<PracticeRunner> practices = List.of(
                new ArrayListPractice(),
                new HashSetPractice(),
                new HashMapPractice(),
                new StreamPractice(),
                new StringPractice(),
                new JpaSqlPractice(),
                new JpaMasterGuide(),
                new JpaRelationshipGuide(),
                new AopPractice(),
                new MvcPractice()
        );

        System.out.println("\n=======================================================");
        System.out.println("🚀 [XLEARNING] JAVA COLLECTIONS & PRACTICE STARTED");
        System.out.println("=======================================================");

        for (PracticeRunner runner : practices) {
            System.out.println("\n*******************************************************");
            System.out.println("👉 RUNNING: " + runner.getTitle());
            System.out.println("*******************************************************");
            runner.run();
        }

        System.out.println("\n=======================================================");
        System.out.println("✅ [XLEARNING] ALL PRACTICES COMPLETED SUCCESSFULLY");
        System.out.println("=======================================================\n");
    }

    /**
     * Run a single specific practice module.
     */
    public static void runSpecific(PracticeRunner runner) {
        System.out.println("\n*******************************************************");
        System.out.println("👉 RUNNING: " + runner.getTitle());
        System.out.println("*******************************************************");
        runner.run();
    }
}
