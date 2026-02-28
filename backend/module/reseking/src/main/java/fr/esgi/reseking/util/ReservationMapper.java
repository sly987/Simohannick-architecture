package fr.esgi.reseking.util;

import fr.esgi.reseking.controller.dto.ReservationDTO;
import fr.esgi.reseking.exception.EmployeeNotFoundException;
import fr.esgi.reseking.exception.ParkingSpotNotFoundException;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.model.ParkingSpot;
import fr.esgi.reseking.model.Reservation;
import fr.esgi.reseking.repository.EmployeeRepository;
import fr.esgi.reseking.repository.ParkingSpotRepository;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    private ReservationMapper() {
    }

    public static Reservation mapToEntity(ReservationDTO dto, Employee employee, ParkingSpot spot) {
        Reservation entity = new Reservation();
        entity.setStartDate(dto.getStartDate());
        entity.setDuration(dto.getDuration());
        entity.setRegistrationNumber(dto.getRegistrationNumber());
        entity.setEmployee(employee);
        entity.setSpot(spot);
        return entity;
    }

    public static ReservationDTO mapToDTO(Reservation entity) {
        ReservationDTO dto = new ReservationDTO();

        dto.setStartDate(entity.getStartDate());
        dto.setDuration(entity.getDuration());
        dto.setRegistrationNumber(entity.getRegistrationNumber());

        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getId());
        }

        if (entity.getSpot() != null) {
            dto.setSpotId(entity.getSpot().getId());
        }
        return dto;
    }
}