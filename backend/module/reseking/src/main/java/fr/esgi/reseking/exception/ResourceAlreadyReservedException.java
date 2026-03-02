package fr.esgi.reseking.exception;

public class ResourceAlreadyReservedException extends RuntimeException {
    public ResourceAlreadyReservedException(String message) {
        super(message);
    }
}

