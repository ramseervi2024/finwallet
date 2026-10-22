package com.rps.finwallet.xlearning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Automatically executes the JPA Complex Queries when the Spring Boot
 * application starts up, allowing you to see actual queries and results
 * in the terminal console.
 */
@Component
@Order(10) // Runs after standard startup beans
public class JpaLearningRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(JpaLearningRunner.class);

    private final JpaComplexQueryService queryService;

    public JpaLearningRunner(JpaComplexQueryService queryService) {
        this.queryService = queryService;
    }

    @Override
    public void run(String... args) {
        try {
            queryService.runAllDatabaseJoinExamples();
        } catch (Exception e) {
            log.warn("[XLEARNING] Could not execute JPA Database queries on startup: {}", e.getMessage());
            System.out.println("ℹ️ [XLEARNING HINT] If MySQL is not running or tables are empty, start MySQL to see live query results.");
        }
    }
}
