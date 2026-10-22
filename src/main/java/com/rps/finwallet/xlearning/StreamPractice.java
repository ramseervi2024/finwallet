package com.rps.finwallet.xlearning;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamPractice implements PracticeRunner {

    @Override
    public String getTitle() {
        return "Java Streams API (Functional style, filter, map, collect, reduce)";
    }

    @Override
    public void run() {
        demonstrateFilterAndMap();
        demonstrateAggregation();
        demonstrateGrouping();
    }

    private void demonstrateFilterAndMap() {
        System.out.println("\n--- [1] Streams: Filter & Map ---");
        List<String> names = List.of("ramesh", "amit", "rahul", "priya", "rohit");

        // Filter names starting with 'r' and convert to UPPERCASE
        List<String> rNamesUpper = names.stream()
                .filter(name -> name.startsWith("r"))
                .map(String::toUpperCase)
                .toList();

        System.out.println("Original Names: " + names);
        System.out.println("Filtered ('r') & Uppercase: " + rNamesUpper);
    }

    private void demonstrateAggregation() {
        System.out.println("\n--- [2] Streams: Sum, Min, Max via Reduce ---");
        List<Integer> transactions = List.of(500, 1200, 300, 4500, 150);

        int total = transactions.stream().reduce(0, Integer::sum);
        int max = transactions.stream().max(Integer::compareTo).orElse(0);
        int min = transactions.stream().min(Integer::compareTo).orElse(0);

        System.out.println("Transactions: " + transactions);
        System.out.println("Total = ₹" + total + " | Max = ₹" + max + " | Min = ₹" + min);
    }

    private void demonstrateGrouping() {
        System.out.println("\n--- [3] Streams: Collectors.groupingBy ---");
        List<String> cities = List.of("Mumbai", "Delhi", "Mumbai", "Pune", "Delhi", "Bangalore");

        Map<String, Long> cityCount = cities.stream()
                .collect(Collectors.groupingBy(city -> city, Collectors.counting()));

        System.out.println("City Count Grouping: " + cityCount);
    }
}
