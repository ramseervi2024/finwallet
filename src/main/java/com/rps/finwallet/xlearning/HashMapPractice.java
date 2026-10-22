package com.rps.finwallet.xlearning;

import java.util.HashMap;
import java.util.Map;

public class HashMapPractice implements PracticeRunner {

    @Override
    public String getTitle() {
        return "HashMap (Key-Value pairs, Fast lookup O(1), Unique keys)";
    }

    @Override
    public void run() {
        demonstrateBasicMapOperations();
        demonstrateEntrySetIteration();
        demonstrateFrequencyCounter();
    }

    // 1. Basic put, get, getOrDefault, containsKey
    private void demonstrateBasicMapOperations() {
        System.out.println("\n--- [1] HashMap: Basic Key-Value Operations ---");
        Map<String, Double> accountBalances = new HashMap<>();

        // Adding entries
        accountBalances.put("ACC1001", 15400.50);
        accountBalances.put("ACC1002", 2300.00);
        accountBalances.put("ACC1003", 89000.75);

        System.out.println("All Accounts: " + accountBalances);
        System.out.println("Balance of ACC1002: ₹" + accountBalances.get("ACC1002"));

        // getOrDefault: returns default value if key doesn't exist (prevents NullPointerException)
        Double unknownBalance = accountBalances.getOrDefault("ACC9999", 0.0);
        System.out.println("Balance of ACC9999 (fallback default): ₹" + unknownBalance);

        // Check if key exists
        System.out.println("Has ACC1001? " + accountBalances.containsKey("ACC1001"));
    }

    // 2. Iterate using EntrySet (Clean & Efficient)
    private void demonstrateEntrySetIteration() {
        System.out.println("\n--- [2] HashMap: Iterating with EntrySet ---");
        Map<String, String> userStatus = Map.of(
                "Ramesh", "ACTIVE",
                "Amit", "PENDING",
                "Priya", "ACTIVE"
        );

        for (Map.Entry<String, String> entry : userStatus.entrySet()) {
            System.out.println("User: " + entry.getKey() + " -> Status: " + entry.getValue());
        }
    }

    // 3. Interview Classic: Frequency Count of Words / Characters
    private void demonstrateFrequencyCounter() {
        System.out.println("\n--- [3] HashMap: Word Frequency Counter (Interview Pattern) ---");
        String text = "wallet credit debit wallet debit wallet transfer";
        String[] words = text.split(" ");

        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }

        System.out.println("Original String: \"" + text + "\"");
        System.out.println("Word Frequencies: " + wordCount);
    }
}