package com.tus.anomalydetector.controllers;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tus.anomalydetector.persistence.documents.NetworkAnomaly;
import com.tus.anomalydetector.services.NetworkAnomalyService;
import com.tus.anomalydetector.utils.AnomalyDetectorConstants;

/**
 * Controller responsible for handling requests related to network anomaly operations,
 * such as retrieving and deleting network anomalies.
 * <p>
 * The controller interacts with the {@link NetworkAnomalyService} to perform the necessary
 * actions and returns appropriate HTTP responses.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping(AnomalyDetectorConstants.NETWORK_ANOMALY_CONTROLLER_URL)
@CrossOrigin
public class NetworkAnomalyController {

    private final NetworkAnomalyService networkAnomalyService;

    /**
     * Constructs a new NetworkAnomalyController with the provided NetworkAnomalyService.
     *
     * @param networkAnomalyService The service to use for network anomaly operations.
     */
    public NetworkAnomalyController(final NetworkAnomalyService networkAnomalyService) {
        this.networkAnomalyService = networkAnomalyService;
    }

    /**
     * Retrieves a network anomaly by its ID.
     *
     * @param id The ID of the network anomaly to retrieve.
     * @return The network anomaly with the provided ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NetworkAnomaly> getNetworkAnomalyById(@PathVariable final String id) {
        log.info("getNetworkAnomalyById() Retrieving network anomaly with ID: {}.", id);
        final NetworkAnomaly networkAnomaly = this.networkAnomalyService.getNetworkAnomalyById(id);
        return ResponseEntity.ok(networkAnomaly);
    }

    /**
     * Retrieves network anomalies for a given network ID.
     *
     * @param networkId the ID of the network.
     * @return A list of the latest network anomalies for the specified network.
     */
    @GetMapping("/network/{networkId}")
    public ResponseEntity<List<NetworkAnomaly>> getNetworkAnomaliesByNetworkId(@PathVariable final long networkId) {
        log.info("getNetworkAnomaliesByNetworkId() Retrieving network anomalies for network with ID: {}.", networkId);
        final List<NetworkAnomaly> networkAnomalies = this.networkAnomalyService.getNetworkAnomaliesByNetworkId(networkId);
        return ResponseEntity.ok(networkAnomalies);
    }

    /**
     * Retrieves network anomalies.
     *
     * @return A list of network anomalies.
     */
    @GetMapping
    public ResponseEntity<List<NetworkAnomaly>> getNetworkAnomalies() {
        log.info("getNetworkAnomalies() Retrieving network anomalies.");
        final List<NetworkAnomaly> networkAnomalies = this.networkAnomalyService.getNetworkAnomalies();
        return ResponseEntity.ok(networkAnomalies);
    }

    /**
     * Deletes a network anomaly by its ID.
     *
     * @param id The ID of the network anomaly to delete.
     * @return A response entity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNetworkAnomalyById(@PathVariable final String id) {
        log.info("deleteNetworkAnomalyById() Deleting network anomaly with ID: {}.", id);
        this.networkAnomalyService.deleteNetworkAnomalyById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes network anomalies for a given network ID.
     *
     * @param networkId The ID of the network.
     * @return A response entity with no content.
     */
    @DeleteMapping("/network/{networkId}")
    public ResponseEntity<Void> deleteNetworkAnomaliesByNetworkId(@PathVariable final long networkId) {
        log.info("deleteNetworkAnomaliesByNetworkId() Deleting network anomalies for network with ID: {}.", networkId);
        this.networkAnomalyService.deleteNetworkAnomaliesByNetworkId(networkId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes all network anomalies.
     *
     * @return A response entity with no content.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteNetworkAnomalies() {
        log.info("deleteNetworkAnomalies() Deleting network anomalies.");
        this.networkAnomalyService.deleteNetworkAnomalies();
        return ResponseEntity.noContent().build();
    }
}
