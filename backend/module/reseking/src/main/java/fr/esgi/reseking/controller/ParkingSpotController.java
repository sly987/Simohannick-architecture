package fr.esgi.reseking.controller;

import fr.esgi.reseking.controller.dto.ParkingSpotDTO;
import fr.esgi.reseking.repository.ParkingSpotRepository;
import fr.esgi.reseking.util.ParkingSpotMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/spots")
public class ParkingSpotController {

    private final ParkingSpotRepository parkingSpotRepository;


    public ParkingSpotController (ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @GetMapping
    public List<ParkingSpotDTO> getParkingSpots() {
        return parkingSpotRepository.findAll().stream().map(ParkingSpotMapper::toDTO).toList();
    }
}
