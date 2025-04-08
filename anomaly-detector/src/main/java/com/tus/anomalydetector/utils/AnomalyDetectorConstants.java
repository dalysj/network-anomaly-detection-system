package com.tus.anomalydetector.utils;

/**
 * Contains constants used in the Anomaly Detector application.
 */
public class AnomalyDetectorConstants {

    private AnomalyDetectorConstants() {
        // Private constructor to prevent instantiation.
    }

    public static final String NETWORK_ANOMALY_CONTROLLER_URL = "/v1/network-anomalies";

    public static final String NETWORK_SUMMARY_CONTROLLER_URL = "/v1/network-summaries";

    public static final String NETWORK_SUMMARIES_COLLECTION = "network_summaries";

    public static final String NETWORK_ANOMALIES_COLLECTION = "network_anomalies";
}
