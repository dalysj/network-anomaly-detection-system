package com.tus.trafficsimulator.services;

import com.tus.trafficsimulator.persistence.entities.Network;
import com.tus.trafficsimulator.persistence.repositories.NetworkRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

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

    /**
     * Constructs a new NetworkService with the provided NetworkRepository.
     * 
     * @param networkRepository The repository to use for network operations.
     */
    public NetworkService(final NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    /**
     * Creates a new network with the provided network details.
     * 
     * @param network The network details to create.
     * @return The created network.
     */
    public Network createNetwork(final Network network) {
        network.setIsActivated(false);
        final Network createdNetwork = this.networkRepository.save(network);
        log.info("createNetwork() Network created with Id: {}, Name: {}, Location: {}. ", createdNetwork.getId(),
                createdNetwork.getName(),
                createdNetwork.getLocation());
        return createdNetwork;
    }

    /**
     * Retrieves a network by its ID.
     * 
     * @param id The ID of the network to retrieve.
     * @return The network with the provided ID.
     */
    public Network getNetworkById(final Long id) {
        log.info("getNetworkById() Retrieving Network with Id: {}.", id);
        return this.networkRepository.findById(id).orElseThrow();
    }

    /**
     * Retrieves all networks.
     * 
     * @return A list of all networks.
     */
    public List<Network> getAllNetworks() {
        log.info("getAllNetworks() Retrieving all Networks.");
        return this.networkRepository.findAll();
    }

    /**
     * Updates a network with the provided network details.
     * 
     * @param id      The ID of the network to update.
     * @param network The network details to update.
     * @return The updated network.
     */
    public Network updateNetwork(final Long id, final Network network) {
        final Optional<Network> networkOptional = this.networkRepository.findById(id);

        if (networkOptional.isEmpty()) {
            log.info("updateNetwork() Network with Id: {} not found. Creating a new Network.", id);
            return this.createNetwork(network);
        }

        final Network existingNetwork = networkOptional.get();
        existingNetwork.setName(network.getName());
        existingNetwork.setLocation(network.getLocation());
        existingNetwork.setIsActivated(network.getIsActivated());

        final Network updatedNetwork = this.networkRepository.save(existingNetwork);
        log.info("updateNetwork() Network updated with Id: {}, Name: {}, Location: {}. ", updatedNetwork.getId(),
                updatedNetwork.getName(), updatedNetwork.getLocation());
        return updatedNetwork;
    }

    /**
     * Deletes a network by its ID.
     * 
     * @param id The ID of the network to delete.
     */
    public void deleteNetworkById(final Long id) {
        final Optional<Network> networkOptional = this.networkRepository.findById(id);

        if (networkOptional.isEmpty()) {
            log.info("deleteNetworkById() Network with Id: {} not found.", id);
            throw new NoSuchElementException();
        }

        log.info("deleteNetworkById() Deleting Network with Id: {}.", id);
        this.networkRepository.deleteById(id);
    }

    /**
     * Deletes all networks.
     */
    public void deleteAllNetworks() {
        log.info("deleteAllNetworks() Deleting all Networks.");
        this.networkRepository.deleteAll();
    }
}
