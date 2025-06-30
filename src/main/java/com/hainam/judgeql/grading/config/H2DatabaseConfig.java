package com.hainam.judgeql.grading.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for H2 database settings in the grading module
 */
@Configuration
public class H2DatabaseConfig {
    
    /**
     * Maximum time in seconds that a SQL query is allowed to run in the H2 database
     * This prevents long-running queries from affecting system performance
     */
    @Bean
    public int h2QueryTimeoutSeconds() {
        return 30; // 30 seconds timeout
    }
    
    /**
     * Maximum number of connection retry attempts for H2 database
     */
    @Bean
    public int h2MaxConnectionRetries() {
        return 3;
    }
    
    /**
     * Delay between connection retry attempts in milliseconds
     */
    @Bean
    public long h2RetryDelayMs() {
        return 1000; // 1 second
    }
}
