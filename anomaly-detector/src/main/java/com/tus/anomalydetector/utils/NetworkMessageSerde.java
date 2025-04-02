package com.tus.anomalydetector.utils;

import org.apache.kafka.common.serialization.Serdes;

import com.tus.anomalydetector.models.NetworkMessage;

/**
 * A custom SerDe (Serializer/Deserializer) for {@link NetworkMessage} objects.
 * <p>
 * This class provides a wrapper for the Kafka Serdes functionality by combining
 * the custom {@link NetworkMessageSerializer} and {@link NetworkMessageDeserializer}
 * for serializing and deserializing {@link NetworkMessage} objects to and from byte arrays.
 * </p>
 */
public class NetworkMessageSerde extends Serdes.WrapperSerde<NetworkMessage> {

    /**
     * Constructs a new {@link NetworkMessageSerde}.
     * <p>
     * The constructor passes the custom {@link NetworkMessageSerializer} and
     * {@link NetworkMessageDeserializer} to the superclass, enabling Kafka
     * to handle {@link NetworkMessage} objects as key/value pairs.
     * </p>
     */
    public NetworkMessageSerde() {
        super(new NetworkMessageSerializer(), new NetworkMessageDeserializer());
    }
}
