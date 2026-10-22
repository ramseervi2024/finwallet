package com.rps.finwallet.xlearning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HashSetPractice implements PracticeRunner {

    @Override
    public String getTitle() {
        return "HashSet (Unordered, Unique elements, O(1) performance)";
    }

    @Override
    public void run() {
        demonstrateUniqueness();
        demonstrateDuplicateRemoval();
        demonstrateSetOperations();
    }

    // 1. Basic Uniqueness & No order guarantee
    private void demonstrateUniqueness() {
        System.out.println("\n--- [1] HashSet: Uniqueness (No Duplicates) ---");
        Set<String> emailSet = new HashSet<>();

        emailSet.add("user1@finwallet.com");
        emailSet.add("user2@finwallet.com");
        boolean isAddedAgain = emailSet.add("user1@finwallet.com"); // Duplicate attempt

        System.out.println("Emails in set: " + emailSet);
        System.out.println("Adding duplicate returned: " + isAddedAgain); // false
        System.out.println("Contains user2@finwallet.com? " + emailSet.contains("user2@finwallet.com"));
    }

    // 2. Practical Interview Pattern: Remove Duplicates from a List
    private void demonstrateDuplicateRemoval() {
        System.out.println("\n--- [2] HashSet: Remove Duplicates from List ---");
        List<String> userRoles = List.of("USER", "ADMIN", "USER", "MANAGER", "ADMIN");
        System.out.println("Original List with Duplicates: " + userRoles);

        Set<String> uniqueRoles = new HashSet<>(userRoles);
        System.out.println("Unique Roles via HashSet: " + uniqueRoles);
    }

    // 3. Set Math Operations: Union, Intersection, Difference
    private void demonstrateSetOperations() {
        System.out.println("\n--- [3] HashSet: Union & Intersection ---");
        Set<Integer> setA = new HashSet<>(Set.of(1, 2, 3, 4, 5));
        Set<Integer> setB = new HashSet<>(Set.of(4, 5, 6, 7, 8));

        // Intersection (Common elements)
        Set<Integer> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);
        System.out.println("Set A: " + setA + " | Set B: " + setB);
        System.out.println("Common Elements (Intersection): " + intersection);

        // Union (All elements combined)
        Set<Integer> union = new HashSet<>(setA);
        union.addAll(setB);
        System.out.println("Combined Elements (Union): " + union);
    }
}
