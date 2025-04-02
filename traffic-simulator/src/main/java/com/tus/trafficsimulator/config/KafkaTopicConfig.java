package com.tus.trafficsimulator.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import com.tus.trafficsimulator.utils.TrafficSimulatorConstants;

/**
 * Configures Kafka topics and KafkaAdmin.
 * <p>
 * Reads the Kafka bootstrap servers address from application properties
 * and uses it to configure the KafkaAdmin bean.
 * <p>
 * Defines a Kafka topic named "network-messages" with a single partition
 * and a replication factor of 1.
 */
@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers:}")
    private String bootstrapAddress;

    /**
     * Configures KafkaAdmin bean.
     *
     * @return a KafkaAdmin instance configured with the bootstrap servers address.
     */
    @Bean
    KafkaAdmin kafkaAdmin() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    /**
     * Defines a Kafka topic named "network-messages".
     *
     * @return a NewTopic instance with a single partition and a replication factor
     * of 1.
     */
    @Bean
    NewTopic networkMessagesTopic() {
        return new NewTopic(TrafficSimulatorConstants.NETWORK_MESSAGES_TOPIC, 1, (short) 1);
    }
}
