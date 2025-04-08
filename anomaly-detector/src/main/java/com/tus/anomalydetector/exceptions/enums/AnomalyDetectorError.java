package com.tus.anomalydetector.exceptions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum that represents the errors that can occur in the Anomaly Detector
 * application.
 */
@Getter
@AllArgsConstructor
public enum AnomalyDetectorError {

    NETWORK_ANOMALY_NOT_FOUND_ERROR("Network Anomaly Not Found", "The network anomaly with ID: %s was not found."),
    GET_NETWORK_ANOMALY_ERROR("Get Network Anomaly Error", "An error occurred while getting the network anomaly with ID: %s."),
    GET_NETWORK_ANOMALIES_ERROR("Get Network Anomalies Error", "An error occurred while getting the network anomalies."),
    GET_NETWORK_SUMMARIES_ERROR("Get Network Summaries Error", "An error occurred while getting the network summaries."),
    SAVE_NETWORK_SUMMARY_ERROR("Save Network Summary Error", "An error occurred while saving the network summary."),
    DELETE_NETWORK_ANOMALY_ERROR("Delete Network Anomaly Error", "An error occurred while deleting the network anomaly with ID: %s."),
    DELETE_NETWORK_ANOMALIES_ERROR("Delete Network Anomalies Error", "An error occurred while deleting the network anomalies.");

    private final String title;

    private final String detail;
}
