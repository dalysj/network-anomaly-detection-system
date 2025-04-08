package com.tus.anomalydetector.services;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tus.anomalydetector.exceptions.AnomalyDetectorException;
import com.tus.anomalydetector.exceptions.enums.AnomalyDetectorError;
import com.tus.anomalydetector.persistence.documents.NetworkSummary;
import com.tus.anomalydetector.persistence.repositories.NetworkSummaryRepository;

/**
 * Provides network summary services such as retrieving and saving network summaries.
 *
 * <p>
 * This class uses Lombok annotations to generate a logger.
 * </p>
 */
@Slf4j
@Service
public class NetworkSummaryService {

    private final NetworkSummaryRepository networkSummaryRepository;

    /**
     * Constructs a new NetworkSummaryService with the provided network summary repository.
     *
     * @param networkSummaryRepository The repository for network summaries.
     */
    public NetworkSummaryService(final NetworkSummaryRepository networkSummaryRepository) {
        this.networkSummaryRepository = networkSummaryRepository;
    }

    /**
     * Retrieves the network summaries for a given network ID.
     *
     * @param networkId the ID of the network.
     * @return A list of network summaries for the specified network.
     */
    public List<NetworkSummary> getNetworkSummariesByNetworkId(long networkId) {
        log.info("getNetworkSummariesByNetworkId() Retrieving network summaries for the network with ID: {}.", networkId);
        try {
            return this.networkSummaryRepository.findByNetworkId(networkId);
        } catch (final Exception exception) {
            log.error("getNetworkSummariesByNetworkId() An error occurred while getting the network summaries for the network with ID: {}. Exception: {}", networkId, exception.getMessage(), exception);
            throw new AnomalyDetectorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    AnomalyDetectorError.GET_NETWORK_SUMMARIES_ERROR);
        }
    }

    /**
     * Retrieves the network summaries.
     *
     * @return A list of network summaries.
     */
    public List<NetworkSummary> getNetworkSummaries() {
        log.info("getNetworkSummaries() Retrieving network summaries.");
        try {
            return this.networkSummaryRepository.findAll();
        } catch (final Exception exception) {
            log.error("getNetworkSummaries() An error occurred while getting the network summaries. Exception: {}", exception.getMessage(), exception);
            throw new AnomalyDetectorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    AnomalyDetectorError.GET_NETWORK_SUMMARIES_ERROR);
        }
    }

    /**
     * Saves a network summary.
     *
     * @param networkSummary The network summary to be saved.
     */
    public void saveNetworkSummary(final NetworkSummary networkSummary) {
        try {
            final NetworkSummary savedNetworkSummary = this.networkSummaryRepository.save(networkSummary);
            log.debug("saveNetworkSummary() Network summary saved with ID: {}, network ID: {}, traffic size: {} bytes, anomaly count: {}, non-anomaly count: {}. ", networkSummary.getId(),
                    networkSummary.getNetworkId(), networkSummary.getTrafficSizeInBytes(), networkSummary.getAnomalyCount(), networkSummary.getNonAnomalyCount());
        } catch (final Exception exception) {
            log.error("saveNetworkSummary() An error occurred while saving the network summary. Exception: {}", exception.getMessage(), exception);
            throw new AnomalyDetectorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    AnomalyDetectorError.SAVE_NETWORK_SUMMARY_ERROR);
        }
    }
}
