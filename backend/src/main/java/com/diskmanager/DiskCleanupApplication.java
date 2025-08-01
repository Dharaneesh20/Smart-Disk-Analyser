package com.diskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Disk Cleanup and Partition Assistant
 * 
 * @author Dharaneesh
 * @version 1.0.0
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class DiskCleanupApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiskCleanupApplication.class, args);
        System.out.println("\n" +
            "╔════════════════════════════════════════════════════════════╗\n" +
            "║  Disk Cleanup & Partition Assistant API Started!          ║\n" +
            "║  Access at: http://localhost:8080                          ║\n" +
            "║  Swagger UI: http://localhost:8080/swagger-ui.html         ║\n" +
            "╚════════════════════════════════════════════════════════════╝\n");
    }
}
