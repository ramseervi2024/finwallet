package com.rps.finwallet.codinginterview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * 📦 TOP 10 ARRAY CODING INTERVIEW QUESTIONS (COMPLETE & EXHAUSTIVE)
 * ============================================================================
 * 1. Two Sum (Target sum pair find karna) - O(n)
 * 2. Second Highest Number (Bina sort kiye) - O(n)
 * 3. Move All Zeros to End (In-place array manipulation) - O(n)
 * 4. Find Missing Number in 1 to N - O(n)
 * 5. Remove Duplicates from Sorted Array (In-place) - O(n)
 * 6. Kadane's Algorithm (Maximum Subarray Sum) - O(n)
 * 7. Rotate Array by K Steps to Right - O(n)
 * 8. Subarray with Given Sum (Contiguous Subarray) - O(n)
 * 9. Container With Most Water (Two-Pointer Max Area) - O(n)
 * 10. Merge Two Sorted Arrays - O(m + n)
 * ============================================================================
 */
public class ArrayInterviewQuestions {

    public void runAll() {
        System.out.println("\n=======================================================");
        System.out.println("📦 [INTERVIEW READY] TOP 10 ARRAY CODING QUESTIONS");
        System.out.println("=======================================================");
        testTwoSum();
        testSecondHighest();
        testMoveZerosToEnd();
        testFindMissingNumber();
        testRemoveDuplicatesFromSorted();
        testKadaneMaxSubarray();
        testRotateArray();
        testSubarrayWithGivenSum();
        testContainerWithMostWater();
        testMergeSortedArrays();
    }

