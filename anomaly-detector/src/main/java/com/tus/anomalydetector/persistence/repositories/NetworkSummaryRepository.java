package com.tus.anomalydetector.persistence.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tus.anomalydetector.persistence.documents.NetworkSummary;

/**
 * Repository interface for Network Summaries.
 * Extends MongoRepository for CRUD operations and custom queries.
 */
@Repository
public interface NetworkSummaryRepository extends MongoRepository<NetworkSummary, String> {

    Optional<NetworkSummary> findByNetworkId(final long networkId);
}
