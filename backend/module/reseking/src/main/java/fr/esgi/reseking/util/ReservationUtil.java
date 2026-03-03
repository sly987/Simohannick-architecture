package fr.esgi.reseking.util;

import fr.esgi.reseking.model.enums.Role;

import java.time.LocalDate;

public class ReservationUtil {

    private ReservationUtil() {
    }

    public static boolean isValidReservationDuration(int requestedDuration, Role role) {
        return requestedDuration <= role.getMaxReservationDays() && requestedDuration != 0;
    }
}

