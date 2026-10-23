package com.rps.finwallet.codinginterview;

import java.util.*;

/**
 * ============================================================================
 * 🧠 TOP 5 ALGORITHM & DYNAMIC PROGRAMMING INTERVIEW QUESTIONS
 * ============================================================================
 * 1. Binary Search (Iterative O(log n))
 * 2. Search in Rotated Sorted Array (O(log n))
 * 3. Climbing Stairs (Fibonacci DP Pattern) - O(n)
 * 4. Coin Change Problem (Minimum Coins to make Amount) - DP O(amount * n)
 * 5. Merge Overlapping Intervals ([[1,3],[2,6],[8,10]] -> [[1,6],[8,10]]) - O(n log n)
 * ============================================================================
 */
public class AlgorithmAndDpInterviewQuestions {

    public void runAll() {
        System.out.println("\n=======================================================");
        System.out.println("🧠 [INTERVIEW READY] ALGORITHMS & DYNAMIC PROGRAMMING");
        System.out.println("=======================================================");
        testBinarySearch();
        testSearchRotatedSorted();
        testClimbingStairs();
        testCoinChange();
        testMergeIntervals();
    }

    // =========================================================================
    // Q1: BINARY SEARCH (Iterative)
    // Time: O(log n) | Space: O(1)
    // =========================================================================
    public int binarySearch(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2; // Prevents integer overflow!
            if (nums[mid] == target) return mid;
            else if (nums[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    private void testBinarySearch() {
        System.out.println("\n--- [Q1] BINARY SEARCH ---");
        int[] sorted = {-1, 0, 3, 5, 9, 12};
        int target = 9;
        int idx = binarySearch(sorted, target);
        System.out.println("INPUT : " + Arrays.toString(sorted) + ", Target = " + target);
        System.out.println("OUTPUT: Found at Index = " + idx);
    }

    // =========================================================================
    // Q2: SEARCH IN ROTATED SORTED ARRAY
    // e.g. [4, 5, 6, 7, 0, 1, 2], target = 0 -> Output index 4
    // Time: O(log n) | Space: O(1)
    // =========================================================================
    public int searchRotated(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) return mid;

            // Check if left half is sorted
            if (nums[left] <= nums[mid]) {
                if (target >= nums[left] && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            // Right half must be sorted
            else {
                if (target > nums[mid] && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }

    private void testSearchRotatedSorted() {
        System.out.println("\n--- [Q2] SEARCH IN ROTATED SORTED ARRAY ---");
        int[] rotated = {4, 5, 6, 7, 0, 1, 2};
        int target = 0;
        int idx = searchRotated(rotated, target);
        System.out.println("INPUT : " + Arrays.toString(rotated) + ", Target = " + target);
        System.out.println("OUTPUT: Found at Index = " + idx);
    }

    // =========================================================================
    // Q3: CLIMBING STAIRS (DP Fibonacci)
    // Har step par ya to 1 step ya 2 steps le sakte ho. N steps climb karne ke kitne ways hain?
    // Formula: ways[i] = ways[i - 1] + ways[i - 2]
    // Time: O(n) | Space: O(1)
    // =========================================================================
    public int climbStairs(int n) {
        if (n <= 2) return n;
        int prev2 = 1;
        int prev1 = 2;
        for (int i = 3; i <= n; i++) {
            int current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        return prev1;
    }

    private void testClimbingStairs() {
        System.out.println("\n--- [Q3] CLIMBING STAIRS (DP) ---");
        int n = 5;
        int ways = climbStairs(n);
        System.out.println("INPUT : n = " + n + " stairs");
        System.out.println("OUTPUT: Total distinct ways = " + ways + " (Expected 8)");
    }

    // =========================================================================
    // Q4: COIN CHANGE (Bottom-up DP)
    // Input: coins = [1, 2, 5], amount = 11 -> Output: 3 (5 + 5 + 1)
    // Time: O(amount * n) | Space: O(amount)
    // =========================================================================
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;

        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i - coin >= 0) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    private void testCoinChange() {
        System.out.println("\n--- [Q4] COIN CHANGE (MINIMUM COINS) ---");
        int[] coins = {1, 2, 5};
        int amount = 11;
        int minCoins = coinChange(coins, amount);
        System.out.println("INPUT : Coins = " + Arrays.toString(coins) + ", Amount = " + amount);
        System.out.println("OUTPUT: Minimum Coins = " + minCoins + " (5 + 5 + 1)");
    }

    // =========================================================================
    // Q5: MERGE OVERLAPPING INTERVALS
    // Input: [[1,3],[2,6],[8,10],[15,18]] -> Output: [[1,6],[8,10],[15,18]]
    // Time: O(n log n) | Space: O(n)
    // =========================================================================
    public int[][] mergeIntervals(int[][] intervals) {
        if (intervals.length <= 1) return intervals;

        // Sort by start time
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

        List<int[]> result = new ArrayList<>();
        int[] current = intervals[0];
        result.add(current);

        for (int[] next : intervals) {
            if (next[0] <= current[1]) { // Overlap detected
                current[1] = Math.max(current[1], next[1]); // Merge
            } else {
                current = next;
                result.add(current);
            }
        }
        return result.toArray(new int[result.size()][]);
    }

    private void testMergeIntervals() {
        System.out.println("\n--- [Q5] MERGE OVERLAPPING INTERVALS ---");
        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] merged = mergeIntervals(intervals);
        System.out.println("INPUT : " + Arrays.deepToString(intervals));
        System.out.println("OUTPUT: Merged Intervals = " + Arrays.deepToString(merged));
    }
}