    // =========================================================================
    // Q1: TWO SUM
    // Input: nums = [2, 7, 11, 15], target = 9 -> Output: [0, 1]
    // Time: O(n) | Space: O(n)
    // =========================================================================
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{};
    }

    private void testTwoSum() {
        System.out.println("\n--- [Q1] TWO SUM ---");
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] res = twoSum(nums, target);
        System.out.println("INPUT : nums = " + Arrays.toString(nums) + ", target = " + target);
        System.out.println("OUTPUT: Indices = " + Arrays.toString(res) + " (Values: " + nums[res[0]] + " + " + nums[res[1]] + " = " + target + ")");
    }

    // =========================================================================
    // Q2: SECOND HIGHEST NUMBER (Without Sorting)
    // Input: [12, 35, 1, 10, 34, 35, 1] -> Output: 34
    // Time: O(n) | Space: O(1)
    // =========================================================================
    public int findSecondLargest(int[] arr) {
        int highest = Integer.MIN_VALUE;
        int secondHighest = Integer.MIN_VALUE;

        for (int num : arr) {
            if (num > highest) {
                secondHighest = highest;
                highest = num;
            } else if (num > secondHighest && num != highest) {
                secondHighest = num;
            }
        }
        return secondHighest;
    }

    private void testSecondHighest() {
        System.out.println("\n--- [Q2] SECOND HIGHEST NUMBER ---");
        int[] nums = {12, 35, 1, 10, 34, 35, 1};
        int res = findSecondLargest(nums);
        System.out.println("INPUT : " + Arrays.toString(nums));
        System.out.println("OUTPUT: Second Highest = " + res + " (Skips duplicates of 35)");
    }

    // =========================================================================
    // Q3: MOVE ALL ZEROS TO END (In-place)
    // Input: [0, 1, 0, 3, 12] -> Output: [1, 3, 12, 0, 0]
    // Time: O(n) | Space: O(1)
    // =========================================================================
    public void moveZerosToEnd(int[] nums) {
        int insertPos = 0;
        for (int num : nums) {
            if (num != 0) nums[insertPos++] = num;
        }
        while (insertPos < nums.length) {
            nums[insertPos++] = 0;
        }
    }

    private void testMoveZerosToEnd() {
        System.out.println("\n--- [Q3] MOVE ALL ZEROS TO END ---");
        int[] arr = {0, 1, 0, 3, 12, 0, 5};
        System.out.println("INPUT : " + Arrays.toString(arr));
        moveZerosToEnd(arr);
        System.out.println("OUTPUT: " + Arrays.toString(arr));
    }

    // =========================================================================
    // Q4: FIND MISSING NUMBER (1 to N)
    // Formula: expectedSum = N * (N + 1) / 2
    // Input: [1, 2, 4, 5, 6], N = 6 -> Output: 3
    // Time: O(n) | Space: O(1)
    // =========================================================================
    public int findMissingNumber(int[] nums, int n) {
        int expectedSum = n * (n + 1) / 2;
        int actualSum = 0;
        for (int num : nums) actualSum += num;
        return expectedSum - actualSum;
    }

    private void testFindMissingNumber() {
        System.out.println("\n--- [Q4] FIND MISSING NUMBER IN 1 TO N ---");
        int[] arr = {1, 2, 4, 5, 6};
        int missing = findMissingNumber(arr, 6);
        System.out.println("INPUT : " + Arrays.toString(arr) + " (Range: 1 to 6)");
        System.out.println("OUTPUT: Missing Number = " + missing);
    }

    // =========================================================================
    // Q5: REMOVE DUPLICATES FROM SORTED ARRAY (In-place)
    // Input: [1, 1, 2, 2, 3, 4, 4, 5] -> Output: 5 unique elements [1, 2, 3, 4, 5]
    // Time: O(n) | Space: O(1)
    // =========================================================================
    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                nums[++i] = nums[j];
            }
        }
        return i + 1;
    }

    private void testRemoveDuplicatesFromSorted() {
        System.out.println("\n--- [Q5] REMOVE DUPLICATES FROM SORTED ARRAY ---");
        int[] sorted = {1, 1, 2, 2, 3, 4, 4, 5};
        System.out.println("INPUT : " + Arrays.toString(sorted));
        int count = removeDuplicates(sorted);
        System.out.print("OUTPUT: Unique Count = " + count + " -> " + Arrays.toString(Arrays.copyOf(sorted, count)) + "\n");
    }

    // =========================================================================
    // Q6: KADANE'S ALGORITHM (MAXIMUM SUBARRAY SUM)
    // Interviewer Favorite: Array me se aisi contiguous subarray dhundho jiska sum MAX ho.
    // Input: [-2, 1, -3, 4, -1, 2, 1, -5, 4] -> Output: 6 (Subarray: [4, -1, 2, 1])
    // Time: O(n) | Space: O(1)
    // =========================================================================
    public int maxSubArraySum(int[] nums) {
        int maxSoFar = nums[0];
        int currentSum = nums[0];

        for (int i = 1; i < nums.length; i++) {
            // Ya to naye number se fresh start karo, ya pichle sum me add karo
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            maxSoFar = Math.max(maxSoFar, currentSum);
        }
        return maxSoFar;
    }

    private void testKadaneMaxSubarray() {
        System.out.println("\n--- [Q6] KADANE'S ALGORITHM (MAX SUBARRAY SUM) ---");
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int max = maxSubArraySum(nums);
        System.out.println("INPUT : " + Arrays.toString(nums));
        System.out.println("OUTPUT: Max Subarray Sum = " + max + " (Subarray [4, -1, 2, 1] has sum 6)");
    }

    // =========================================================================
    // Q7: ROTATE ARRAY BY K STEPS (In-Place 3-Reverse Technique)
    // Input: nums = [1, 2, 3, 4, 5, 6, 7], k = 3 -> Output: [5, 6, 7, 1, 2, 3, 4]
    // Trick:
    // 1. Reverse entire array: [7, 6, 5, 4, 3, 2, 1]
    // 2. Reverse first k elements: [5, 6, 7, 4, 3, 2, 1]
    // 3. Reverse remaining elements: [5, 6, 7, 1, 2, 3, 4]
    // Time: O(n) | Space: O(1)
    // =========================================================================
    public void rotate(int[] nums, int k) {
        k = k % nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    private void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start++] = nums[end];
            nums[end--] = temp;
        }
    }

    private void testRotateArray() {
        System.out.println("\n--- [Q7] ROTATE ARRAY BY K STEPS ---");
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        int k = 3;
        System.out.println("INPUT : " + Arrays.toString(arr) + ", k = " + k);
        rotate(arr, k);
        System.out.println("OUTPUT: " + Arrays.toString(arr));
    }

    // =========================================================================
    // Q8: SUBARRAY WITH GIVEN SUM (Sliding Window for positive numbers)
    // Input: arr = [1, 4, 20, 3, 10, 5], sum = 33 -> Output: Subarray [20, 3, 10]
    // Time: O(n) | Space: O(1)
    // =========================================================================
    public int[] findSubarrayWithSum(int[] arr, int targetSum) {
        int currentSum = 0;
        int start = 0;

        for (int end = 0; end < arr.length; end++) {
            currentSum += arr[end];

            while (currentSum > targetSum && start < end) {
                currentSum -= arr[start++];
            }

            if (currentSum == targetSum) {
                return new int[]{start, end};
            }
        }
        return new int[]{-1, -1};
    }

    private void testSubarrayWithGivenSum() {
        System.out.println("\n--- [Q8] SUBARRAY WITH GIVEN SUM ---");
        int[] arr = {1, 4, 20, 3, 10, 5};
        int target = 33;
        int[] range = findSubarrayWithSum(arr, target);
        System.out.println("INPUT : " + Arrays.toString(arr) + ", target = " + target);
        System.out.println("OUTPUT: Start Index = " + range[0] + ", End Index = " + range[1] +
                " -> " + Arrays.toString(Arrays.copyOfRange(arr, range[0], range[1] + 1)));
    }

    // =========================================================================
    // Q9: CONTAINER WITH MOST WATER (Two-Pointer Max Area)
    // Input: [1, 8, 6, 2, 5, 4, 8, 3, 7] -> Output: 49 (Width 7 * Height 7)
    // Time: O(n) | Space: O(1)
    // =========================================================================
    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int maxWater = 0;

        while (left < right) {
            int width = right - left;
            int currentArea = width * Math.min(height[left], height[right]);
            maxWater = Math.max(maxWater, currentArea);

            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxWater;
    }

    private void testContainerWithMostWater() {
        System.out.println("\n--- [Q9] CONTAINER WITH MOST WATER ---");
        int[] heights = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int water = maxArea(heights);
        System.out.println("INPUT : Heights = " + Arrays.toString(heights));
        System.out.println("OUTPUT: Max Water Trapped = " + water);
    }

    // =========================================================================
    // Q10: MERGE TWO SORTED ARRAYS
    // Input: A = [1, 3, 5, 7], B = [2, 4, 6, 8] -> Output: [1, 2, 3, 4, 5, 6, 7, 8]
    // Time: O(m + n) | Space: O(m + n)
    // =========================================================================
    public int[] mergeSorted(int[] a, int[] b) {
        int[] merged = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;

        while (i < a.length && j < b.length) {
            if (a[i] <= b[j]) merged[k++] = a[i++];
            else merged[k++] = b[j++];
        }
        while (i < a.length) merged[k++] = a[i++];
        while (j < b.length) merged[k++] = b[j++];

        return merged;
    }

    private void testMergeSortedArrays() {
        System.out.println("\n--- [Q10] MERGE TWO SORTED ARRAYS ---");
        int[] a = {1, 3, 5, 7};
        int[] b = {2, 4, 6, 8};
        int[] merged = mergeSorted(a, b);
        System.out.println("INPUT : A = " + Arrays.toString(a) + ", B = " + Arrays.toString(b));
        System.out.println("OUTPUT: Merged = " + Arrays.toString(merged));
    }
}
