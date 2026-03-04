package fr.esgi.reseking.controller.validator;

import fr.esgi.reseking.controller.dto.ReservationDTO;
import fr.esgi.reseking.exception.ReservationDurationExceededException;
import fr.esgi.reseking.exception.ResourceAlreadyReservedException;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.model.ReservationDay;
import fr.esgi.reseking.util.ReservationUtil;

import java.time.LocalDate;
import java.util.List;

public class ReservationValidator {

    private ReservationValidator() {
    }

    public static void validateReservationInput(ReservationDTO dto) {
        if (dto.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        if (dto.getSpotId() == null) {
            throw new IllegalArgumentException("Spot ID is required");
        }

        if (dto.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required");
        }
        if (dto.getEndDate() == null) {
            throw new IllegalArgumentException("End date is required");
        }

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }

        if (dto.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
    }

    public static void validateEmployeeHasNoActiveReservation(boolean hasActiveReservation, Integer employeeId) {
        if (hasActiveReservation) {
            throw new ResourceAlreadyReservedException("Employee with id " + employeeId + " already has an active reservation.");
        }
    }

    public static void validateReservationDuration(int requestedDuration, Employee employee) {
        if (!ReservationUtil.isValidReservationDuration(requestedDuration, employee.getRole())) {
            throw new ReservationDurationExceededException(
                    "Reservation duration exceeds the maximum allowed for role " + employee.getRole().name()
            );
        }
    }

    public static void validateSpotAvailabilityForDays(List<ReservationDay> existingDays, String row, String column) {
        if (!existingDays.isEmpty()) {
            throw new ResourceAlreadyReservedException("Parking spot " + row + column + " is already occupied for one or more requested days.");
        }
    }
}
