package com.tus.trafficsimulator.simulation;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.tus.trafficsimulator.models.NetworkMessage;
import com.tus.trafficsimulator.persistence.entities.Network;
import com.tus.trafficsimulator.persistence.enums.NetworkStatus;
import com.tus.trafficsimulator.services.KafkaProducerService;

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

    private static final int INITIAL_DELAY = 2;

    private static final int MIN_PERIOD = 2;

    private static final int MAX_PERIOD = 5;

    private static final int MIN_BYTES = 100;

    private static final int MAX_BYTES = 1000;

    @Getter
    private final Network network;

    private final KafkaProducerService kafkaProducerService;

    private final ScheduledExecutorService scheduledExecutorService;

    /**
     * Constructs a new NetworkSimulation instance.
     *
     * @param network              the network entity for which the simulation is
     *                             being created.
     * @param kafkaProducerService the Kafka producer service used to send network
     *                             messages.
     */
    public NetworkSimulation(final Network network, final KafkaProducerService kafkaProducerService) {
        this.network = network;
        this.kafkaProducerService = kafkaProducerService;
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
                this.kafkaProducerService.sendMessage(networkMessage);
            }
        }, INITIAL_DELAY, RANDOM.nextInt(MIN_PERIOD, MAX_PERIOD + 1), TimeUnit.SECONDS);
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
