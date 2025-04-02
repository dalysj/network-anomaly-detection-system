package com.tus.anomalydetector.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import com.tus.anomalydetector.utils.NetworkMessageSerde;

/**
 * Configuration class for setting up Kafka Streams.
 * <p>
 * This class defines the necessary Kafka Streams configuration
 * properties such as application ID, bootstrap servers, the number
 * of stream threads, and the replication factor. It also specifies
 * the default key and value serializers/deserializers (Serdes) used
 * by Kafka Streams.
 * </p>
 */
@Configuration
public class KafkaStreamsConfig {

    @Value("${spring.application.name}")
    private String applicationId;

    @Value("${spring.kafka.streams.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${spring.kafka.streams.num-stream-threads}")
    private int streamThreads;

    @Value("${spring.kafka.streams.replication-factor}")
    private int replicationFactor;

    /**
     * Creates a KafkaStreamsConfiguration bean with the necessary
     * properties to configure Kafka Streams.
     *
     * @return the KafkaStreamsConfiguration bean
     */
    @Bean
    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        final Map<String, Object> configs = new HashMap<>();

        configs.put(StreamsConfig.APPLICATION_ID_CONFIG, this.applicationId);
        configs.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapAddress);
        configs.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        configs.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, NetworkMessageSerde.class.getName());
        configs.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, this.streamThreads);
        configs.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, this.replicationFactor);

        return new KafkaStreamsConfiguration(configs);
    }
}
