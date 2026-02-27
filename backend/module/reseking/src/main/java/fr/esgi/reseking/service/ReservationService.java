package fr.esgi.reseking.service;

import fr.esgi.reseking.controller.dto.ReservationDTO;
import fr.esgi.reseking.exception.EmployeeNotFoundException;
import fr.esgi.reseking.exception.ParkingSpotNotFoundException;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.model.ParkingSpot;
import fr.esgi.reseking.model.Reservation;
import fr.esgi.reseking.repository.EmployeeRepository;
import fr.esgi.reseking.repository.ParkingSpotRepository;
import fr.esgi.reseking.repository.ReservationRepository;
import fr.esgi.reseking.util.ReservationMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final EmployeeRepository employeeRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              EmployeeRepository employeeRepository,
                              ParkingSpotRepository parkingSpotRepository) {
        this.reservationRepository = reservationRepository;
        this.employeeRepository = employeeRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    public Integer addReservation(ReservationDTO dto) {
        Employee employee = Optional.ofNullable(dto.getEmployeeId())
                .map(id -> employeeRepository.findById(id)
                        .orElseThrow(() -> new EmployeeNotFoundException(id)))
                .orElseThrow(() -> new IllegalArgumentException("Employee ID is required"));

        ParkingSpot spot = Optional.ofNullable(dto.getSpotId())
                .map(id -> parkingSpotRepository.findById(id)
                        .orElseThrow(() -> new ParkingSpotNotFoundException(id)))
                .orElseThrow(() -> new IllegalArgumentException("Spot ID is required"));

        Reservation reservation = ReservationMapper.mapToEntity(dto, employee, spot);
        Reservation saved = reservationRepository.save(reservation);

        employee.setActiveReservation(saved);
        employeeRepository.save(employee);

        spot.setActiveReservation(saved);
        parkingSpotRepository.save(spot);

        return saved.getId();
    }

    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationMapper::mapToDTO)
                .toList();
    }


}