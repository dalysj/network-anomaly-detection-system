package com.tus.anomalydetector.services;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tus.anomalydetector.exceptions.AnomalyDetectorException;
import com.tus.anomalydetector.exceptions.enums.AnomalyDetectorError;
import com.tus.anomalydetector.persistence.documents.NetworkAnomaly;
import com.tus.anomalydetector.persistence.repositories.NetworkAnomalyRepository;

/**
 * Provides network anomaly services such as retrieving and deleting network anomalies.
 *
 * <p>
 * This class uses Lombok annotations to generate a logger.
 * </p>
 */
@Slf4j
@Service
public class NetworkAnomalyService {

    private static final String TIMESTAMP_FIELD = "timestamp";

    private final NetworkAnomalyRepository networkAnomalyRepository;

    /**
     * Constructs a new NetworkAnomalyService with the provided network anomaly repository.
     *
     * @param networkAnomalyRepository The repository for network anomalies.
     */
    public NetworkAnomalyService(final NetworkAnomalyRepository networkAnomalyRepository) {
        this.networkAnomalyRepository = networkAnomalyRepository;
    }

    /**
     * Retrieves a network anomaly by its ID.
     *
     * @param id The ID of the network anomaly to retrieve.
     * @return The network anomaly with the provided ID.
     */
    public NetworkAnomaly getNetworkAnomalyById(final String id) {
        final Optional<NetworkAnomaly> networkAnomalyOptional;
        try {
            networkAnomalyOptional = this.networkAnomalyRepository.findById(id);
        } catch (final Exception exception) {
            log.error("getNetworkAnomalyById() An error occurred while getting the network anomaly with ID: {}. Exception: {}",
                    id,
                    exception.getMessage(), exception);
            final String[] errorDetailArgs = {id};
            throw new AnomalyDetectorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    AnomalyDetectorError.GET_NETWORK_ANOMALY_ERROR, errorDetailArgs);
        }

        if (networkAnomalyOptional.isEmpty()) {
            log.error("getNetworkAnomalyById() Network anomaly with ID: {} not found.", id);
            final String[] errorDetailArgs = {id};
            throw new AnomalyDetectorException(HttpStatus.NOT_FOUND,
                    AnomalyDetectorError.NETWORK_ANOMALY_NOT_FOUND_ERROR,
                    errorDetailArgs);
        }

        return networkAnomalyOptional.get();
    }

    /**
     * Retrieves the 100 most recent network anomalies for a given network ID, sorted by timestamp descending.
     *
     * @param networkId the ID of the network.
     * @return A list of the latest network anomalies for the specified network.
     */
    public List<NetworkAnomaly> getNetworkAnomaliesByNetworkId(long networkId) {
        log.info("getNetworkAnomaliesByNetworkId() Retrieving network anomalies for the network with ID: {}.", networkId);
        try {
            return this.networkAnomalyRepository.findByNetworkId(
                    networkId,
                    PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, TIMESTAMP_FIELD))
            ).getContent();
        } catch (final Exception exception) {
            log.error("getNetworkAnomaliesByNetworkId() An error occurred while getting the network anomalies for the network with ID: {}. Exception: {}", networkId, exception.getMessage(), exception);
            throw new AnomalyDetectorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    AnomalyDetectorError.GET_NETWORK_ANOMALIES_ERROR);
        }
    }

    /**
     * Retrieves the 100 most recent network anomalies, sorted by timestamp descending.
     *
     * @return A list of the latest network anomalies.
     */
    public List<NetworkAnomaly> getNetworkAnomalies() {
        log.info("getNetworkAnomalies() Retrieving network anomalies.");
        try {
            return this.networkAnomalyRepository.findAll(
                    PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, TIMESTAMP_FIELD))
            ).getContent();
        } catch (final Exception exception) {
            log.error("getNetworkAnomalies() An error occurred while getting the network anomalies. Exception: {}", exception.getMessage(), exception);
            throw new AnomalyDetectorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    AnomalyDetectorError.GET_NETWORK_ANOMALIES_ERROR);
        }
    }

    /**
     * Deletes a network anomaly by its ID.
     *
     * @param id The ID of the network anomaly to delete.
     */
    public void deleteNetworkAnomalyById(final String id) {
        final Optional<NetworkAnomaly> networkAnomalyOptional;
        try {
            networkAnomalyOptional = this.networkAnomalyRepository.findById(id);
        } catch (final Exception exception) {
            log.error("deleteNetworkAnomalyById() An error occurred while getting the network anomaly with ID: {}. Exception: {}", id,
                    exception.getMessage(), exception);
            final String[] errorDetailArgs = {id};
            throw new AnomalyDetectorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    AnomalyDetectorError.GET_NETWORK_ANOMALY_ERROR, errorDetailArgs);
        }

        if (networkAnomalyOptional.isEmpty()) {
            log.info("deleteNetworkAnomalyById() Network anomaly with ID: {} not found.", id);
            final String[] errorDetailArgs = {id};
            throw new AnomalyDetectorException(HttpStatus.NOT_FOUND,
                    AnomalyDetectorError.NETWORK_ANOMALY_NOT_FOUND_ERROR,
                    errorDetailArgs);
        }

        try {
            log.info("deleteNetworkAnomalyById() Deleting network anomaly with ID: {}.", id);
            this.networkAnomalyRepository.deleteById(id);
        } catch (final Exception exception) {
            log.error("deleteNetworkAnomalyById() An error occurred while deleting the network anomaly with ID: {}. Exception: {}", id,
                    exception.getMessage(), exception);
            final String[] errorDetailArgs = {id};
            throw new AnomalyDetectorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    AnomalyDetectorError.DELETE_NETWORK_ANOMALY_ERROR, errorDetailArgs);
        }
    }

    /**
     * Deletes the network anomalies for a given network ID.
     *
     * @param networkId the ID of the network.
     */
    public void deleteNetworkAnomaliesByNetworkId(long networkId) {
        log.info("deleteNetworkAnomaliesByNetworkId() Deleting network anomalies for the network with ID: {}.", networkId);
        try {
            this.networkAnomalyRepository.deleteByNetworkId(networkId);
        } catch (final Exception exception) {
            log.error("deleteNetworkAnomaliesByNetworkId() An error occurred while deleting the network anomalies for the network with ID: {}. Exception: {}", networkId, exception.getMessage(), exception);
            throw new AnomalyDetectorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    AnomalyDetectorError.DELETE_NETWORK_ANOMALIES_ERROR);
        }
    }

    /**
     * Delete the network anomalies.
     */
    public void deleteNetworkAnomalies() {
        log.info("deleteNetworkAnomalies() Deleting network anomalies.");
        try {
            this.networkAnomalyRepository.deleteAll();
        } catch (final Exception exception) {
            log.error("deleteNetworkAnomalies() An error occurred while deleting the network anomalies. Exception: {}", exception.getMessage(), exception);
            throw new AnomalyDetectorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    AnomalyDetectorError.DELETE_NETWORK_ANOMALIES_ERROR);
        }
    }
}
