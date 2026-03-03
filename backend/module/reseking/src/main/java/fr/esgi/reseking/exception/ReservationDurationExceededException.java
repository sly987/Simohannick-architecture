package fr.esgi.reseking.exception;

public class ReservationDurationExceededException extends RuntimeException {
    public ReservationDurationExceededException(String message) {
        super(message);
    }
}

