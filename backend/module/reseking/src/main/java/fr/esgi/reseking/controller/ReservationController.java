package fr.esgi.reseking.controller;

import fr.esgi.reseking.controller.response.CreationApiResponse;
import fr.esgi.reseking.controller.dto.ReservationDTO;
import fr.esgi.reseking.controller.validator.ReservationValidator;
import fr.esgi.reseking.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservations", description = "Endpoints for managing parking spot reservations")
@SecurityRequirement(name = "bearerAuth")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @Operation(summary = "Create reservation", description = "Create a new parking spot reservation for an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reservation created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreationApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input, spot unavailable, or duration exceeded"),
            @ApiResponse(responseCode = "409", description = "Employee already has an active reservation or spot is occupied"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<CreationApiResponse> add(@RequestBody ReservationDTO reservationDTO) {
        ReservationValidator.validateReservationInput(reservationDTO);
        Integer reservationId = reservationService.addReservation(reservationDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreationApiResponse("Reservation created successfully",  reservationId));
    }

    @GetMapping
    @Operation(summary = "Get all reservations", description = "Retrieve all reservations in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public List<ReservationDTO> getAll() {
        return reservationService.getAllReservations();
    }
}
