package com.tus.trafficsimulator.models;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a network message entity with an ID, network ID, size in bytes,
 * timestamp, and anomaly status.
 * 
 * <p>
 * This class uses Lombok annotations to generate boilerplate code such as
 * getters, setters,
 * constructors, and a builder pattern implementation.
 * </p>
 * 
 * <p>
 * Fields:
 * </p>
 * <ul>
 * <li>{@code id} - Unique identifier for the network message.</li>
 * <li>{@code networkId} - Unique identifier for the network.</li>
 * <li>{@code sizeInBytes} - Size of the network message in bytes.</li>
 * <li>{@code timestamp} - Timestamp of the network message.</li>
 * </ul>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NetworkMessage {

    private UUID id;

    private long networkId;

    private double sizeInBytes;

    private Instant timestamp;

    /**
     * Returns a string representation of the network message.
     * 
     * @return String representation of the network message.
     */
    @Override
    public String toString() {
        return String.format("NetworkMessage{id=%s, networkId=%d, sizeInBytes=%.2f, timestamp=%s}", id, networkId,
                sizeInBytes, timestamp);
    }
}
