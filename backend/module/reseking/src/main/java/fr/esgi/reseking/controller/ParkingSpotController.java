package fr.esgi.reseking.controller;

import fr.esgi.reseking.controller.dto.ParkingSpotDTO;
import fr.esgi.reseking.controller.response.CheckInApiResponse;
import fr.esgi.reseking.service.ParkingSpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spots")
@Tag(name = "Parking Spots", description = "Endpoints for managing parking spots and check-ins")
@SecurityRequirement(name = "bearerAuth")
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @GetMapping
    @Operation(summary = "Get all parking spots", description = "Retrieve all parking spots with their details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parking spots retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public List<ParkingSpotDTO> getParkingSpots() {
        return parkingSpotService.getAllParkingSpots();
    }

    @PostMapping("/check-in/{row}/{column}")
    @Operation(summary = "Check-in to parking spot", description = "Check-in to a reserved parking spot using row and column")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check-in successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CheckInApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Parking spot or reservation not found"),
            @ApiResponse(responseCode = "400", description = "Invalid check-in or already checked-in"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<CheckInApiResponse> checkIn(
            @Parameter(description = "Parking spot row (A-F)", required = true)
            @PathVariable String row,
            @Parameter(description = "Parking spot column (1-10)", required = true)
            @PathVariable String column,
            Authentication authentication
    ) {
        String employeeEmail = authentication.getName();
        parkingSpotService.checkIn(row, column, employeeEmail);
        return ResponseEntity.ok(new CheckInApiResponse("Check-in successful"));
    }
}
