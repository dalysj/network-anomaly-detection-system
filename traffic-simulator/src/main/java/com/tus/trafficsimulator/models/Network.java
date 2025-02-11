package com.tus.trafficsimulator.models;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a network entity with an ID, name, location, and activation
 * status.
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
 * <li>{@code id} - Unique identifier for the network.</li>
 * <li>{@code name} - Name of the network.</li>
 * <li>{@code location} - Location of the network.</li>
 * <li>{@code isActivated} - Activation status of the network.</li>
 * </ul>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Network {

    private UUID id;

    private String name;

    private String location;

    private boolean isActivated;
}
