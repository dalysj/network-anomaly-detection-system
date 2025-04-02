package com.tus.trafficsimulator.controllers;

import java.net.URI;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tus.trafficsimulator.models.ActivationManagementRequest;
import com.tus.trafficsimulator.persistence.entities.Network;
import com.tus.trafficsimulator.services.NetworkService;
import com.tus.trafficsimulator.utils.TrafficSimulatorConstants;

/**
 * Provides network services such as creating, retrieving, updating, and
 * deleting networks.
 *
 * <p>
 * This class uses Lombok annotations to generate a logger.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping(TrafficSimulatorConstants.NETWORK_CONTROLLER_URL)
@CrossOrigin
public class NetworkController {

    private final NetworkService networkService;

    /**
     * Constructs a new NetworkController with the provided NetworkService.
     *
     * @param networkService The service to use for network operations.
     */
    public NetworkController(final NetworkService networkService) {
        this.networkService = networkService;
    }

    /**
     * Retrieves a network by its ID.
     *
     * @param id The ID of the network to retrieve.
     * @return The network with the provided ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Network> getNetworkById(@PathVariable final Long id) {
        log.info("getNetworkById() Retrieving network with ID: {}.", id);
        final Network network = this.networkService.getNetworkById(id);
        return ResponseEntity.ok(network);
    }

    /**
     * Retrieves all networks.
     *
     * @return A list of all networks.
     */
    @GetMapping
    public ResponseEntity<List<Network>> getNetworks() {
        log.info("getNetworks() Retrieving all networks.");
        final List<Network> networks = this.networkService.getAllNetworks();
        return ResponseEntity.ok(networks);
    }

    /**
     * Creates a new network with the provided network details.
     *
     * @param network The network details to create.
     * @return The created network.
     */
    @PostMapping
    public ResponseEntity<Network> createNetwork(@RequestBody final Network network) {
        log.info("createNetwork() Creating network.");
        final Network createdNetwork = this.networkService.createNetwork(network);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdNetwork.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdNetwork);
    }

    /**
     * Updates the status of a network by its ID.
     *
     * @param id                          The ID of the network to update.
     * @param activationManagementRequest The request to update the network status.
     * @return The updated network.
     */
    @PutMapping("/activation-management/{id}")
    public ResponseEntity<Network> updateNetworkStatus(@PathVariable final Long id,
                                                       @RequestBody final ActivationManagementRequest activationManagementRequest) {
        log.info("updateNetworkStatus() Updating status of network with ID: {}.", id);
        final Network updatedNetwork = this.networkService.updateNetworkStatus(id, activationManagementRequest);
        return ResponseEntity.ok(updatedNetwork);
    }

    /**
     * Deletes a network by its ID.
     *
     * @param id The ID of the network to delete.
     * @return A response entity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNetworkById(@PathVariable final Long id) {
        log.info("deleteNetworkById() Deleting network with ID: {}.", id);
        this.networkService.deleteNetworkById(id);
        return ResponseEntity.noContent().build();
    }
}
