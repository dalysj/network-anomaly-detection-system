package com.tus.trafficsimulator.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.trafficsimulator.persistence.entities.Network;

/**
 * Repository interface for Network entities.
 * Extends JpaRepository for CRUD operations and custom queries.
 */
@Repository
public interface NetworkRepository extends JpaRepository<Network, Long> {
    // No implementation.
}
