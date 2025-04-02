package com.tus.anomalydetector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The AnomalyDetectorApplication class is the main class for the Anomaly
 * Detector application.
 * It is a Spring Boot application, and it uses the @SpringBootApplication
 * annotation to enable autoconfiguration and component scanning.
 */
@SpringBootApplication
public class AnomalyDetectorApplication {

    /**
     * The main method serves as the entry point for the Anomaly Detector
     * application.
     * It uses Spring Boot's SpringApplication.run() method to launch the
     * application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(AnomalyDetectorApplication.class, args);
    }
}
