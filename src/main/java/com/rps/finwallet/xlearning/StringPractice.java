package com.rps.finwallet.xlearning;

public class StringPractice implements PracticeRunner {

    @Override
    public String getTitle() {
        return "String & StringBuilder (Immutability, Reversal, Palindrome Check)";
    }

    @Override
    public void run() {
        demonstrateStringBuilder();
        demonstratePalindromeCheck();
    }

    private void demonstrateStringBuilder() {
        System.out.println("\n--- [1] String vs StringBuilder ---");
        // String is immutable: each concatenation creates a new object in memory
        String str = "Hello";
        str = str + " World";
        System.out.println("String Result: " + str);

        // StringBuilder is mutable: fast and memory efficient for loops
        StringBuilder sb = new StringBuilder("Fin");
        sb.append("Wallet").append(" App");
        System.out.println("StringBuilder Result: " + sb);
        System.out.println("Reversed StringBuilder: " + sb.reverse());
    }

    private void demonstratePalindromeCheck() {
        System.out.println("\n--- [2] Interview Pattern: Palindrome Check ---");
        String word1 = "madam";
        String word2 = "wallet";

        System.out.println("\"" + word1 + "\" is palindrome? " + isPalindrome(word1));
        System.out.println("\"" + word2 + "\" is palindrome? " + isPalindrome(word2));
    }

    private boolean isPalindrome(String text) {
        String clean = text.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
        String reversed = new StringBuilder(clean).reverse().toString();
        return clean.equals(reversed);
    }
}
