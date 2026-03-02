package fr.esgi.reseking.util;

import fr.esgi.reseking.model.enums.Role;

import java.time.LocalDate;

public class ReservationUtil {

    private ReservationUtil() {
    }

    public static boolean isValidReservationDuration(LocalDate startDate, LocalDate endDate, Role role) {
        long workingDaysCount = startDate.datesUntil(endDate.plusDays(1))
                .filter(date -> date.getDayOfWeek().getValue() < 6)
                .count();
        return workingDaysCount <= role.getMaxReservationDays();
    }
}

