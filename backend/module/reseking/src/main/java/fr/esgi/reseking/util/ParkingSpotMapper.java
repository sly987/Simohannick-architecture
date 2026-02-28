package fr.esgi.reseking.util;

import fr.esgi.reseking.controller.dto.ParkingSpotDTO;
import fr.esgi.reseking.model.ParkingSpot;

import java.util.List;

public class ParkingSpotMapper {

    public static ParkingSpotDTO toDTO(ParkingSpot spot) {

        List<String> reservationIds = null;

        if (spot.getReservations() != null) {
            reservationIds = spot.getReservations()
                    .stream()
                    .map(reservation -> String.valueOf(reservation.getId()))
                    .toList();
        }

        String activeReservationId = null;

        if (spot.getActiveReservation() != null) {
            activeReservationId = String.valueOf(spot.getActiveReservation().getId());
        }

        return new ParkingSpotDTO(
                spot.getId(),
                spot.getRow(),
                spot.getColumn(),
                spot.getType(),
                reservationIds,
                activeReservationId
        );
    }
}
