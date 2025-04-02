package com.tus.trafficsimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The TrafficSimulatorApplication class is the main class for the Traffic
 * Simulator application.
 * It is a Spring Boot application, and it uses the @SpringBootApplication
 * annotation to enable autoconfiguration and component scanning.
 */
@SpringBootApplication
public class TrafficSimulatorApplication {

    /**
     * The main method serves as the entry point for the Traffic Simulator
     * application.
     * It uses Spring Boot's SpringApplication.run() method to launch the
     * application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(TrafficSimulatorApplication.class, args);
    }
}
