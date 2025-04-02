package com.tus.anomalydetector.utils;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.tus.anomalydetector.models.NetworkMessage;

/**
 * Custom serializer for Kafka that converts {@link NetworkMessage} objects to byte arrays.
 * <p>
 * This serializer uses Jackson's ObjectMapper to serialize {@link NetworkMessage} objects to byte arrays.
 * It also handles Java 8 date-time types by registering the {@link JavaTimeModule} for proper serialization.
 * </p>
 */
@Slf4j
public class NetworkMessageSerializer implements Serializer<NetworkMessage> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Configures the serializer with the provided settings.
     * <p>
     * This method registers the {@link JavaTimeModule} with the ObjectMapper to handle Java 8 date-time types
     * during serialization.
     * </p>
     *
     * @param configs A map of configurations for the serializer.
     * @param isKey   A boolean indicating whether the serialization is for a key or value.
     */
    @Override
    public void configure(final Map<String, ?> configs, final boolean isKey) {
        Serializer.super.configure(configs, isKey);
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Serializes a {@link NetworkMessage} object into a byte array.
     * <p>
     * This method uses Jackson's ObjectMapper to convert the {@link NetworkMessage} object into a byte array.
     * If the object is null, it returns null. If an error occurs during serialization, an exception is logged,
     * and a {@link RuntimeException} is thrown.
     * </p>
     *
     * @param topic The Kafka topic to which the message is being serialized.
     * @param data  The {@link NetworkMessage} object to serialize.
     * @return The byte array representing the serialized message.
     */
    @Override
    public byte[] serialize(final String topic, final NetworkMessage data) {
        try {
            if (data == null) {
                return null;
            }

            return this.objectMapper.writeValueAsBytes(data);
        } catch (final Exception exception) {
            log.error("serialize() An error occurred while serializing message from topic: {}. Exception: {}", topic, exception.getMessage(), exception);
            throw new RuntimeException(exception);
        }
    }

    /**
     * Closes the serializer.
     * <p>
     * This method is a no-op in this implementation, as no specific cleanup is required.
     * </p>
     */
    @Override
    public void close() {
        Serializer.super.close();
    }
}
