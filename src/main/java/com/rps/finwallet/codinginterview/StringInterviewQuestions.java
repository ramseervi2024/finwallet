package com.rps.finwallet.codinginterview;

import java.util.*;

/**
 * ============================================================================
 * 🔤 TOP 8 STRING CODING INTERVIEW QUESTIONS (COMPLETE & EXHAUSTIVE)
 * ============================================================================
 * 1. Reverse String Without Built-in reverse() - Two Pointer O(n)
 * 2. Valid Anagram Check ("silent" & "listen") - Frequency Array O(n)
 * 3. First Non-Repeating Character ("swiss" -> 'w') - LinkedHashMap O(n)
 * 4. Longest Substring Without Repeating Characters - Sliding Window O(n)
 * 5. Valid Palindrome (Ignoring spaces & special chars) - Two Pointer O(n)
 * 6. Group Anagrams (Group words having same characters) - O(N * K log K)
 * 7. String Compression ("aabcccccaaa" -> "a2b1c5a3") - O(n)
 * 8. Longest Common Prefix (["flower","flow","flight"] -> "fl") - O(S)
 * ============================================================================
 */
public class StringInterviewQuestions {

    public void runAll() {
        System.out.println("\n=======================================================");
        System.out.println("🔤 [INTERVIEW READY] TOP 8 STRING CODING QUESTIONS");
        System.out.println("=======================================================");
        testReverseString();
        testAnagram();
        testFirstNonRepeatingChar();
        testLongestSubstringWithoutRepeating();
        testValidPalindrome();
        testGroupAnagrams();
        testStringCompression();
        testLongestCommonPrefix();
    }

    // =========================================================================
    // Q1: REVERSE A STRING WITHOUT BUILT-IN REVERSE()
    // Time: O(n) | Space: O(n)
    // =========================================================================
    public String reverseString(String s) {
        char[] chars = s.toCharArray();
        int left = 0, right = chars.length - 1;
        while (left < right) {
            char temp = chars[left];
            chars[left++] = chars[right];
            chars[right--] = temp;
        }
        return new String(chars);
    }

    private void testReverseString() {
        System.out.println("\n--- [Q1] REVERSE STRING (Two Pointer Swap) ---");
        String original = "FinWallet";
        String reversed = reverseString(original);
        System.out.println("INPUT : \"" + original + "\"");
        System.out.println("OUTPUT: \"" + reversed + "\"");
    }

