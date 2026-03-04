package fr.esgi.reseking.util;

import fr.esgi.reseking.model.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservationUtilTest {

    @Test
    void isValidReservationDuration_shouldReturnTrue_whenEmployeeRequestsValidDuration() {
        int requestedDuration = 3;
        Role role = Role.EMPLOYEE;

        boolean result = ReservationUtil.isValidReservationDuration(requestedDuration, role);

        assertTrue(result);
    }

    @Test
    void isValidReservationDuration_shouldReturnTrue_whenEmployeeRequestsMaxDuration() {
        int requestedDuration = 5;
        Role role = Role.EMPLOYEE;

        boolean result = ReservationUtil.isValidReservationDuration(requestedDuration, role);

        assertTrue(result);
    }

    @Test
    void isValidReservationDuration_shouldReturnFalse_whenEmployeeExceedsMaxDuration() {
        int requestedDuration = 6;
        Role role = Role.EMPLOYEE;

        boolean result = ReservationUtil.isValidReservationDuration(requestedDuration, role);

        assertFalse(result);
    }

    @Test
    void isValidReservationDuration_shouldReturnFalse_whenDurationIsZero() {
        int requestedDuration = 0;
        Role role = Role.EMPLOYEE;

        boolean result = ReservationUtil.isValidReservationDuration(requestedDuration, role);

        assertFalse(result);
    }

    @Test
    void isValidReservationDuration_shouldReturnTrue_whenManagerRequestsValidDuration() {
        int requestedDuration = 20;
        Role role = Role.MANAGER;

        boolean result = ReservationUtil.isValidReservationDuration(requestedDuration, role);

        assertTrue(result);
    }

    @Test
    void isValidReservationDuration_shouldReturnTrue_whenManagerRequestsMaxDuration() {
        int requestedDuration = 30;
        Role role = Role.MANAGER;

        boolean result = ReservationUtil.isValidReservationDuration(requestedDuration, role);

        assertTrue(result);
    }

    @Test
    void isValidReservationDuration_shouldReturnFalse_whenManagerExceedsMaxDuration() {
        int requestedDuration = 31;
        Role role = Role.MANAGER;

        boolean result = ReservationUtil.isValidReservationDuration(requestedDuration, role);

        assertFalse(result);
    }

    @Test
    void isValidReservationDuration_shouldReturnTrue_whenAdminRequestsValidDuration() {
        int requestedDuration = 15;
        Role role = Role.ADMIN;

        boolean result = ReservationUtil.isValidReservationDuration(requestedDuration, role);

        assertTrue(result);
    }

    @Test
    void isValidReservationDuration_shouldReturnTrue_whenAdminRequestsMaxDuration() {
        int requestedDuration = 30;
        Role role = Role.ADMIN;

        boolean result = ReservationUtil.isValidReservationDuration(requestedDuration, role);

        assertTrue(result);
    }

    @Test
    void isValidReservationDuration_shouldReturnFalse_whenAdminExceedsMaxDuration() {
        int requestedDuration = 31;
        Role role = Role.ADMIN;

        boolean result = ReservationUtil.isValidReservationDuration(requestedDuration, role);

        assertFalse(result);
    }

    @Test
    void isValidReservationDuration_shouldReturnTrue_whenRequestingOneDayForEmployee() {
        int requestedDuration = 1;
        Role role = Role.EMPLOYEE;

        boolean result = ReservationUtil.isValidReservationDuration(requestedDuration, role);

        assertTrue(result);
    }

    @Test
    void isValidReservationDuration_shouldReturnTrue_whenRequestingOneDayForManager() {
        int requestedDuration = 1;
        Role role = Role.MANAGER;

        boolean result = ReservationUtil.isValidReservationDuration(requestedDuration, role);

        assertTrue(result);
    }

    @Test
    void isValidReservationDuration_shouldReturnTrue_whenRequestingOneDayForAdmin() {
        int requestedDuration = 1;
        Role role = Role.ADMIN;

        boolean result = ReservationUtil.isValidReservationDuration(requestedDuration, role);

        assertTrue(result);
    }
}

