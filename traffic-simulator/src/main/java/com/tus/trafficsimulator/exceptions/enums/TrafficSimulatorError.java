package com.tus.trafficsimulator.exceptions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum that represents the errors that can occur in the Traffic Simulator
 * application.
 */
@Getter
@AllArgsConstructor
public enum TrafficSimulatorError {

    NETWORK_NOT_FOUND_ERROR("Network Not Found", "The network with ID: %s was not found."),
    GET_NETWORK_ERROR("Get Network Error", "An error occurred while getting the network with ID: %s."),
    GET_NETWORKS_ERROR("Get Networks Error", "An error occurred while getting the networks."),
    CREATE_NETWORK_ERROR("Create Network Error", "An error occurred while creating the network."),
    ACTIVATION_MANAGEMENT_ERROR("Update Network Status Error",
            "Network with ID: %s must have status: %s to %s the network."),
    UPDATE_NETWORK_STATUS_ERROR("Update Network Status Error",
            "An error occurred while updating the status of the network with ID: %s."),
    DELETE_NETWORK_ERROR("Delete Network Error", "An error occurred while deleting the network with ID: %s.");

    private final String title;

    private final String detail;
}
