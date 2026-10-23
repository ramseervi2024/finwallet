package com.rps.finwallet.codinginterview;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * ⚡ TOP 10 JAVA 8 STREAMS INTERVIEW QUESTIONS (COMPLETE & EXHAUSTIVE)
 * ============================================================================
 * 1. Character Frequency Counting (groupingBy + counting)
 * 2. Find Duplicate Elements from a List using Streams
 * 3. Second Highest Number / Salary using Streams
 * 4. Group Employees by Department & Calculate Average Salary
 * 5. FlatMap: Flattening Nested Collections
 * 6. Numbers Starting With '1' (e.g. [10, 15, 8, 49, 25, 98] -> [10, 15])
 * 7. Partitioning Numbers into Even & Odd (partitioningBy)
 * 8. SummaryStatistics (Min, Max, Avg, Sum in 1 single pass)
 * 9. Multi-field Sorting (Department ASC, then Salary DESC)
 * 10. String Joining with Prefix, Suffix & Delimiter (Collectors.joining)
 * ============================================================================
 */
public class StreamInterviewQuestions {

    public void runAll() {
        System.out.println("\n=======================================================");
        System.out.println("⚡ [INTERVIEW READY] TOP 10 JAVA 8 STREAMS QUESTIONS");
        System.out.println("=======================================================");
        testFrequencyCounting();
        testFindDuplicates();
        testSecondHighestWithStreams();
        testEmployeeGroupingAndAverage();
        testFlatMap();
        testNumbersStartingWithOne();
        testPartitioningEvenOdd();
        testSummaryStatistics();
        testMultiFieldSorting();
        testStringJoining();
    }

    public record Emp(int id, String name, String department, double salary) {}

    // Q1: Character Frequency
    private void testFrequencyCounting() {
        System.out.println("\n--- [Q1] CHARACTER FREQUENCY WITH STREAMS ---");
        String text = "finwallet";
        Map<Character, Long> freq = text.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println("INPUT : \"" + text + "\"");
        System.out.println("OUTPUT: " + freq);
    }

    // Q2: Find Duplicates
    private void testFindDuplicates() {
        System.out.println("\n--- [Q2] FIND DUPLICATES FROM LIST ---");
        List<Integer> numbers = List.of(10, 20, 30, 20, 40, 50, 10, 60, 10);
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicates = numbers.stream()
                .filter(n -> !seen.add(n))
                .collect(Collectors.toSet());
        System.out.println("INPUT : " + numbers);
        System.out.println("OUTPUT: Duplicates = " + duplicates);
    }

    // Q3: Second Highest Number
    private void testSecondHighestWithStreams() {
        System.out.println("\n--- [Q3] SECOND HIGHEST NUMBER WITH STREAMS ---");
        List<Integer> numbers = List.of(5, 12, 90, 45, 90, 80, 23);
        Optional<Integer> second = numbers.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst();
        System.out.println("INPUT : " + numbers);
        System.out.println("OUTPUT: Second Highest = " + second.orElse(-1));
    }

    // Q4: Group By Department & Average Salary
    private void testEmployeeGroupingAndAverage() {
        System.out.println("\n--- [Q4] GROUP BY DEPT & AVERAGE SALARY ---");
        List<Emp> emps = List.of(
                new Emp(1, "Ramesh", "Engineering", 80000),
                new Emp(2, "Amit", "Engineering", 60000),
                new Emp(3, "Priya", "Finance", 90000),
                new Emp(4, "Rohit", "Finance", 70000),
                new Emp(5, "Sneha", "HR", 50000)
        );
        Map<String, Double> avgSalaries = emps.stream()
                .collect(Collectors.groupingBy(Emp::department, Collectors.averagingDouble(Emp::salary)));
        System.out.println("INPUT : 5 Employees in 3 Depts");
        System.out.println("OUTPUT: " + avgSalaries);
    }

    // Q5: FlatMap
    private void testFlatMap() {
        System.out.println("\n--- [Q5] FLATMAP (FLATTENING NESTED COLLECTIONS) ---");
        List<List<String>> skills = List.of(
                List.of("Java", "Spring Boot"),
                List.of("MySQL", "Docker", "Java"),
                List.of("AWS", "Kubernetes", "Spring Boot")
        );
        List<String> flat = skills.stream().flatMap(List::stream).distinct().toList();
        System.out.println("INPUT : " + skills);
        System.out.println("OUTPUT: Flattened Unique = " + flat);
    }

    // Q6: Numbers Starting With '1'
    private void testNumbersStartingWithOne() {
        System.out.println("\n--- [Q6] NUMBERS STARTING WITH '1' ---");
        List<Integer> list = List.of(10, 15, 8, 49, 25, 98, 102);
        List<Integer> startingWithOne = list.stream()
                .map(String::valueOf)
                .filter(s -> s.startsWith("1"))
                .map(Integer::valueOf)
                .toList();
        System.out.println("INPUT : " + list);
        System.out.println("OUTPUT: Starting with '1' = " + startingWithOne);
    }

    // Q7: Partitioning Even & Odd
    private void testPartitioningEvenOdd() {
        System.out.println("\n--- [Q7] PARTITIONING BY EVEN & ODD ---");
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Map<Boolean, List<Integer>> partitioned = numbers.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("INPUT : " + numbers);
        System.out.println("OUTPUT: Even Numbers (true)  = " + partitioned.get(true));
        System.out.println("OUTPUT: Odd Numbers (false)  = " + partitioned.get(false));
    }

    // Q8: SummaryStatistics
    private void testSummaryStatistics() {
        System.out.println("\n--- [Q8] SUMMARY STATISTICS (IN 1 PASS) ---");
        List<Integer> transactions = List.of(500, 1200, 300, 4500, 150);
        IntSummaryStatistics stats = transactions.stream()
                .mapToInt(Integer::intValue)
                .summaryStatistics();
        System.out.println("INPUT : " + transactions);
        System.out.printf("OUTPUT: Count=%d | Sum=%d | Min=%d | Max=%d | Avg=%.2f%n",
                stats.getCount(), stats.getSum(), stats.getMin(), stats.getMax(), stats.getAverage());
    }

    // Q9: Multi-field Sorting
    private void testMultiFieldSorting() {
        System.out.println("\n--- [Q9] MULTI-FIELD SORTING (Dept ASC, Salary DESC) ---");
        List<Emp> emps = new ArrayList<>(List.of(
                new Emp(1, "Ramesh", "Engineering", 80000),
                new Emp(2, "Amit", "Engineering", 95000),
                new Emp(3, "Priya", "Finance", 70000),
                new Emp(4, "Rohit", "Finance", 90000)
        ));
        emps.sort(Comparator.comparing(Emp::department)
                .thenComparing(Comparator.comparing(Emp::salary).reversed()));
        System.out.println("OUTPUT: Sorted Employees:");
        for (Emp e : emps) {
            System.out.printf("   Dept: %-12s | Salary: ₹%-6.0f | Name: %s%n", e.department(), e.salary(), e.name());
        }
    }

    // Q10: String Joining
    private void testStringJoining() {
        System.out.println("\n--- [Q10] COLLECTORS.JOINING WITH DELIMITER ---");
        List<String> currencies = List.of("INR", "USD", "EUR", "GBP", "JPY");
        String formatted = currencies.stream()
                .collect(Collectors.joining(", ", "Supported Currencies: [", "]"));
        System.out.println("INPUT : " + currencies);
        System.out.println("OUTPUT: " + formatted);
    }
}
