package fr.esgi.reseking.controller;

import fr.esgi.reseking.controller.dto.ManagerDashboardDTO;
import fr.esgi.reseking.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/manager/dashboard")
@Tag(name = "Manager Dashboard", description = "Manager dashboard endpoints for analytics and statistics")
@SecurityRequirement(name = "bearerAuth")
public class ManagerDashboardController {

    private final DashboardService dashboardService;

    public ManagerDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    @Operation(summary = "Get dashboard statistics", description = "Retrieve dashboard statistics for a given date range including unique users, occupancy rate, no-show rate, and electric spot ratio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard statistics retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ManagerDashboardDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date range"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Manager access required")
    })
    public ManagerDashboardDTO getDashboard(
            @Parameter(description = "Start date (ISO format: YYYY-MM-DD)", required = true, example = "2026-03-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "End date (ISO format: YYYY-MM-DD)", required = true, example = "2026-03-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return dashboardService.getManagerDashboard(from, to);
    }
}