    // =========================================================================
    // Q2: CHECK VALID ANAGRAM
    // Time: O(n) | Space: O(1) [26 characters array]
    // =========================================================================
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        int[] counts = new int[26];
        for (int i = 0; i < s.length(); i++) {
            counts[s.charAt(i) - 'a']++;
            counts[t.charAt(i) - 'a']--;
        }
        for (int count : counts) {
            if (count != 0) return false;
        }
        return true;
    }

    private void testAnagram() {
        System.out.println("\n--- [Q2] VALID ANAGRAM CHECK ---");
        String s1 = "listen", s2 = "silent";
        System.out.println("INPUT : s1 = \"" + s1 + "\", s2 = \"" + s2 + "\"");
        System.out.println("OUTPUT: Are Anagrams? " + isAnagram(s1, s2));
    }

    // =========================================================================
    // Q3: FIRST NON-REPEATING CHARACTER
    // Time: O(n) | Space: O(n)
    // =========================================================================
    public Character firstNonRepeatingChar(String s) {
        Map<Character, Integer> counts = new LinkedHashMap<>();
        for (char c : s.toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }
        for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
            if (entry.getValue() == 1) return entry.getKey();
        }
        return null;
    }

    private void testFirstNonRepeatingChar() {
        System.out.println("\n--- [Q3] FIRST NON-REPEATING CHARACTER ---");
        String word = "swiss";
        System.out.println("INPUT : \"" + word + "\"");
        System.out.println("OUTPUT: First Unique Character = '" + firstNonRepeatingChar(word) + "'");
    }

    // =========================================================================
    // Q4: LONGEST SUBSTRING WITHOUT REPEATING CHARACTERS (Sliding Window)
    // Time: O(n) | Space: O(n)
    // =========================================================================
    public int lengthOfLongestSubstring(String s) {
        Set<Character> seen = new HashSet<>();
        int maxLength = 0, left = 0;
        for (int right = 0; right < s.length(); right++) {
            while (seen.contains(s.charAt(right))) {
                seen.remove(s.charAt(left++));
            }
            seen.add(s.charAt(right));
            maxLength = Math.max(maxLength, right - left + 1);
        }
        return maxLength;
    }

    private void testLongestSubstringWithoutRepeating() {
        System.out.println("\n--- [Q4] LONGEST SUBSTRING WITHOUT REPEATING CHARACTERS ---");
        String s = "abcabcbb";
        System.out.println("INPUT : \"" + s + "\"");
        System.out.println("OUTPUT: Longest Length = " + lengthOfLongestSubstring(s) + " (Substring: \"abc\")");
    }

    // =========================================================================
    // Q5: VALID PALINDROME (ALPHANUMERIC)
    // Time: O(n) | Space: O(1)
    // =========================================================================
    public boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) left++;
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) right--;
            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    private void testValidPalindrome() {
        System.out.println("\n--- [Q5] VALID PALINDROME ---");
        String p = "A man, a plan, a canal: Panama";
        System.out.println("INPUT : \"" + p + "\"");
        System.out.println("OUTPUT: Is Palindrome? " + isPalindrome(p));
    }

    // =========================================================================
    // Q6: GROUP ANAGRAMS
    // Input: ["eat", "tea", "tan", "ate", "nat", "bat"]
    // Output: [["bat"], ["nat", "tan"], ["ate", "eat", "tea"]]
    // Trick: Har word ko sort karke Map ka key banao (e.g. "aet" -> ["eat", "tea", "ate"])
    // Time: O(N * K log K) | Space: O(N * K)
    // =========================================================================
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String word : strs) {
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            String sortedKey = new String(chars);

            map.computeIfAbsent(sortedKey, k -> new ArrayList<>()).add(word);
        }
        return new ArrayList<>(map.values());
    }

    private void testGroupAnagrams() {
        System.out.println("\n--- [Q6] GROUP ANAGRAMS ---");
        String[] words = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> grouped = groupAnagrams(words);
        System.out.println("INPUT : " + Arrays.toString(words));
        System.out.println("OUTPUT: " + grouped);
    }

    // =========================================================================
    // Q7: STRING COMPRESSION
    // Input: "aabcccccaaa" -> Output: "a2b1c5a3"
    // Time: O(n) | Space: O(n)
    // =========================================================================
    public String compressString(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        int count = 1;

        for (int i = 0; i < s.length(); i++) {
            if (i + 1 < s.length() && s.charAt(i) == s.charAt(i + 1)) {
                count++;
            } else {
                sb.append(s.charAt(i)).append(count);
                count = 1;
            }
        }
        return sb.toString();
    }

    private void testStringCompression() {
        System.out.println("\n--- [Q7] STRING COMPRESSION ---");
        String uncompressed = "aabcccccaaa";
        String compressed = compressString(uncompressed);
        System.out.println("INPUT : \"" + uncompressed + "\"");
        System.out.println("OUTPUT: \"" + compressed + "\"");
    }

    // =========================================================================
    // Q8: LONGEST COMMON PREFIX
    // Input: ["flower", "flow", "flight"] -> Output: "fl"
    // Time: O(S) where S is sum of all characters | Space: O(1)
    // =========================================================================
    public String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++) {
            while (strs[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) return "";
            }
        }
        return prefix;
    }

    private void testLongestCommonPrefix() {
        System.out.println("\n--- [Q8] LONGEST COMMON PREFIX ---");
        String[] words = {"flower", "flow", "flight"};
        String prefix = longestCommonPrefix(words);
        System.out.println("INPUT : " + Arrays.toString(words));
        System.out.println("OUTPUT: Longest Prefix = \"" + prefix + "\"");
    }
}
