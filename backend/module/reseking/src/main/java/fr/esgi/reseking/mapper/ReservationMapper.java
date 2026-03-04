package fr.esgi.reseking.mapper;

import fr.esgi.reseking.controller.dto.ReservationDTO;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.model.ParkingSpot;
import fr.esgi.reseking.model.Reservation;
import fr.esgi.reseking.model.enums.Status;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    private ReservationMapper() {
    }

    public static Reservation mapToEntity(ReservationDTO dto, Employee employee, ParkingSpot spot) {
        Reservation entity = new Reservation();
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setRegistrationNumber(dto.getRegistrationNumber());
        entity.setEmployee(employee);
        entity.setSpot(spot);
        entity.setStatus(Status.BOOKED);
        return entity;
    }

    public static ReservationDTO mapToDTO(Reservation entity) {
        ReservationDTO dto = new ReservationDTO();

        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setRegistrationNumber(entity.getRegistrationNumber());
        dto.setStatus(entity.getStatus().name());

        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getId());
        }

        if (entity.getSpot() != null) {
            dto.setSpotId(entity.getSpot().getId());
        }
        return dto;
    }
}