package com.tus.trafficsimulator.persistence.entities;

import com.tus.trafficsimulator.persistence.enums.NetworkStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

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
 * <li>{@code status} - Status of the network.</li>
 * </ul>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Network {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String location;

    @NotNull
    private NetworkStatus status;
}
