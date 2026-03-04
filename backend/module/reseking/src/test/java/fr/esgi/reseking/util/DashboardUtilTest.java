package fr.esgi.reseking.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DashboardUtilTest {

    @Test
    void calculateAverageOccupancyRate_shouldReturnZero_whenTotalSpotsIsZero() {
        LocalDate from = LocalDate.of(2026, 3, 1);
        LocalDate to = LocalDate.of(2026, 3, 5);
        long totalSpots = 0;
        long occupiedDays = 10;

        double result = DashboardUtil.calculateAverageOccupancyRate(from, to, totalSpots, occupiedDays);

        assertEquals(0.0, result);
    }

    @Test
    void calculateAverageOccupancyRate_shouldReturnCorrectRate_whenValidInputs() {
        LocalDate from = LocalDate.of(2026, 3, 1);
        LocalDate to = LocalDate.of(2026, 3, 5);
        long totalSpots = 10;
        long occupiedDays = 30;

        double result = DashboardUtil.calculateAverageOccupancyRate(from, to, totalSpots, occupiedDays);

        double expectedRate = (30.0 / (10 * 5)) * 100;
        assertEquals(expectedRate, result);
    }

    @Test
    void calculateAverageOccupancyRate_shouldReturn100_whenFullyOccupied() {
        LocalDate from = LocalDate.of(2026, 3, 1);
        LocalDate to = LocalDate.of(2026, 3, 3);
        long totalSpots = 5;
        long occupiedDays = 15;

        double result = DashboardUtil.calculateAverageOccupancyRate(from, to, totalSpots, occupiedDays);

        assertEquals(100.0, result);
    }

    @Test
    void calculateAverageOccupancyRate_shouldReturn50_whenHalfOccupied() {
        LocalDate from = LocalDate.of(2026, 3, 1);
        LocalDate to = LocalDate.of(2026, 3, 10);
        long totalSpots = 10;
        long occupiedDays = 50;

        double result = DashboardUtil.calculateAverageOccupancyRate(from, to, totalSpots, occupiedDays);

        assertEquals(50.0, result);
    }

    @Test
    void calculateAverageOccupancyRate_shouldHandleSingleDay() {
        LocalDate from = LocalDate.of(2026, 3, 1);
        LocalDate to = LocalDate.of(2026, 3, 1);
        long totalSpots = 20;
        long occupiedDays = 10;

        double result = DashboardUtil.calculateAverageOccupancyRate(from, to, totalSpots, occupiedDays);

        assertEquals(50.0, result);
    }

    @Test
    void calculateRate_shouldReturnZero_whenTotalIsZero() {
        long total = 0;
        long count = 10;

        double result = DashboardUtil.calculateRate(total, count);

        assertEquals(0.0, result);
    }

    @Test
    void calculateRate_shouldReturnCorrectRate_whenValidInputs() {
        long total = 100;
        long count = 25;

        double result = DashboardUtil.calculateRate(total, count);

        assertEquals(25.0, result);
    }

    @Test
    void calculateRate_shouldReturn100_whenCountEqualsTotal() {
        long total = 50;
        long count = 50;

        double result = DashboardUtil.calculateRate(total, count);

        assertEquals(100.0, result);
    }

    @Test
    void calculateRate_shouldReturnZero_whenCountIsZero() {
        long total = 100;
        long count = 0;

        double result = DashboardUtil.calculateRate(total, count);

        assertEquals(0.0, result);
    }

    @Test
    void calculateRate_shouldHandleDecimalResults() {
        long total = 3;
        long count = 1;

        double result = DashboardUtil.calculateRate(total, count);

        assertEquals(33.33, result, 0.01);
    }
}

