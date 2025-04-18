package com.tus.trafficsimulator.services;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tus.trafficsimulator.exceptions.TrafficSimulatorException;
import com.tus.trafficsimulator.exceptions.enums.TrafficSimulatorError;
import com.tus.trafficsimulator.models.ActivationManagementRequest;
import com.tus.trafficsimulator.models.enums.ActivationAction;
import com.tus.trafficsimulator.persistence.entities.Network;
import com.tus.trafficsimulator.persistence.enums.NetworkStatus;
import com.tus.trafficsimulator.persistence.repositories.NetworkRepository;
import com.tus.trafficsimulator.persistence.repositories.NetworkSimulationRepository;

/**
 * Provides network services such as creating, retrieving, updating, and
 * deleting networks.
 *
 * <p>
 * This class uses Lombok annotations to generate a logger.
 * </p>
 */
@Slf4j
@Service
public class NetworkService {

    private final NetworkRepository networkRepository;

    private final NetworkSimulationRepository networkSimulationRepository;

    /**
     * Constructs a new NetworkService with the provided network repository and
     * network simulation repository.
     *
     * @param networkRepository           The repository for network entities.
     * @param networkSimulationRepository The repository for network simulation
     *                                    entities.
     */
    public NetworkService(final NetworkRepository networkRepository,
                          final NetworkSimulationRepository networkSimulationRepository) {
        this.networkRepository = networkRepository;
        this.networkSimulationRepository = networkSimulationRepository;
    }

    /**
     * Retrieves a network by its ID.
     *
     * @param id The ID of the network to retrieve.
     * @return The network with the provided ID.
     */
    public Network getNetworkById(final long id) {
        final Optional<Network> networkOptional;
        try {
            networkOptional = this.networkRepository.findById(id);
        } catch (final Exception exception) {
            log.error("getNetworkById() An error occurred while getting the network with ID: {}. Exception: {}",
                    id,
                    exception.getMessage(), exception);
            final String[] errorDetailArgs = {String.valueOf(id)};
            throw new TrafficSimulatorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    TrafficSimulatorError.GET_NETWORK_ERROR, errorDetailArgs);
        }

        if (networkOptional.isEmpty()) {
            log.error("getNetworkById() Network with ID: {} not found.", id);
            final String[] errorDetailArgs = {String.valueOf(id)};
            throw new TrafficSimulatorException(HttpStatus.NOT_FOUND,
                    TrafficSimulatorError.NETWORK_NOT_FOUND_ERROR,
                    errorDetailArgs);
        }

