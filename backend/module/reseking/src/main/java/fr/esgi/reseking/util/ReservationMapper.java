package fr.esgi.reseking.util;

import fr.esgi.reseking.controller.dto.ReservationDTO;
import fr.esgi.reseking.model.ReservationEntity;

public class ReservationMapper {

    public static ReservationEntity map(ReservationDTO reservation) {
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setStartDate(reservation.getStartDate());
        reservationEntity.setDuration(reservation.getDuration());
        reservationEntity.setVehiculeType(reservation.getVehiculeType());
        return reservationEntity;
    }

    public static ReservationDTO map(ReservationEntity reservationEntity) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartDate(reservationEntity.getStartDate());
        reservationDTO.setDuration(reservationEntity.getDuration());
        reservationDTO.setVehiculeType(reservationEntity.getVehiculeType());
        return reservationDTO;
    }
}
