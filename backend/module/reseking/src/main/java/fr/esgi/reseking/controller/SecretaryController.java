package fr.esgi.reseking.controller;

import fr.esgi.reseking.controller.dto.CreateEmployeeDTO;
import fr.esgi.reseking.controller.dto.EmployeeDTO;
import fr.esgi.reseking.controller.response.CreationApiResponse;
import fr.esgi.reseking.controller.validator.EmployeeValidator;
import fr.esgi.reseking.service.EmployeeService;
import fr.esgi.reseking.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/secretary")
@Tag(name = "Secretary", description = "Secretary/Admin endpoints for managing employees and reservations")
@SecurityRequirement(name = "bearerAuth")
public class SecretaryController {

    private final EmployeeService employeeService;
    private final ReservationService reservationService;

    public SecretaryController(EmployeeService employeeService, ReservationService reservationService) {
        this.employeeService = employeeService;
        this.reservationService = reservationService;
    }

    @PostMapping("/employees")
    @Operation(summary = "Create employee", description = "Create a new employee in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreationApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate email"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<CreationApiResponse> addEmployee(@RequestBody CreateEmployeeDTO createEmployeeDTO) {
        EmployeeValidator.validateEmployeeInput(createEmployeeDTO);
        Integer employeeId = employeeService.addEmployee(createEmployeeDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreationApiResponse("Employee created successfully", employeeId));
    }

    @GetMapping("/employees")
    @Operation(summary = "Get all employees", description = "Retrieve all employees in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @DeleteMapping("/reservations")
    @Operation(summary = "Delete reservation", description = "Delete a reservation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reservation deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<Void> deleteReservation(
            @Parameter(description = "Reservation ID to delete", required = true)
            @RequestParam Integer id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reservations/cancel")
    @Operation(summary = "Cancel reservation", description = "Cancel a reservation by ID and remove associated reservation days")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reservation cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Reservation not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<Void> cancelReservation(
            @Parameter(description = "Reservation ID to cancel", required = true)
            @RequestParam Integer id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}