        return networkOptional.get();
    }

    /**
     * Retrieves all networks.
     *
     * @return A list of all networks.
     */
    public List<Network> getAllNetworks() {
        log.info("getAllNetworks() Retrieving all networks.");
        try {
            return this.networkRepository.findAll();
        } catch (final Exception exception) {
            log.error("getAllNetworks() An error occurred while getting the networks. Exception: {}", exception.getMessage(), exception);
            throw new TrafficSimulatorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    TrafficSimulatorError.GET_NETWORKS_ERROR);
        }
    }

    /**
     * Creates a new network with the provided network details.
     *
     * @param network The network details to create.
     * @return The created network.
     */
    public Network createNetwork(final Network network) {
        try {
            this.validateNetworkForCreate(network);
            network.setStatus(NetworkStatus.DEACTIVATED);

            final Network createdNetwork = this.networkRepository.save(network);
            this.networkSimulationRepository.save(createdNetwork);
            log.info("createNetwork() Network created with ID: {}, name: {}, location: {}. ", createdNetwork.getId(),
                    createdNetwork.getName(),
                    createdNetwork.getLocation());
            return createdNetwork;
        } catch (final Exception exception) {
            log.error("createNetwork() An error occurred while creating the network. Exception: {}", exception.getMessage(), exception);
            throw new TrafficSimulatorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    TrafficSimulatorError.CREATE_NETWORK_ERROR);
        }
    }

    /**
     * Updates the status of a network by its ID.
     *
     * @param id                          The ID of the network to update.
     * @param activationManagementRequest The request containing the action to
     *                                    perform on the network.
     * @return The updated network.
     */
    public Network updateNetworkStatus(final long id, final ActivationManagementRequest activationManagementRequest) {
        final Optional<Network> networkOptional;
        try {
            networkOptional = this.networkRepository.findById(id);
        } catch (final Exception exception) {
            log.error("updateNetworkStatus() An error occurred while getting the network with ID: {}. Exception: {}",
                    id,
                    exception.getMessage(), exception);
            final String[] errorDetailArgs = {String.valueOf(id)};
            throw new TrafficSimulatorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    TrafficSimulatorError.GET_NETWORK_ERROR, errorDetailArgs);
        }

        if (networkOptional.isEmpty()) {
            log.error("updateNetworkStatus() Network with ID: {} not found.", id);
            final String[] errorDetailArgs = {String.valueOf(id)};
            throw new TrafficSimulatorException(HttpStatus.NOT_FOUND,
                    TrafficSimulatorError.NETWORK_NOT_FOUND_ERROR,
                    errorDetailArgs);
        }

        final Network existingNetwork = networkOptional.get();
        final NetworkStatus status = activationManagementRequest.getAction() == ActivationAction.ACTIVATE
                ? NetworkStatus.ACTIVATED
                : NetworkStatus.DEACTIVATED;

        if (existingNetwork.getStatus() == status && status == NetworkStatus.ACTIVATED) {
            log.error("updateNetworkStatus() Network with ID: {} must have status: {} to {} the network.", id,
                    NetworkStatus.DEACTIVATED, ActivationAction.ACTIVATE);
            final String[] errorDetailArgs = {String.valueOf(id), NetworkStatus.DEACTIVATED.toString(),
                    ActivationAction.ACTIVATE.toString()};
            throw new TrafficSimulatorException(HttpStatus.BAD_REQUEST,
                    TrafficSimulatorError.ACTIVATION_MANAGEMENT_ERROR,
                    errorDetailArgs);
        }

        if (existingNetwork.getStatus() == status) {
            log.error("updateNetworkStatus() Network with ID: {} must have status: {} to {} the network.", id,
                    NetworkStatus.ACTIVATED, ActivationAction.DEACTIVATE);
            final String[] errorDetailArgs = {String.valueOf(id), NetworkStatus.ACTIVATED.toString(),
                    ActivationAction.DEACTIVATE.toString()};
            throw new TrafficSimulatorException(HttpStatus.BAD_REQUEST,
                    TrafficSimulatorError.ACTIVATION_MANAGEMENT_ERROR,
                    errorDetailArgs);
        }

        existingNetwork.setStatus(status);

        try {
            final Network updatedNetwork = this.networkRepository.save(existingNetwork);
            this.networkSimulationRepository.save(updatedNetwork);
            log.info("updateNetworkStatus() Network updated with ID: {}, status: {}. ", updatedNetwork.getId(),
                    updatedNetwork.getStatus());
            return updatedNetwork;
        } catch (final Exception exception) {
            log.error(
                    "updateNetworkStatus() An error occurred while updating the status of the network with ID: {}. Exception: {}",
                    id, exception.getMessage(), exception);
            final String[] errorDetailArgs = {String.valueOf(id)};
            throw new TrafficSimulatorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    TrafficSimulatorError.UPDATE_NETWORK_STATUS_ERROR, errorDetailArgs);
        }
    }

    /**
     * Deletes a network by its ID.
     *
     * @param id The ID of the network to delete.
     */
    public void deleteNetworkById(final long id) {
        final Optional<Network> networkOptional;
        try {
            networkOptional = this.networkRepository.findById(id);
        } catch (final Exception exception) {
            log.error("deleteNetworkById() An error occurred while getting the network with ID: {}. Exception: {}", id,
                    exception.getMessage(), exception);
            final String[] errorDetailArgs = {String.valueOf(id)};
            throw new TrafficSimulatorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    TrafficSimulatorError.GET_NETWORK_ERROR, errorDetailArgs);
        }

        if (networkOptional.isEmpty()) {
            log.info("deleteNetworkById() Network with ID: {} not found.", id);
            final String[] errorDetailArgs = {String.valueOf(id)};
            throw new TrafficSimulatorException(HttpStatus.NOT_FOUND,
                    TrafficSimulatorError.NETWORK_NOT_FOUND_ERROR,
                    errorDetailArgs);
        }

        try {
            log.info("deleteNetworkById() Deleting network with ID: {}.", id);
            this.networkRepository.deleteById(id);
            this.networkSimulationRepository.deleteByNetworkId(id);
        } catch (final Exception exception) {
            log.error("deleteNetworkById() An error occurred while deleting the network with ID: {}. Exception: {}", id,
                    exception.getMessage(), exception);
            final String[] errorDetailArgs = {String.valueOf(id)};
            throw new TrafficSimulatorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    TrafficSimulatorError.DELETE_NETWORK_ERROR, errorDetailArgs);
        }
    }

    private void validateNetworkForCreate(final Network network) {
        if (network.getName() == null || network.getName().isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (network.getLocation() == null || network.getLocation().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
