package com.tus.trafficsimulator.persistence.repositories;

import com.tus.trafficsimulator.persistence.entities.Network;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Network entities.
 * Extends JpaRepository for CRUD operations and custom queries.
 */
@Repository
public interface NetworkRepository extends JpaRepository<Network, Long> {

    /**
     * Find network by name.
     *
     * @param name name of the network
     * @return list of networks with the given name
     */
    List<Network> findByName(final String name);

    /**
     * Find network by location.
     *
     * @param location location of the network
     * @return list of networks with the given location
     */
    List<Network> findByLocation(final String location);

    /**
     * Find network by isActivated.
     *
     * @param isActivated isActivated of the network
     * @return list of networks with the given isActivated
     */
    List<Network> findByIsActivated(final Boolean isActivated);
}
