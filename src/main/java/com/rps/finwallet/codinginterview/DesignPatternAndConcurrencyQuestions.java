package com.rps.finwallet.codinginterview;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * ============================================================================
 * ⚙️ TOP DESIGN PATTERNS & CONCURRENCY INTERVIEW QUESTIONS
 * ============================================================================
 * 1. Thread-Safe Singleton Pattern (Double-Checked Locking + volatile)
 * 2. Custom LRU Cache (Least Recently Used Cache using LinkedHashMap)
 * 3. Producer-Consumer Pattern using BlockingQueue
 * 4. Deadlock Explanation & Prevention (Lock Ordering)
 * ============================================================================
 */
public class DesignPatternAndConcurrencyQuestions {

    public void runAll() {
        System.out.println("\n=======================================================");
        System.out.println("⚙️ [INTERVIEW READY] DESIGN PATTERNS & CONCURRENCY");
        System.out.println("=======================================================");
        testSingleton();
        testLruCache();
        testProducerConsumer();
        explainDeadlock();
    }

    // =========================================================================
    // Q1: THREAD-SAFE SINGLETON (Double-Checked Locking)
    // Interviewer: "Thread-safe singleton class banao with maximum performance."
    // Trick:
    // 1. private constructor
    // 2. private static volatile instance (prevents instruction reordering!)
    // 3. Double-checked locking block (synchronized block only on first creation)
    // =========================================================================
    public static class ThreadSafeSingleton {
        // 'volatile' ensures changes made by one thread are immediately visible to all threads
        private static volatile ThreadSafeSingleton instance;

        private ThreadSafeSingleton() {
            // Reflection attack protection
            if (instance != null) {
                throw new RuntimeException("Singleton instance already exists!");
            }
        }

        public static ThreadSafeSingleton getInstance() {
            // First check (bina lock ke - Fast read)
            if (instance == null) {
                synchronized (ThreadSafeSingleton.class) {
                    // Second check (lock ke andar - Thread safe)
                    if (instance == null) {
                        instance = new ThreadSafeSingleton();
                    }
                }
            }
            return instance;
        }
    }

    private void testSingleton() {
        System.out.println("\n--- [Q1] THREAD-SAFE SINGLETON (Double-Checked Locking) ---");
        ThreadSafeSingleton s1 = ThreadSafeSingleton.getInstance();
        ThreadSafeSingleton s2 = ThreadSafeSingleton.getInstance();
        System.out.println("Instance 1 HashCode: " + s1.hashCode());
        System.out.println("Instance 2 HashCode: " + s2.hashCode());
        System.out.println("Are both instances exact same? " + (s1 == s2));
    }

    // =========================================================================
    // Q2: LRU CACHE (Least Recently Used Cache)
    // Interviewer Favorite (Asked in Google, Amazon, Microsoft):
    // "Ek cache banao jo capacity exceed hone par sabse purane (least accessed)
    // element ko nikaal de."
    // Solution: LinkedHashMap with accessOrder = true and removeEldestEntry()
    // Time: O(1) for get() and put()
    // =========================================================================
    public static class LruCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;

        public LruCache(int capacity) {
            // initialCapacity, loadFactor, accessOrder = true (access order maintain karega)
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            // Agar size capacity se bada hua to sabse purana element auto-remove hoga!
            return size() > capacity;
        }
    }

    private void testLruCache() {
        System.out.println("\n--- [Q2] LRU CACHE (Capacity = 3) ---");
        LruCache<String, String> cache = new LruCache<>(3);

        cache.put("user1", "Ramesh");
        cache.put("user2", "Amit");
        cache.put("user3", "Priya");
        System.out.println("Cache after 3 entries: " + cache);

        // Access "user1" -> Ab user1 most recently used ban gaya!
        cache.get("user1");
        System.out.println("Accessed 'user1'. Cache Order: " + cache);

        // Put 4th element -> "user2" will be evicted because it was least recently used!
        cache.put("user4", "Sneha");
        System.out.println("Added 'user4'. Evicted least used ('user2'): " + cache);
    }

    // =========================================================================
    // Q3: PRODUCER - CONSUMER PATTERN USING BLOCKINGQUEUE
    // Interviewer: "Multithreading me Producer aur Consumer ke beech data
    // safely transfer kaise karoge bina manual wait/notify ke?"
    // Solution: java.util.concurrent.BlockingQueue (Thread-safe, handles waiting automatically)
    // =========================================================================
    private void testProducerConsumer() {
        System.out.println("\n--- [Q3] PRODUCER - CONSUMER PATTERN (BlockingQueue) ---");
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(2); // Capacity = 2

        try {
            // Producer pushes
            queue.put("Transaction #TXN-101");
            queue.put("Transaction #TXN-102");
            System.out.println("Produced 2 items in Queue: " + queue);

            // Consumer pulls
            String item1 = queue.take();
            System.out.println("Consumed: " + item1 + " | Remaining in Queue: " + queue);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // =========================================================================
    // Q4: DEADLOCK EXPLANATION & PREVENTION
    // Interviewer: "Deadlock kab hota hai aur ise prevent kaise karein?"
    // =========================================================================
    private void explainDeadlock() {
        System.out.println("\n--- [Q4] DEADLOCK & HOW TO PREVENT IT ---");
        System.out.println("⚠️ Deadlock Condition:");
        System.out.println("   Thread A ne Lock 1 liya aur Lock 2 ka wait kar raha hai.");
        System.out.println("   Thread B ne Lock 2 liya aur Lock 1 ka wait kar raha hai.");
        System.out.println("   Dono ek dusre ke chhodne ka intezaar karte hain -> Application HANGS!\n");

        System.out.println("💡 3 Golden Rules to Prevent Deadlock:");
        System.out.println("   1. Lock Ordering (Hamesha same order me locks acquire karo: Lock1 then Lock2).");
        System.out.println("   2. Lock Timeout: 'tryLock(5, TimeUnit.SECONDS)' use karo instead of synchronized.");
        System.out.println("   3. Minimize Lock Scope (Sirf critical code line ko lock karo, poora method nahi).");
    }
}
