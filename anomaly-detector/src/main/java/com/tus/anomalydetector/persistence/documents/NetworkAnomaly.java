package com.tus.anomalydetector.persistence.documents;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tus.anomalydetector.utils.AnomalyDetectorConstants;

/**
 * Represents an anomaly detected in a network.
 *
 * <p>Fields:</p>
 * <ul>
 *   <li><b>id</b> - Unique identifier for the anomaly.</li>
 *   <li><b>networkId</b> - ID of the network where the anomaly occurred.</li>
 *   <li><b>sizeInBytes</b> - Size of the anomalous data in bytes.</li>
 *   <li><b>timestamp</b> - Time when the anomaly was detected.</li>
 * </ul>
 */
@Document(collection = AnomalyDetectorConstants.NETWORK_ANOMALIES_COLLECTION)
@Data
@Builder
public class NetworkAnomaly {

    @Id
    private String id;

    private long networkId;

    private double sizeInBytes;

    private Instant timestamp;
}
