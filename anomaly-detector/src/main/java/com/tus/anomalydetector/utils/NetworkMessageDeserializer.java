package com.tus.anomalydetector.utils;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.tus.anomalydetector.models.NetworkMessage;

/**
 * Custom deserializer for Kafka that converts byte arrays into {@link NetworkMessage} objects.
 * <p>
 * This deserializer uses Jackson's ObjectMapper to deserialize the byte data into a {@link NetworkMessage}
 * object. It also handles Java 8 date-time types by registering the {@link JavaTimeModule} for proper
 * deserialization.
 * </p>
 */
@Slf4j
public class NetworkMessageDeserializer implements Deserializer<NetworkMessage> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Configures the deserializer with the provided settings.
     * <p>
     * This method registers the {@link JavaTimeModule} with the ObjectMapper to handle Java 8 date-time
     * types during deserialization.
     * </p>
     *
     * @param configs A map of configurations for the deserializer.
     * @param isKey   A boolean indicating whether the deserialization is for a key or value.
     */
    @Override
    public void configure(final Map<String, ?> configs, final boolean isKey) {
        Deserializer.super.configure(configs, isKey);
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Deserializes a byte array into a {@link NetworkMessage} object.
     * <p>
     * This method uses Jackson's ObjectMapper to convert the byte array into a {@link NetworkMessage}
     * instance. If an error occurs during deserialization, an exception is logged, and a
     * {@link RuntimeException} is thrown.
     * </p>
     *
     * @param topic The Kafka topic from which the message was consumed.
     * @param data  The byte array representing the serialized message.
     * @return The deserialized {@link NetworkMessage} object.
     */
    @Override
    public NetworkMessage deserialize(final String topic, final byte[] data) {
        try {
            return this.objectMapper.readValue(data, NetworkMessage.class);
        } catch (final Exception exception) {
            log.error("deserialize() An error occurred while deserializing message from topic: {}. Exception: {}", topic, exception.getMessage(), exception);
            throw new RuntimeException(exception);
        }
    }

    /**
     * Closes the deserializer.
     * <p>
     * This method is a no-op in this implementation, as no specific cleanup is required.
     * </p>
     */
    @Override
    public void close() {
        Deserializer.super.close();
    }
}
