package com.tus.anomalydetector.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tus.anomalydetector.persistence.documents.NetworkAnomaly;

/**
 * Repository interface for Network Anomalies.
 * Extends MongoRepository for CRUD operations and custom queries.
 */
@Repository
public interface NetworkAnomalyRepository extends MongoRepository<NetworkAnomaly, String> {
    // No implementation.
}
