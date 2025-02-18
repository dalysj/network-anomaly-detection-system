package com.tus.trafficsimulator.persistence.repositories;

import com.tus.trafficsimulator.persistence.entities.Network;
import com.tus.trafficsimulator.services.KafkaProducerService;
import com.tus.trafficsimulator.simulation.NetworkSimulation;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

/**
 * Represents a repository for network simulations.
 */
@Slf4j
@Repository
public class NetworkSimulationRepository {

    private final NetworkRepository networkRepository;

    private final KafkaProducerService kafkaProducerService;

    private final Map<Long, NetworkSimulation> networkSimulations;

    /**
     * Constructs a new network simulation repository.
     * 
     * @param networkRepository    The network repository.
     * @param kafkaProducerService The Kafka producer service.
     */
    public NetworkSimulationRepository(final NetworkRepository networkRepository,
            final KafkaProducerService kafkaProducerService) {
        this.networkRepository = networkRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.networkSimulations = new ConcurrentHashMap<>();
    }

    /**
     * Finds a network simulation by the network ID.
     * 
     * @param networkId The network ID.
     * @return An {@link Optional} containing the network simulation if found,
     *         otherwise an empty {@link Optional}.
     */
    public Optional<NetworkSimulation> findByNetworkId(final long networkId) {
        log.info("findByNetworkId() Getting network simulation with network ID: {}.", networkId);
        return Optional.ofNullable(this.networkSimulations.get(networkId));
    }

    /**
     * Saves a network simulation.
     * 
     * @param network The network to save.
     */
    public void save(final Network network) {
        final Optional<NetworkSimulation> networkSimulationOptional = this.findByNetworkId(network.getId());

        if (networkSimulationOptional.isPresent()) {
            log.info("save() Updating status of network simulation with network ID: {}.",
                    network.getId());
            final NetworkSimulation existingNetworkSimulation = networkSimulationOptional.get();
            existingNetworkSimulation.getNetwork().setStatus(network.getStatus());
            return;
        }

        log.info("save() Saving network simulation with network ID: {}.", network.getId());
        final NetworkSimulation networkSimulation = new NetworkSimulation(network, this.kafkaProducerService);
        networkSimulation.start();
        this.networkSimulations.put(network.getId(), networkSimulation);
    }

    /**
     * Deletes a network simulation by the network ID.
     * 
     * @param networkId The network ID.
     */
    public void deleteByNetworkId(final long networkId) {
        final Optional<NetworkSimulation> networkSimulationOptional = this.findByNetworkId(networkId);

        if (networkSimulationOptional.isEmpty()) {
            log.warn("deleteByNetworkId() Network simulation with network ID: {} not found.", networkId);
            return;
        }

        log.info("deleteByNetworkId() Deleting network simulation with network ID: {}.", networkId);
        networkSimulationOptional.get().stop();
        this.networkSimulations.remove(networkId);
    }

    @PostConstruct
    private void init() {
        log.info("init() Initializing network simulations.");
        this.networkRepository.findAll().forEach(network -> {
            this.save(network);
        });
        log.info("init() Network simulations initialized.");
    }
}
