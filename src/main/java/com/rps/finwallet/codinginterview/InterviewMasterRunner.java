package com.rps.finwallet.codinginterview;

/**
 * ============================================================================
 * 🎓 ULTIMATE JAVA CODING INTERVIEW COMPENDIUM (52 TOP QUESTIONS)
 * ============================================================================
 * Run this file standalone using:
 * java -cp target/classes com.rps.finwallet.codinginterview.InterviewMasterRunner
 *
 * Modules Executed:
 * 1. Arrays (Top 10 Questions: Two Sum, Kadane's, Rotate, Most Water, etc.)
 * 2. Strings (Top 8 Questions: Reverse, Anagrams, Group Anagrams, Sliding Window, etc.)
 * 3. Java 8 Streams (Top 10 Questions: Frequency, Duplicates, GroupBy, FlatMap, etc.)
 * 4. Linked Lists (Top 5 Questions: Reverse, Cycle, Middle, Merge, Remove N-th)
 * 5. Stack & Queue (Top 5 Questions: Parentheses, MinStack, Monotonic, 2-Stacks Queue)
 * 6. Binary Tree & BST (Top 5 Questions: Max Depth, Invert, LevelOrder, Validate BST, LCA)
 * 7. Algorithms & DP (Top 5 Questions: Binary Search, Rotated, Stairs, Coin Change, Intervals)
 * 8. Concurrency & Patterns (Top 4 Questions: Singleton, LRU Cache, Producer-Consumer, Deadlock)
 * ============================================================================
 */
public class InterviewMasterRunner {

    public static void main(String[] args) {
        runAllInterviewQuestions();
    }

    public static void runAllInterviewQuestions() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║        🚀 THE ULTIMATE JAVA FULL STACK CODING INTERVIEW COMPENDIUM           ║");
        System.out.println("║         (52 Top Coding Questions with Inputs, Outputs & Hinglish Explanations)║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");

        long start = System.currentTimeMillis();

        // 1. Array Questions (Top 10)
        new ArrayInterviewQuestions().runAll();

        // 2. String Questions (Top 8)
        new StringInterviewQuestions().runAll();

        // 3. Java 8 Streams Questions (Top 10)
        new StreamInterviewQuestions().runAll();

        // 4. Linked List Questions (Top 5)
        new LinkedListInterviewQuestions().runAll();

        // 5. Stack & Queue Questions (Top 5)
        new StackQueueInterviewQuestions().runAll();

        // 6. Binary Tree & BST Questions (Top 5)
        new BinaryTreeInterviewQuestions().runAll();

        // 7. Algorithms & DP Questions (Top 5)
        new AlgorithmAndDpInterviewQuestions().runAll();

        // 8. Design Patterns & Concurrency (Top 4)
        new DesignPatternAndConcurrencyQuestions().runAll();

        long duration = System.currentTimeMillis() - start;

        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║          ✅ ALL 52 CODING INTERVIEW QUESTIONS EXECUTED IN " + duration + " MS!          ║");
        System.out.println("║       (Every single category is now 100% covered with Zero doubts left)      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");
    }
}
