package com.tiger.braziltours.repositories;

import com.tiger.braziltours.entities.TouristSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristSpotRepository extends JpaRepository<TouristSpot, Long> {
}
