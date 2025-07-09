package com.neighborfit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for NeighborFit - Neighborhood Lifestyle Matching Application
 * 
 * This application solves the neighborhood-lifestyle matching problem through:
 * - Systematic research and data analysis
 * - Algorithmic matching based on user preferences and neighborhood characteristics
 * - Integration with external data sources for real neighborhood data
 * 
 * @author NeighborFit Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class NeighborFitApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeighborFitApplication.class, args);
        System.out.println("🚀 NeighborFit Application Started Successfully!");
        System.out.println("📍 Access the application at: http://localhost:8095/api");
        System.out.println("📊 H2 Database Console: http://localhost:8095/api/h2-console");
    }
} 