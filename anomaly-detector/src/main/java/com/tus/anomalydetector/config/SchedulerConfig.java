package com.tus.anomalydetector.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration class for enabling scheduling in the Spring application context.
 *
 * <p>
 * This class is annotated with {@link Configuration} and {@link EnableScheduling}, indicating that it provides Spring configuration and enables
 * support for scheduled tasks.
 * </p>
 *
 * <p>
 * The {@link ConditionalOnProperty} annotation ensures that this configuration is only active when the property
 * {@code spring.task.scheduler.enabled} is set to {@code true} or is missing (defaults to {@code true}).
 * </p>
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "spring.task.scheduler.enabled", havingValue = "true", matchIfMissing = true)
public class SchedulerConfig {
    // No implementation.
}
