package com.tus.trafficsimulator.services;

import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.tus.trafficsimulator.models.NetworkMessage;
import com.tus.trafficsimulator.utils.TrafficSimulatorConstants;

/**
 * Sends messages to a Kafka topic.
 */
@Slf4j
@Service
public class KafkaProducerService {

    final KafkaTemplate<String, NetworkMessage> kafkaTemplate;

    /**
     * Constructs a KafkaProducerService instance.
     *
     * @param kafkaTemplate a KafkaTemplate instance.
     */
    public KafkaProducerService(final KafkaTemplate<String, NetworkMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a message to a Kafka topic.
     *
     * @param message the message to send.
     */
    public void sendMessage(final NetworkMessage message) {
        final CompletableFuture<SendResult<String, NetworkMessage>> future = kafkaTemplate
                .send(TrafficSimulatorConstants.NETWORK_MESSAGES_TOPIC, message);
        future.whenComplete((result, exception) -> {
            if (exception == null) {
                log.info("sendMessage() Message sent to Kafka topic: {}. Message: {}. Offset: {}.",
                        TrafficSimulatorConstants.NETWORK_MESSAGES_TOPIC, message, result.getRecordMetadata().offset());
            } else {
                log.error("sendMessage() Error sending message to Kafka topic: {}. Message: {}. Exception: {}.",
                        TrafficSimulatorConstants.NETWORK_MESSAGES_TOPIC, message, exception.getMessage(), exception);
            }
        });
    }
}
