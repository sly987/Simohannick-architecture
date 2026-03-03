package fr.esgi.reseking.util;

import java.time.LocalDate;

public class DashboardUtil {

    public static double calculateAverageOccupancyRate(LocalDate from, LocalDate to, long totalSpots, long occupiedDays) {
        double rate = 0.0;

        if (totalSpots > 0) {
            long totalDays = from.datesUntil(to.plusDays(1)).count();
            long totalCapacity = totalSpots * totalDays;
            rate = totalCapacity > 0 ? (double) occupiedDays / totalCapacity * 100 : 0.0;
        }

        return rate;
    }

    public static double calculateRate(long total, long count) {
        double rate = 0.0;

        if (total > 0) {
            rate = (double) count / total * 100;
        }

        return rate;
    }
}

