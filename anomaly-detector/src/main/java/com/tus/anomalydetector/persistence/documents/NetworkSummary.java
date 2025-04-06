package com.tus.anomalydetector.persistence.documents;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tus.anomalydetector.utils.AnomalyDetectorConstants;

/**
 * Represents a summary of network traffic and anomalies.
 *
 * <p>Fields:</p>
 * <ul>
 *   <li><b>id</b> - Unique identifier for the summary record.</li>
 *   <li><b>networkId</b> - ID of the network.</li>
 *   <li><b>trafficSizeInBytes</b> - Total traffic size in bytes.</li>
 *   <li><b>anomalyCount</b> - Number of detected anomalies.</li>
 *   <li><b>nonAnomalyCount</b> - Number of normal (non-anomalous) events.</li>
 *   <li><b>lastUpdatedAt</b> - Timestamp of the last update to the summary.</li>
 * </ul>
 */
@Document(collection = AnomalyDetectorConstants.NETWORK_SUMMARIES_COLLECTION)
@Data
@Builder
public class NetworkSummary {

    @Id
    private String id;

    private long networkId;

    private double trafficSizeInBytes;

    private long anomalyCount;

    private long nonAnomalyCount;

    private Instant lastUpdatedAt;
}
