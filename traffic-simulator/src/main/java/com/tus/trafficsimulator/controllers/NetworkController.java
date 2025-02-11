package com.tus.trafficsimulator.controllers;

import com.tus.trafficsimulator.exceptions.TrafficSimulatorError;
import com.tus.trafficsimulator.models.ProblemDetails;
import com.tus.trafficsimulator.persistence.entities.Network;
import com.tus.trafficsimulator.services.NetworkService;
import com.tus.trafficsimulator.utils.TrafficSimulatorConstants;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> getNetworkById(@PathVariable final Long id) {
        try {
            log.info("getNetworkById() Retrieving Network with Id: {}.", id);
            final Network network = this.networkService.getNetworkById(id);

            return ResponseEntity.ok(network);
        } catch (final NoSuchElementException noSuchElementException) {
            log.error("getNetworkById() Network with Id: {} not found.", id);
            final ProblemDetails problemDetails = this.createProblemDetails(
                    TrafficSimulatorError.NETWORK_NOT_FOUND_ERROR.getTitle(),
                    404,
                    TrafficSimulatorError.NETWORK_NOT_FOUND_ERROR.getDetail());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetails);
        } catch (final Exception exception) {
            log.error("getNetworkById() Error retrieving Network with Id: {}.", id, exception);
            final ProblemDetails problemDetails = this.createProblemDetails(
                    TrafficSimulatorError.GET_NETWORK_ERROR.getTitle(),
                    500,
                    TrafficSimulatorError.GET_NETWORK_ERROR.getDetail());

            return ResponseEntity.internalServerError().body(problemDetails);
        }
    }

    /**
     * Retrieves all networks.
     * 
     * @return A list of all networks.
     */
    @GetMapping
    public ResponseEntity<?> getNetworks() {
        try {
            log.info("getNetworks() Retrieving all Networks.");
            final List<Network> networks = this.networkService.getAllNetworks();

            return ResponseEntity.ok(networks);
        } catch (final Exception exception) {
            log.error("getNetworks() Error retrieving all Networks.", exception);
            final ProblemDetails problemDetails = this.createProblemDetails(
                    TrafficSimulatorError.GET_NETWORKS_ERROR.getTitle(),
                    500,
                    TrafficSimulatorError.GET_NETWORKS_ERROR.getDetail());

            return ResponseEntity.internalServerError().body(problemDetails);
        }
    }

    /**
     * Creates a new network with the provided network details.
     * 
     * @param network The network details to create.
     * @return The created network.
     */
    @PostMapping
    public ResponseEntity<?> createNetwork(@RequestBody final Network network) {
        try {
            log.info("createNetwork() Creating Network with Name: {}, Location: {}.", network.getName(),
                    network.getLocation());
            final Network createdNetwork = this.networkService.createNetwork(network);

            final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdNetwork.getId())
                    .toUri();

            return ResponseEntity.created(location).body(createdNetwork);
        } catch (final Exception exception) {
            log.error("createNetwork() Error creating Network with Name: {}, Location: {}.", network.getName(),
                    network.getLocation(), exception);
            final ProblemDetails problemDetails = this.createProblemDetails(
                    TrafficSimulatorError.CREATE_NETWORK_ERROR.getTitle(),
                    400,
                    TrafficSimulatorError.CREATE_NETWORK_ERROR.getDetail());

            return ResponseEntity.badRequest().body(problemDetails);
        }
    }

    /**
     * Updates a network with the provided network details.
     * 
     * @param id      The ID of the network to update.
     * @param network The network details to update.
     * @return The updated network.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNetwork(@PathVariable final Long id, @RequestBody final Network network) {
        int status;
        try {
            log.info("updateNetwork() Checking if Network with Id: {} exists.", id);
            this.networkService.getNetworkById(id);
            status = 200;
        } catch (final NoSuchElementException noSuchElementException) {
            log.info("updateNetwork() Network with Id: {} not found. Creating a new Network.", id);
            status = 201;
        }

        try {
            log.info("updateNetwork() Updating Network with Id: {}, Name: {}, Location: {}.", id, network.getName(),
                    network.getLocation());
            final Network updatedNetwork = this.networkService.updateNetwork(id, network);

            return ResponseEntity.status(status).body(updatedNetwork);
        } catch (final Exception exception) {
            log.error("updateNetwork() Error updating Network with Id: {}, Name: {}, Location: {}.", id,
                    network.getName(), network.getLocation(), exception);
            final ProblemDetails problemDetails = this.createProblemDetails(
                    TrafficSimulatorError.UPDATE_NETWORK_ERROR.getTitle(),
                    500,
                    TrafficSimulatorError.UPDATE_NETWORK_ERROR.getDetail());

            return ResponseEntity.internalServerError().body(problemDetails);
        }
    }

    /**
     * Deletes a network by its ID.
     * 
     * @param id The ID of the network to delete.
     * @return A response entity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNetworkById(@PathVariable final Long id) {
        try {
            log.info("deleteNetworkById() Deleting Network with Id: {}.", id);
            this.networkService.deleteNetworkById(id);

            return ResponseEntity.noContent().build();
        } catch (final NoSuchElementException noSuchElementException) {
            log.error("deleteNetworkById() Network with Id: {} not found.", id);
            final ProblemDetails problemDetails = this.createProblemDetails(
                    TrafficSimulatorError.NETWORK_NOT_FOUND_ERROR.getTitle(),
                    404,
                    TrafficSimulatorError.NETWORK_NOT_FOUND_ERROR.getDetail());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetails);
        } catch (final Exception exception) {
            log.error("deleteNetworkById() Error deleting Network with Id: {}.", id, exception);
            final ProblemDetails problemDetails = this.createProblemDetails(
                    TrafficSimulatorError.DELETE_NETWORK_ERROR.getTitle(),
                    500,
                    TrafficSimulatorError.DELETE_NETWORK_ERROR.getDetail());

            return ResponseEntity.internalServerError().body(problemDetails);
        }
    }

    /**
     * Deletes all networks.
     * 
     * @return A response entity with no content.
     */
    @DeleteMapping
    public ResponseEntity<?> deleteNetworks() {
        try {
            log.info("deleteNetworks() Deleting all Networks.");
            this.networkService.deleteAllNetworks();

            return ResponseEntity.noContent().build();
        } catch (final Exception exception) {
            log.error("deleteNetworks() Error deleting all Networks.", exception);
            final ProblemDetails problemDetails = this.createProblemDetails(
                    TrafficSimulatorError.DELETE_NETWORKS_ERROR.getTitle(),
                    500,
                    TrafficSimulatorError.DELETE_NETWORKS_ERROR.getDetail());

            return ResponseEntity.internalServerError().body(problemDetails);
        }
    }

    private ProblemDetails createProblemDetails(final String title, final int status, final String detail) {
        return ProblemDetails.builder()
                .title(title)
                .status(status)
                .detail(detail)
                .build();
    }
}
