package fr.esgi.reseking.repository;

import fr.esgi.reseking.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Integer> {
}