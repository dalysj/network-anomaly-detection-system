package com.tus.anomalydetector.persistence.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tus.anomalydetector.persistence.documents.NetworkSummary;

/**
 * Repository interface for Network Summaries.
 * Extends MongoRepository for CRUD operations and custom queries.
 */
@Repository
public interface NetworkSummaryRepository extends MongoRepository<NetworkSummary, String> {

    /**
     * Retrieves all {@link NetworkSummary} entries by network ID.
     *
     * @param networkId The ID of the network.
     * @return A {@link List} of {@link NetworkSummary} instances matching the network ID.
     * The list will be empty if no matching entries are found.
     */
    List<NetworkSummary> findByNetworkId(final long networkId);
}
