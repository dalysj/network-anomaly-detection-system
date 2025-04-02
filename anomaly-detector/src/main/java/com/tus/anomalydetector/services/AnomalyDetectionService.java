package com.tus.anomalydetector.services;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.stereotype.Service;

import com.tus.anomalydetector.models.NetworkMessage;
import com.tus.anomalydetector.utils.AnomalyDetectorConstants;

/**
 * Service for detecting anomalies by processing incoming network messages
 * through Kafka Streams.
 * <p>
 * This service sets up and runs a Kafka Streams application that listens
 * for messages from a Kafka topic, processes them, and logs the messages
 * for further anomaly detection processing.
 * </p>
 */
@Slf4j
@Service
public class AnomalyDetectionService {

    final KafkaStreamsConfiguration kafkaStreamsConfiguration;

    /**
     * Constructor for the AnomalyDetectionService.
     *
     * @param kafkaStreamsConfiguration The KafkaStreamsConfiguration used
     *                                  to configure the Kafka Streams instance.
     */
    public AnomalyDetectionService(final KafkaStreamsConfiguration kafkaStreamsConfiguration) {
        this.kafkaStreamsConfiguration = kafkaStreamsConfiguration;
    }

    /**
     * Initializes the Kafka Streams application.
     * <p>
     * This method is called after the bean has been constructed and initializes
     * the Kafka Streams processing by starting the KafkaStreams instance.
     * It also adds a shutdown hook to cleanly close the KafkaStreams instance
     * when the application shuts down.
     * </p>
     */
    @PostConstruct
    public void init() {
        try (final KafkaStreams kafkaStreams = this.buildKStreams()) {
            kafkaStreams.start();
            Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        }
    }

    private KafkaStreams buildKStreams() {
        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        final KStream<String, NetworkMessage> kStream = streamsBuilder.stream(AnomalyDetectorConstants.NETWORK_MESSAGES_TOPIC);

        kStream.foreach((key, value) -> log.info("buildKStreams() Message received from Kafka topic: {}. Message: {}.",
                AnomalyDetectorConstants.NETWORK_MESSAGES_TOPIC, value.toString()));

        return new KafkaStreams(streamsBuilder.build(), this.kafkaStreamsConfiguration.asProperties());
    }
}
