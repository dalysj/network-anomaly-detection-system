package com.tus.trafficsimulator.persistence.entities;

import com.tus.trafficsimulator.persistence.enums.NetworkStatus;
import com.tus.trafficsimulator.utils.TrafficSimulatorConstants;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

/**
 * Represents a network in the Traffic Simulator application.
 */
@Entity
@Table(name = TrafficSimulatorConstants.NETWORKS_TABLE, schema = TrafficSimulatorConstants.TRAFFIC_SIMULATOR_SCHEMA)
@Data
public class Network {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String location;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NetworkStatus status;
}
