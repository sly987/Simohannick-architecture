package fr.esgi.reseking.controller;

import fr.esgi.reseking.controller.response.CreationApiResponse;
import fr.esgi.reseking.controller.dto.ReservationDTO;
import fr.esgi.reseking.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<CreationApiResponse> add(@RequestBody ReservationDTO reservationDTO) {
        Integer reservationId = reservationService.addReservation(reservationDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreationApiResponse("Reservation created successfully",  reservationId));
    }

    @GetMapping
    public List<ReservationDTO> getAll() {
        return reservationService.getAllReservations();
    }
}