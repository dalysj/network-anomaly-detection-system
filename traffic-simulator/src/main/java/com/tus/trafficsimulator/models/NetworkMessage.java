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
 * <li>{@code isAnomaly} - Anomaly status of the network message.</li>
 * </ul>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NetworkMessage {

    private UUID id;

    private UUID networkId;

    private double sizeInBytes;

    private Instant timestamp;

    private boolean isAnomaly;
}
