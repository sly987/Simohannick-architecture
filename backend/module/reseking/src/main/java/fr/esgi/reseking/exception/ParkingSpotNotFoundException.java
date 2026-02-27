package fr.esgi.reseking.exception;

public class ParkingSpotNotFoundException extends RuntimeException {

    public ParkingSpotNotFoundException(Integer spotId) {
        super("Parking spot with ID " + spotId + " not found");
    }
}