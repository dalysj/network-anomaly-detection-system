package com.tus.anomalydetector.persistence.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tus.anomalydetector.persistence.documents.NetworkAnomaly;

/**
 * Repository interface for Network Anomalies.
 * Extends MongoRepository for CRUD operations and custom queries.
 */
@Repository
public interface NetworkAnomalyRepository extends MongoRepository<NetworkAnomaly, String> {

    /**
     * Finds network anomalies by network ID with pagination and sorting.
     *
     * @param networkId the ID of the network.
     * @param pageable  the pagination and sorting information.
     * @return a page of network anomalies for the specified network.
     */
    Page<NetworkAnomaly> findByNetworkId(final long networkId, final Pageable pageable);

    /**
     * Deletes network anomalies by network ID.
     *
     * @param networkId the ID of the network.
     */
    void deleteByNetworkId(final long networkId);
}
