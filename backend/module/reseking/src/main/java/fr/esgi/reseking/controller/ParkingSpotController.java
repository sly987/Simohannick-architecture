package fr.esgi.reseking.controller;

import fr.esgi.reseking.controller.dto.ParkingSpotDTO;
import fr.esgi.reseking.controller.response.CheckInApiResponse;
import fr.esgi.reseking.service.ParkingSpotService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spots")
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @GetMapping
    public List<ParkingSpotDTO> getParkingSpots() {
        return parkingSpotService.getAllParkingSpots();
    }

    @PostMapping("/check-in/{row}/{column}")
    public ResponseEntity<CheckInApiResponse> checkIn(
            @PathVariable String row,
            @PathVariable String column,
            Authentication authentication
    ) {
        String employeeEmail = authentication.getName();
        parkingSpotService.checkIn(row, column, employeeEmail);
        return ResponseEntity.ok(new CheckInApiResponse("Check-in successful"));
    }
}
