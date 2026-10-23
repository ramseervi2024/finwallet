package com.rps.finwallet.codinginterview;

/**
 * ============================================================================
 * 🔗 TOP 5 LINKED LIST CODING INTERVIEW QUESTIONS (COMPLETE & EXHAUSTIVE)
 * ============================================================================
 * 1. Reverse a Linked List (In-place 3 Pointers) - O(n) Time, O(1) Space
 * 2. Detect Cycle in Linked List (Floyd's Tortoise & Hare) - O(n) Time, O(1) Space
 * 3. Find Middle Node (Fast & Slow Pointer) - O(n) Time, O(1) Space
 * 4. Merge Two Sorted Linked Lists - O(m + n) Time, O(1) Space
 * 5. Remove N-th Node From End of List - O(n) Time, O(1) Space
 * ============================================================================
 */
public class LinkedListInterviewQuestions {

    public void runAll() {
        System.out.println("\n=======================================================");
        System.out.println("🔗 [INTERVIEW READY] TOP 5 LINKED LIST CODING QUESTIONS");
        System.out.println("=======================================================");
        testReverseLinkedList();
        testCycleDetection();
        testFindMiddleNode();
        testMergeTwoSortedLists();
        testRemoveNthFromEnd();
    }

    // Singly Linked List Node Definition
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    private static String printList(ListNode head) {
        StringBuilder sb = new StringBuilder();
        ListNode curr = head;
        while (curr != null) {
            sb.append(curr.val);
            if (curr.next != null) sb.append(" -> ");
            curr = curr.next;
        }
        return sb.toString();
    }

    // =========================================================================
    // Q1: REVERSE A LINKED LIST (Iterative In-place)
    // 3 Pointers: prev, curr, nextNode
    // Time: O(n) | Space: O(1)
    // =========================================================================
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode nextNode = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextNode;
        }
        return prev;
    }

    private void testReverseLinkedList() {
        System.out.println("\n--- [Q1] REVERSE A LINKED LIST ---");
        ListNode head = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));
        System.out.println("INPUT : " + printList(head));
        ListNode reversed = reverseList(head);
        System.out.println("OUTPUT: " + printList(reversed));
    }

    // =========================================================================
    // Q2: DETECT CYCLE IN LINKED LIST (Floyd's Tortoise & Hare)
    // Fast pointer moves 2 steps, slow pointer moves 1 step.
    // Agar cycle hai to dono zaroor meet karenge!
    // Time: O(n) | Space: O(1)
    // =========================================================================
    public boolean hasCycle(ListNode head) {
        if (head == null) return false;
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }

    private void testCycleDetection() {
        System.out.println("\n--- [Q2] DETECT CYCLE (FLOYD'S ALGORITHM) ---");
        ListNode node1 = new ListNode(3);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(0);
        ListNode node4 = new ListNode(-4);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node2; // Cycle created back to node 2!

        System.out.println("INPUT : 3 -> 2 -> 0 -> -4 -> (loops back to 2)");
        System.out.println("OUTPUT: Has Cycle? " + hasCycle(node1));
    }

    // =========================================================================
    // Q3: FIND MIDDLE NODE OF LINKED LIST
    // Fast moves 2 steps, slow moves 1 step.
    // Jab fast end par pahuchega, slow theek middle me hoga!
    // Time: O(n) | Space: O(1)
    // =========================================================================
    public ListNode findMiddle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    private void testFindMiddleNode() {
        System.out.println("\n--- [Q3] FIND MIDDLE NODE (FAST & SLOW POINTER) ---");
        ListNode head = new ListNode(10, new ListNode(20, new ListNode(30, new ListNode(40, new ListNode(50)))));
        System.out.println("INPUT : " + printList(head));
        ListNode mid = findMiddle(head);
        System.out.println("OUTPUT: Middle Node Value = " + (mid != null ? mid.val : "null"));
    }

    // =========================================================================
    // Q4: MERGE TWO SORTED LINKED LISTS
    // Time: O(m + n) | Space: O(1)
    // =========================================================================
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1);
        ListNode current = dummy;

        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                current.next = l1;
                l1 = l1.next;
            } else {
                current.next = l2;
                l2 = l2.next;
            }
            current = current.next;
        }
        current.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }

    private void testMergeTwoSortedLists() {
        System.out.println("\n--- [Q4] MERGE TWO SORTED LISTS ---");
        ListNode l1 = new ListNode(1, new ListNode(3, new ListNode(5)));
        ListNode l2 = new ListNode(2, new ListNode(4, new ListNode(6)));
        System.out.println("INPUT : L1 = " + printList(l1) + " | L2 = " + printList(l2));
        ListNode merged = mergeTwoLists(l1, l2);
        System.out.println("OUTPUT: Merged = " + printList(merged));
    }

    // =========================================================================
    // Q5: REMOVE N-TH NODE FROM END OF LIST (1-Pass)
    // Fast pointer ko pehle N steps aage bhejo, fir dono ko sath sath move karo
    // Time: O(n) | Space: O(1)
    // =========================================================================
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);
        ListNode fast = dummy;
        ListNode slow = dummy;

        for (int i = 0; i <= n; i++) {
            fast = fast.next;
        }
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        slow.next = slow.next.next;
        return dummy.next;
    }

    private void testRemoveNthFromEnd() {
        System.out.println("\n--- [Q5] REMOVE N-TH NODE FROM END ---");
        ListNode head = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));
        int n = 2; // remove 4 (2nd from end)
        System.out.println("INPUT : " + printList(head) + ", n = " + n);
        ListNode updated = removeNthFromEnd(head, n);
        System.out.println("OUTPUT: " + printList(updated));
    }
}
