package org.br.com.core.support;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the context for the entire test execution.
 * It uses ThreadLocal for scenario-specific data to ensure thread safety,
 * and thread-safe collections for run-wide data like counters and feature IDs.
 */
public class Context {

    // For scenario-specific data, isolated per thread
    private static final ThreadLocal<Map<String, Object>> scenarioContext = ThreadLocal.withInitial(HashMap::new);

    // For data that spans the entire test run (shared across threads)
    private static final Map<String, String> featureUUIDs = new ConcurrentHashMap<>();
    private static final AtomicInteger passedTests = new AtomicInteger(0);
    private static final AtomicInteger failedTests = new AtomicInteger(0);

    // --- Context Lifecycle Methods ---

    public static void startContext(String featureName, String scenarioName) {
        // This method can be expanded for more detailed logging or setup if needed.
        // The main context setup is handled by ThreadLocal's lazy initialization.
    }

    public static void clearContext() {
        // Clears the map for the current thread to prevent data leakage between scenarios.
        // This is crucial and should be called in an @After hook.
        scenarioContext.remove();
    }

    public static void finishedContext(long duration) {
        // Placeholder for any end-of-run logic, like generating a final summary report.
        System.out.println("==========================================");
        System.out.println("Test Run Finished.");
        System.out.println("Duration: " + duration + " ms");
        System.out.println("Passed: " + passedTests.get());
        System.out.println("Failed: " + failedTests.get());
        System.out.println("==========================================");
    }

    // --- Counter Methods ---

    public static void resetCounters() {
        passedTests.set(0);
        failedTests.set(0);
        featureUUIDs.clear();
    }

    public static void incrementPassed() {
        passedTests.incrementAndGet();
    }

    public static void incrementFailed() {
        failedTests.incrementAndGet();
    }

    // --- UUID and ID Management ---

    public static String getOrCreateFeatureUUID(String featureName) {
        // Atomically computes the value if the key is not present, making it thread-safe.
        return featureUUIDs.computeIfAbsent(featureName, k -> UUID.randomUUID().toString());
    }

    public static void setScenarioId(String id) {
        put("scenarioId", id);
    }

    // --- Generic Data Management ---

    public static void put(String key, Object value) {
        scenarioContext.get().put(key, value);
    }

    public static Object get(String key) {
        return scenarioContext.get().get(key);
    }

    // --- Specific Data Accessors (Convenience Methods) ---

    public static void setData(Object data) {
        put("data", data);
    }

    public static Object getData() {
        return get("data");
    }

    public static void setIdUsuario(String id) {
        put("idUsuario", id);
    }

    public static String getIdUsuario() {
        return (String) get("idUsuario");
    }
}