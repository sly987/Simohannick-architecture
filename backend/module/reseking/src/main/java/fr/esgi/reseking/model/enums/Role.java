package fr.esgi.reseking.model.enums;

public enum Role {
    ADMIN(30),
    EMPLOYEE(5),
    MANAGER(30);

    private final long maxReservationDays;

    Role(long maxReservationDays) {
        this.maxReservationDays = maxReservationDays;
    }

    public long getMaxReservationDays() {
        return maxReservationDays;
    }
}
