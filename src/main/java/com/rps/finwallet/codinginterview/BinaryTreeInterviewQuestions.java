package com.rps.finwallet.codinginterview;

import java.util.*;

/**
 * ============================================================================
 * 🌳 TOP 5 BINARY TREE & BST INTERVIEW QUESTIONS (COMPLETE & EXHAUSTIVE)
 * ============================================================================
 * 1. Maximum Depth / Height of Binary Tree (DFS Recursion) - O(n)
 * 2. Invert / Mirror Binary Tree (The Famous Google Interview Question) - O(n)
 * 3. Level Order Traversal (Breadth-First Search with Queue) - O(n)
 * 4. Validate Binary Search Tree (BST In-Order / Min-Max Range) - O(n)
 * 5. Lowest Common Ancestor (LCA in BST) - O(h)
 * ============================================================================
 */
public class BinaryTreeInterviewQuestions {

    public void runAll() {
        System.out.println("\n=======================================================");
        System.out.println("🌳 [INTERVIEW READY] TOP 5 BINARY TREE & BST QUESTIONS");
        System.out.println("=======================================================");
        testMaxDepth();
        testInvertTree();
        testLevelOrder();
        testValidateBst();
        testLowestCommonAncestor();
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // =========================================================================
    // Q1: MAXIMUM DEPTH / HEIGHT OF BINARY TREE
    // Height = 1 + max(leftHeight, rightHeight)
    // Time: O(n) | Space: O(h) recursion stack
    // =========================================================================
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    private void testMaxDepth() {
        System.out.println("\n--- [Q1] MAXIMUM DEPTH OF BINARY TREE ---");
        // Tree: 3 -> [9, 20 -> [15, 7]]
        TreeNode root = new TreeNode(3, new TreeNode(9), new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        int depth = maxDepth(root);
        System.out.println("INPUT : Root = 3, Left = 9, Right = 20 (with children 15, 7)");
        System.out.println("OUTPUT: Max Depth = " + depth);
    }

    // =========================================================================
    // Q2: INVERT / MIRROR A BINARY TREE
    // Left child aur right child ko recursively swap karna
    // Time: O(n) | Space: O(h)
    // =========================================================================
    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        root.left = right;
        root.right = left;
        return root;
    }

    private void testInvertTree() {
        System.out.println("\n--- [Q2] INVERT / MIRROR BINARY TREE ---");
        TreeNode root = new TreeNode(4, new TreeNode(2), new TreeNode(7));
        System.out.println("INPUT : Root 4 (Left: 2, Right: 7)");
        invertTree(root);
        System.out.println("OUTPUT: Inverted Root 4 (Left: " + root.left.val + ", Right: " + root.right.val + ")");
    }

    // =========================================================================
    // Q3: LEVEL ORDER TRAVERSAL (BFS with Queue)
    // Har level ke nodes ko list me pack karke return karna
    // Time: O(n) | Space: O(n)
    // =========================================================================
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevel.add(node.val);
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            result.add(currentLevel);
        }
        return result;
    }

    private void testLevelOrder() {
        System.out.println("\n--- [Q3] LEVEL ORDER TRAVERSAL (BFS) ---");
        TreeNode root = new TreeNode(3, new TreeNode(9), new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        List<List<Integer>> levels = levelOrder(root);
        System.out.println("INPUT : Tree [3, 9, 20, null, null, 15, 7]");
        System.out.println("OUTPUT: Level-by-level = " + levels);
    }

    // =========================================================================
    // Q4: VALIDATE BINARY SEARCH TREE (BST)
    // Rule: Left subtree < Root < Right subtree (Har node ke liye min-max bound hona chahiye)
    // Time: O(n) | Space: O(h)
    // =========================================================================
    public boolean isValidBST(TreeNode root) {
        return validate(root, null, null);
    }

    private boolean validate(TreeNode node, Integer min, Integer max) {
        if (node == null) return true;
        if ((min != null && node.val <= min) || (max != null && node.val >= max)) {
            return false;
        }
        return validate(node.left, min, node.val) && validate(node.right, node.val, max);
    }

    private void testValidateBst() {
        System.out.println("\n--- [Q4] VALIDATE BINARY SEARCH TREE (BST) ---");
        TreeNode validBst = new TreeNode(5, new TreeNode(3), new TreeNode(7));
        TreeNode invalidBst = new TreeNode(5, new TreeNode(6), new TreeNode(4)); // 6 > 5 on left is invalid!

        System.out.println("INPUT 1: [5, Left: 3, Right: 7] -> Valid BST? " + isValidBST(validBst));
        System.out.println("INPUT 2: [5, Left: 6, Right: 4] -> Valid BST? " + isValidBST(invalidBst));
    }

    // =========================================================================
    // Q5: LOWEST COMMON ANCESTOR (LCA in BST)
    // BST Property:
    // - Agar dono p aur q root se chhote hain -> Left jao.
    // - Agar dono p aur q root se bade hain -> Right jao.
    // - Agar ek chhota aur ek bada hai -> Root hi LCA hai!
    // Time: O(h) | Space: O(1)
    // =========================================================================
    public TreeNode lowestCommonAncestor(TreeNode root, int pVal, int qVal) {
        TreeNode curr = root;
        while (curr != null) {
            if (pVal < curr.val && qVal < curr.val) {
                curr = curr.left;
            } else if (pVal > curr.val && qVal > curr.val) {
                curr = curr.right;
            } else {
                return curr; // Split point is the LCA
            }
        }
        return null;
    }

    private void testLowestCommonAncestor() {
        System.out.println("\n--- [Q5] LOWEST COMMON ANCESTOR (LCA IN BST) ---");
        TreeNode root = new TreeNode(6,
                new TreeNode(2, new TreeNode(0), new TreeNode(4)),
                new TreeNode(8, new TreeNode(7), new TreeNode(9)));

        TreeNode lca = lowestCommonAncestor(root, 2, 8);
        System.out.println("INPUT : BST with root 6, finding LCA of 2 and 8");
        System.out.println("OUTPUT: LCA Value = " + (lca != null ? lca.val : "null") + " (Expected 6)");
    }
}
