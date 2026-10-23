package com.rps.finwallet.codinginterview;

import java.util.*;

/**
 * ============================================================================
 * 🥞 TOP 5 STACK & QUEUE CODING INTERVIEW QUESTIONS (COMPLETE & EXHAUSTIVE)
 * ============================================================================
 * 1. Valid Parentheses / Balanced Brackets ("{[()]}") - Stack O(n)
 * 2. Min Stack (getMin in O(1) time) - Two Stacks / Pair O(1)
 * 3. Next Greater Element (Monotonic Stack) - O(n)
 * 4. Implement Queue using Two Stacks - Amortized O(1)
 * 5. Daily Temperatures (Wait days for warmer temperature) - O(n)
 * ============================================================================
 */
public class StackQueueInterviewQuestions {

    public void runAll() {
        System.out.println("\n=======================================================");
        System.out.println("🥞 [INTERVIEW READY] TOP 5 STACK & QUEUE QUESTIONS");
        System.out.println("=======================================================");
        testValidParentheses();
        testMinStack();
        testNextGreaterElement();
        testQueueUsingStacks();
        testDailyTemperatures();
    }

    // =========================================================================
    // Q1: VALID PARENTHESES (Balanced Brackets)
    // Input: "()[]{}" -> true | "(]" -> false | "([)]" -> false
    // Time: O(n) | Space: O(n)
    // =========================================================================
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(') stack.push(')');
            else if (c == '{') stack.push('}');
            else if (c == '[') stack.push(']');
            else if (stack.isEmpty() || stack.pop() != c) {
                return false;
            }
        }
        return stack.isEmpty();
    }

    private void testValidParentheses() {
        System.out.println("\n--- [Q1] VALID PARENTHESES ---");
        String s1 = "{[()]}";
        String s2 = "([)]";
        System.out.println("INPUT : \"" + s1 + "\" -> Is Valid? " + isValid(s1));
        System.out.println("INPUT : \"" + s2 + "\" -> Is Valid? " + isValid(s2));
    }

    // =========================================================================
    // Q2: MIN STACK (getMin() in O(1) time)
    // Trick: Har element ke sath current minimum bhi store karo!
    // =========================================================================
    public static class MinStack {
        private final Deque<int[]> stack = new ArrayDeque<>(); // [val, currentMin]

        public void push(int val) {
            int currentMin = stack.isEmpty() ? val : Math.min(val, stack.peek()[1]);
            stack.push(new int[]{val, currentMin});
        }

        public void pop() {
            stack.pop();
        }

        public int top() {
            return stack.peek()[0];
        }

        public int getMin() {
            return stack.peek()[1];
        }
    }

    private void testMinStack() {
        System.out.println("\n--- [Q2] MIN STACK (O(1) GET_MIN) ---");
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println("Pushed: -2, 0, -3");
        System.out.println("Current Min: " + minStack.getMin() + " (Expected -3)");
        minStack.pop();
        System.out.println("Popped -3. Top: " + minStack.top() + " | Current Min: " + minStack.getMin() + " (Expected -2)");
    }

    // =========================================================================
    // Q3: NEXT GREATER ELEMENT (Monotonic Decreasing Stack)
    // Input: [4, 5, 2, 25] -> Output: [5, 25, 25, -1]
    // Time: O(n) | Space: O(n)
    // =========================================================================
    public int[] nextGreaterElements(int[] nums) {
        int[] result = new int[nums.length];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>(); // stores indices

        for (int i = 0; i < nums.length; i++) {
            while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
                int smallerIndex = stack.pop();
                result[smallerIndex] = nums[i];
            }
            stack.push(i);
        }
        return result;
    }

    private void testNextGreaterElement() {
        System.out.println("\n--- [Q3] NEXT GREATER ELEMENT ---");
        int[] arr = {4, 5, 2, 25};
        int[] nxt = nextGreaterElements(arr);
        System.out.println("INPUT : " + Arrays.toString(arr));
        System.out.println("OUTPUT: " + Arrays.toString(nxt));
    }

    // =========================================================================
    // Q4: IMPLEMENT QUEUE USING TWO STACKS
    // Push Stack & Pop Stack
    // Amortized Time: O(1) for push & pop
    // =========================================================================
    public static class MyQueue {
        private final Deque<Integer> inStack = new ArrayDeque<>();
        private final Deque<Integer> outStack = new ArrayDeque<>();

        public void push(int x) {
            inStack.push(x);
        }

        public int pop() {
            peek();
            return outStack.pop();
        }

        public int peek() {
            if (outStack.isEmpty()) {
                while (!inStack.isEmpty()) {
                    outStack.push(inStack.pop());
                }
            }
            return outStack.peek();
        }

        public boolean empty() {
            return inStack.isEmpty() && outStack.isEmpty();
        }
    }

    private void testQueueUsingStacks() {
        System.out.println("\n--- [Q4] QUEUE USING TWO STACKS ---");
        MyQueue queue = new MyQueue();
        queue.push(10);
        queue.push(20);
        queue.push(30);
        System.out.println("Pushed: 10, 20, 30");
        System.out.println("Pop (FIFO): " + queue.pop() + " (Expected 10)");
        System.out.println("Peek next : " + queue.peek() + " (Expected 20)");
    }

    // =========================================================================
    // Q5: DAILY TEMPERATURES (Wait days for warmer weather)
    // Input: [73, 74, 75, 71, 69, 72, 76, 73] -> Output: [1, 1, 4, 2, 1, 1, 0, 0]
    // Time: O(n) | Space: O(n)
    // =========================================================================
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        Deque<Integer> stack = new ArrayDeque<>(); // stores indices

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int prevDay = stack.pop();
                answer[prevDay] = i - prevDay;
            }
            stack.push(i);
        }
        return answer;
    }

    private void testDailyTemperatures() {
        System.out.println("\n--- [Q5] DAILY TEMPERATURES (WAIT DAYS) ---");
        int[] temps = {73, 74, 75, 71, 69, 72, 76, 73};
        int[] waitDays = dailyTemperatures(temps);
        System.out.println("INPUT : " + Arrays.toString(temps));
        System.out.println("OUTPUT: Wait Days = " + Arrays.toString(waitDays));
    }
}
