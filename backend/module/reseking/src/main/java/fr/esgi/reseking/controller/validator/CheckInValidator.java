package fr.esgi.reseking.controller.validator;

import fr.esgi.reseking.exception.ResourceAlreadyReservedException;
import fr.esgi.reseking.model.Reservation;

import java.time.LocalDate;


public class CheckInValidator {

    private CheckInValidator() {
    }

    public static void validateReservationOwnership(Reservation reservation, String employeeEmail) {
        if (!reservation.getEmployee().getEmail().equals(employeeEmail)) {
            throw new ResourceAlreadyReservedException("This reservation belongs to another employee");
        }
    }

    public static void validateNotAlreadyCheckedIn(Reservation reservation, LocalDate date) {
        if (reservation.isCheckedInForDate(date)) {
            throw new ResourceAlreadyReservedException("Already checked in for date " + date);
        }
    }
}
