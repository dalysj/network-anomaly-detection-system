package com.tus.anomalydetector.controllers;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tus.anomalydetector.persistence.documents.NetworkSummary;
import com.tus.anomalydetector.services.NetworkSummaryService;
import com.tus.anomalydetector.utils.AnomalyDetectorConstants;

/**
 * Controller responsible for handling requests related to network summary operations,
 * such as retrieving network summaries.
 * <p>
 * The controller interacts with the {@link NetworkSummaryService} to perform the necessary
 * actions and returns appropriate HTTP responses.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping(AnomalyDetectorConstants.NETWORK_SUMMARY_CONTROLLER_URL)
@CrossOrigin
public class NetworkSummaryController {

    private final NetworkSummaryService networkSummaryService;

    /**
     * Constructs a new NetworkSummaryController with the provided NetworkSummaryService.
     *
     * @param networkSummaryService The service to use for network summary operations.
     */
    public NetworkSummaryController(final NetworkSummaryService networkSummaryService) {
        this.networkSummaryService = networkSummaryService;
    }

    /**
     * Retrieves network summaries for a given network ID.
     *
     * @param networkId the ID of the network.
     * @return A list of network summaries for the specified network.
     */
    @GetMapping("/network/{networkId}")
    public ResponseEntity<List<NetworkSummary>> getNetworkSummariesByNetworkId(@PathVariable final long networkId) {
        log.info("getNetworkSummariesByNetworkId() Retrieving network summaries for network with ID: {}.", networkId);
        final List<NetworkSummary> networkSummaries = this.networkSummaryService.getNetworkSummariesByNetworkId(networkId);
        return ResponseEntity.ok(networkSummaries);
    }

    /**
     * Retrieves network summaries.
     *
     * @return A list of network summaries.
     */
    @GetMapping
    public ResponseEntity<List<NetworkSummary>> getNetworkSummaries() {
        log.info("getNetworkSummaries() Retrieving network summaries.");
        final List<NetworkSummary> networkSummaries = this.networkSummaryService.getNetworkSummaries();
        return ResponseEntity.ok(networkSummaries);
    }
}
