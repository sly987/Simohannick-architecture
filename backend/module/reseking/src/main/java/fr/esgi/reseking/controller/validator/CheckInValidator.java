package fr.esgi.reseking.controller.validator;

import fr.esgi.reseking.exception.ResourceAlreadyReservedException;
import fr.esgi.reseking.model.Reservation;
import fr.esgi.reseking.model.ReservationDay;
import fr.esgi.reseking.model.enums.Status;


public class CheckInValidator {

    private CheckInValidator() {
    }

    public static void validateReservationOwnership(Reservation reservation, String employeeEmail) {
        if (!reservation.getEmployee().getEmail().equals(employeeEmail)) {
            throw new ResourceAlreadyReservedException("This reservation belongs to another employee");
        }
    }

    public static void validateNotAlreadyCheckedIn(ReservationDay reservationDay) {
        if (Status.CHECKED_IN == reservationDay.getStatus()) {
            throw new ResourceAlreadyReservedException("Already checked in for date " + reservationDay.getDate());
        }
    }
}
