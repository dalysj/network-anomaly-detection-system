package com.tus.trafficsimulator.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum that represents the errors that can occur in the Traffic Simulator
 * application.
 */
@Getter
@AllArgsConstructor
public enum TrafficSimulatorError {

    NETWORK_NOT_FOUND_ERROR("Network Not Found", "The network was not found."),
    GET_NETWORK_ERROR("Get Network Error", "An error occurred while getting the network."),
    GET_NETWORKS_ERROR("Get Networks Error", "An error occurred while getting the networks."),
    CREATE_NETWORK_ERROR("Create Network Error", "An error occurred while creating the network."),
    UPDATE_NETWORK_ERROR("Update Network Error", "An error occurred while updating the network."),
    DELETE_NETWORK_ERROR("Delete Network Error", "An error occurred while deleting the network."),
    DELETE_NETWORKS_ERROR("Delete Networks Error", "An error occurred while deleting the networks.");

    private final String title;

    private final String detail;
}
