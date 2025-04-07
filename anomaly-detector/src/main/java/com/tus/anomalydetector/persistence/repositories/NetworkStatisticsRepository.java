package com.tus.anomalydetector.persistence.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tus.anomalydetector.models.FixedSizeDeque;
import com.tus.anomalydetector.models.NetworkMessage;

/**
 * Repository class for storing and analyzing network statistics.
 *
 * <p>This class maintains a fixed-size history of message sizes for each network ID
 * and provides methods to compute statistical measures such as mean and standard deviation.</p>
 */
@Repository
public class NetworkStatisticsRepository {

    private final Map<Long, FixedSizeDeque<Double>> map;

    /**
     * Constructs a new {@code NetworkStatisticsRepository} with an empty storage map.
     */
    public NetworkStatisticsRepository() {
        this.map = new HashMap<>();
    }

    /**
     * Saves a network message's size into the repository.
     *
     * <p>If no previous entries exist for the given network ID, a new fixed-size deque is created.</p>
     *
     * @param networkMessage the network message containing the network ID and size information
     */
    public void save(final NetworkMessage networkMessage) {
        if (!this.map.containsKey(networkMessage.getNetworkId())) {
            this.map.put(networkMessage.getNetworkId(), new FixedSizeDeque<>(500));
        }

        this.map.get(networkMessage.getNetworkId()).addFirst(networkMessage.getSizeInBytes());
    }

    /**
     * Computes the mean (average) size of network messages for a given network ID.
     *
     * @param networkId the ID of the network
     * @return the mean size of the messages; returns {@code 0.0} if no data is available
     */
    public double findMean(final long networkId) {
        return this.map.containsKey(networkId) ? this.map.get(networkId).mean() : 0.0;
    }

    /**
     * Computes the standard deviation of network message sizes for a given network ID.
     *
     * @param networkId the ID of the network
     * @return the standard deviation of the message sizes; returns {@code 0.0} if no data is available
     */
    public double findStandardDeviation(final long networkId) {
        return this.map.containsKey(networkId) ? this.map.get(networkId).standardDeviation() : 0.0;
    }
}
