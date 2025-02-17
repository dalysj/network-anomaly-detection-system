package com.tus.trafficsimulator.simulation;

import com.tus.trafficsimulator.models.NetworkMessage;
import com.tus.trafficsimulator.persistence.entities.Network;
import com.tus.trafficsimulator.persistence.enums.NetworkStatus;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a network simulation entity that generates network messages for a
 * network.
 * 
 * <p>
 * This class uses Lombok annotations to generate a logger instance.
 * </p>
 */
@Slf4j
public class NetworkSimulation {

    private static final Random RANDOM = new Random();

    private final static int INITAL_DELAY = 2;

    private final static int MIN_PERIOD = 2;

    private final static int MAX_PERIOD = 5;

    private final static int MIN_BYTES = 100;

    private final static int MAX_BYTES = 1000;

    @Getter
    private final Network network;

    private final ScheduledExecutorService scheduledExecutorService;

    /**
     * Creates a new instance of {@link NetworkSimulation} with the specified
     * {@link Network}.
     * 
     * @param network The network to simulate.
     */
    public NetworkSimulation(final Network network) {
        this.network = network;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Starts the simulation for the network.
     */
    public void start() {
        this.scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (this.network.getStatus() == NetworkStatus.ACTIVATED) {
                final NetworkMessage networkMessage = this.generateNetworkMessage();
                log.info("start() Network with ID: {} generated network message: {}", this.network.getId(),
                        networkMessage);
            }
        }, INITAL_DELAY, RANDOM.nextInt(MIN_PERIOD, MAX_PERIOD + 1), TimeUnit.SECONDS);
    }

    /**
     * Stops the simulation for the network.
     */
    public void stop() {
        log.info("stop() Stopping simulation for network with ID: {}.", network.getId());
        this.scheduledExecutorService.shutdown();
        log.info("stop() Simulation for network with ID: {} has stopped.", network.getId());
    }

    private NetworkMessage generateNetworkMessage() {
        return NetworkMessage.builder()
                .id(UUID.randomUUID())
                .networkId(this.network.getId())
                .sizeInBytes(RANDOM.nextInt(MIN_BYTES, MAX_BYTES + 1))
                .timestamp(Instant.now())
                .build();
    }
}
