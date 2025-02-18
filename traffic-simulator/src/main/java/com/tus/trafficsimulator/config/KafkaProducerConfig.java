package com.tus.trafficsimulator.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/**
 * Configures Kafka producer.
 * 
 * Reads the Kafka bootstrap servers address from application properties
 * and uses it to configure the producer factory.
 */
@Configuration
public class KafkaProducerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers:}")
    private String bootstrapAddress;

    /**
     * Configures a producer factory.
     * 
     * @return a DefaultKafkaProducerFactory instance configured with the bootstrap
     *         servers address.
     */
    @Bean
    ProducerFactory<String, String> producerFactory() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs);
    }

    /**
     * Configures a Kafka template.
     * 
     * @return a KafkaTemplate instance configured with the producer factory.
     */
    @Bean
    KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
