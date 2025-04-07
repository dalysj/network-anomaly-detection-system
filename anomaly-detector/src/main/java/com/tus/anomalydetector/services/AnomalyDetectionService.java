package com.tus.anomalydetector.services;

import java.time.Instant;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.stereotype.Service;

import com.tus.anomalydetector.models.NetworkMessage;
import com.tus.anomalydetector.persistence.documents.NetworkAnomaly;
import com.tus.anomalydetector.persistence.documents.NetworkSummary;
import com.tus.anomalydetector.persistence.repositories.NetworkAnomalyRepository;
import com.tus.anomalydetector.persistence.repositories.NetworkStatisticsRepository;
import com.tus.anomalydetector.persistence.repositories.NetworkSummaryRepository;

/**
 * Service for detecting anomalies by processing incoming network messages
 * through Kafka Streams.
 *
 * <p>
 * This service sets up and runs a Kafka Streams application that listens
 * for network traffic data, analyzes it for anomalies based on predefined
 * volume and statistical thresholds, and records detected anomalies and traffic summaries.
 * </p>
 */
@Slf4j
@Service
public class AnomalyDetectionService {

    private static final String NETWORK_MESSAGES_TOPIC = "network-messages";

    private static final double NETWORK_TRAFFIC_VOLUME_THRESHOLD = 800;

    private static final double NETWORK_TRAFFIC_STD_DEV_THRESHOLD = 2.0;

    private final KafkaStreamsConfiguration kafkaStreamsConfiguration;

    private final NetworkStatisticsRepository networkStatisticsRepository;

    private final NetworkSummaryRepository networkSummaryRepository;

    private final NetworkAnomalyRepository networkAnomalyRepository;

    /**
     * Constructs an {@code AnomalyDetectionService} with the required dependencies.
     *
     * @param kafkaStreamsConfiguration   the configuration for Kafka Streams
     * @param networkStatisticsRepository the repository to store and retrieve network statistics
     * @param networkSummaryRepository    the repository to store and retrieve network traffic summaries
     * @param networkAnomalyRepository    the repository to store detected network anomalies
     */
    public AnomalyDetectionService(final KafkaStreamsConfiguration kafkaStreamsConfiguration,
                                   final NetworkStatisticsRepository networkStatisticsRepository,
                                   final NetworkSummaryRepository networkSummaryRepository,
                                   final NetworkAnomalyRepository networkAnomalyRepository) {
        this.kafkaStreamsConfiguration = kafkaStreamsConfiguration;
        this.networkStatisticsRepository = networkStatisticsRepository;
        this.networkSummaryRepository = networkSummaryRepository;
        this.networkAnomalyRepository = networkAnomalyRepository;
    }

    /**
     * Initializes and starts the Kafka Streams application.
     *
     * <p>
     * This method is invoked after the bean has been constructed.
     * It builds the Kafka Streams topology, starts streaming, and registers a shutdown hook
     * to gracefully close the streams when the application terminates.
     * </p>
     */
    @PostConstruct
    public void init() {
        final KafkaStreams kafkaStreams = this.buildKStreams();
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }

    /**
     * Builds the Kafka Streams topology to process incoming network messages.
     *
     * <p>
     * This method defines the stream processing logic, including:
     * <ul>
     *   <li>Logging received messages</li>
     *   <li>Saving traffic statistics</li>
     *   <li>Detecting anomalies based on volume and standard deviation thresholds</li>
     *   <li>Updating network traffic summaries</li>
     * </ul>
     * </p>
     *
     * @return a configured {@link KafkaStreams} instance
     */
    private KafkaStreams buildKStreams() {
        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        final KStream<String, NetworkMessage> kStream = streamsBuilder.stream(NETWORK_MESSAGES_TOPIC);

        kStream.foreach((key, networkMessage) -> {
            log.info("buildKStreams() Message received from Kafka topic: {}. Message: {}.",
                    NETWORK_MESSAGES_TOPIC, networkMessage.toString());
            this.networkStatisticsRepository.save(networkMessage);

            final NetworkSummary networkSummary = this.networkSummaryRepository.findByNetworkId(networkMessage.getNetworkId())
                    .orElse(NetworkSummary.builder().networkId(networkMessage.getNetworkId()).trafficSizeInBytes(0)
                            .anomalyCount(0).nonAnomalyCount(0).build());

            networkSummary.setTrafficSizeInBytes(networkSummary.getTrafficSizeInBytes() + networkMessage.getSizeInBytes());
            networkSummary.setLastUpdatedAt(Instant.now());

            if (networkMessage.getSizeInBytes() > NETWORK_TRAFFIC_VOLUME_THRESHOLD) {
                log.info("buildKStreams() Volume threshold check - Message size: {} bytes exceeds threshold: {} bytes. Message: {}.",
                        networkMessage.getSizeInBytes(), NETWORK_TRAFFIC_VOLUME_THRESHOLD, networkMessage);
                this.networkAnomalyRepository.save(
                        NetworkAnomaly.builder().networkId(networkMessage.getNetworkId()).sizeInBytes(networkMessage.getSizeInBytes()).timestamp(networkMessage.getTimestamp()).build()
                );
                networkSummary.setAnomalyCount(networkSummary.getAnomalyCount() + 1);
                this.networkSummaryRepository.save(networkSummary);
                return;
            }

            final double mean = this.networkStatisticsRepository.findMean(networkMessage.getNetworkId());
            final double standardDeviation = this.networkStatisticsRepository.findStandardDeviation(networkMessage.getNetworkId());

            if (Math.abs(networkMessage.getSizeInBytes() - mean) >
                    standardDeviation * NETWORK_TRAFFIC_STD_DEV_THRESHOLD) {
                log.info("buildKStreams() Std dev threshold check - Message size: {} bytes, Mean: {}, Std Dev: {}, Threshold Multiplier: {}, Message: {}.",
                        networkMessage.getSizeInBytes(), mean, standardDeviation, NETWORK_TRAFFIC_STD_DEV_THRESHOLD, networkMessage);
                this.networkAnomalyRepository.save(
                        NetworkAnomaly.builder().networkId(networkMessage.getNetworkId()).sizeInBytes(networkMessage.getSizeInBytes()).timestamp(networkMessage.getTimestamp()).build()
                );
                networkSummary.setAnomalyCount(networkSummary.getAnomalyCount() + 1);
                this.networkSummaryRepository.save(networkSummary);
                return;
            }

            networkSummary.setNonAnomalyCount(networkSummary.getNonAnomalyCount() + 1);
            this.networkSummaryRepository.save(networkSummary);
        });

        return new KafkaStreams(streamsBuilder.build(), this.kafkaStreamsConfiguration.asProperties());
    }
}
