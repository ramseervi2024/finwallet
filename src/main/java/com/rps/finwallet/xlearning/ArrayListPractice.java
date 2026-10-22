package com.rps.finwallet.xlearning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ArrayListPractice implements PracticeRunner {

    @Override
    public String getTitle() {
        return "ArrayList (Ordered, Index-based, Allows Duplicates)";
    }

    @Override
    public void run() {
        demonstrateBasicOperations();
        demonstrateIterationMethods();
        demonstrateSortingAndSearch();
        demonstrateSumAndFilter();
    }

    // 1. Basic Add, Update, Remove
    private void demonstrateBasicOperations() {
        System.out.println("\n--- [1] ArrayList: Basic Operations ---");
        List<String> wallets = new ArrayList<>();

        wallets.add("Paytm");
        wallets.add("PhonePe");
        wallets.add("GooglePay");
        wallets.add("PhonePe"); // Duplicates allowed!
        System.out.println("Initial List: " + wallets);

        // Access by Index
        System.out.println("Element at index 1: " + wallets.get(1));

        // Update an element
        wallets.set(0, "Paytm UPI");
        System.out.println("After update at index 0: " + wallets);

        // Remove element
        wallets.remove("PhonePe"); // removes first occurrence
        System.out.println("After removing PhonePe: " + wallets);
    }

    // 2. Different ways to Iterate
    private void demonstrateIterationMethods() {
        System.out.println("\n--- [2] ArrayList: Iteration Ways ---");
        List<Integer> amounts = List.of(100, 250, 500, 1200);

        System.out.print("For-each loop: ");
        for (int amount : amounts) {
            System.out.print(amount + " ");
        }
        System.out.println();

        System.out.print("Stream forEach: ");
        amounts.forEach(val -> System.out.print(val + " "));
        System.out.println();
    }

    // 3. Sorting & Binary Search
    private void demonstrateSortingAndSearch() {
        System.out.println("\n--- [3] ArrayList: Sorting & Binary Search ---");
        List<Integer> scores = new ArrayList<>(List.of(88, 42, 95, 12, 67));
        System.out.println("Before Sorting: " + scores);

        Collections.sort(scores);
        System.out.println("Ascending Sort: " + scores);

        Collections.sort(scores, Collections.reverseOrder());
        System.out.println("Descending Sort: " + scores);
    }

    // 4. Practical: Sum & Filter
    private void demonstrateSumAndFilter() {
        System.out.println("\n--- [4] ArrayList: Calculate Sum & Filter ---");
        List<Integer> numbers = new ArrayList<>(List.of(10, 25, 40, 55, 60));

        int totalSum = 0;
        for (int n : numbers) {
            totalSum += n;
        }
        System.out.println("Numbers: " + numbers + " | Total Sum = " + totalSum);

        // Filter numbers > 30
        List<Integer> filtered = numbers.stream().filter(n -> n > 30).toList();
        System.out.println("Numbers greater than 30: " + filtered);
    }
}