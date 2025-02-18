package com.tus.trafficsimulator.utils;

/**
 * Contains constants used in the Traffic Simulator application.
 */
public class TrafficSimulatorConstants {

    private TrafficSimulatorConstants() {
        // Private constructor to prevent instantiation.
    }

    public static final String NETWORK_CONTROLLER_URL = "/v1/networks";

    public static final String TRAFFIC_SIMULATOR_SCHEMA = "traffic_simulator";

    public static final String NETWORKS_TABLE = "networks";

    public static final String NETWORK_MESSAGES_TOPIC = "network-messages";
}
