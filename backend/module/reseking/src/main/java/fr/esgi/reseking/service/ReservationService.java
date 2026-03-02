package fr.esgi.reseking.service;

import fr.esgi.reseking.controller.dto.ReservationDTO;
import fr.esgi.reseking.exception.DataNotFoundException;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.model.ParkingSpot;
import fr.esgi.reseking.model.Reservation;
import fr.esgi.reseking.model.enums.Status;
import fr.esgi.reseking.repository.EmployeeRepository;
import fr.esgi.reseking.repository.ParkingSpotRepository;
import fr.esgi.reseking.repository.ReservationRepository;
import fr.esgi.reseking.util.ReservationMapper;
import fr.esgi.reseking.controller.validator.ReservationValidator;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException("Employee not found with id: " + dto.getEmployeeId()));

        ParkingSpot spot = parkingSpotRepository.findById(dto.getSpotId())
                .orElseThrow(() -> new DataNotFoundException("Parking spot not found with id: " + dto.getSpotId()));

        boolean hasActiveReservation = reservationRepository.existsByEmployeeAndStatusIn(
                employee,
                Status.BOOKED
        );

        boolean isSpotOccupied = reservationRepository.existsBySpotAndStatusIn(
                spot,
                Status.BOOKED
        );

        ReservationValidator.validateEmployeeHasNoActiveReservation(hasActiveReservation, employee.getId());
        ReservationValidator.validateSpotIsAvailable(isSpotOccupied, spot.getRow(), spot.getColumn());
        ReservationValidator.validateReservationDuration(dto, employee);

        Reservation reservation = ReservationMapper.mapToEntity(dto, employee, spot);
        Reservation saved = reservationRepository.save(reservation);

        return saved.getId();
    }

    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationMapper::mapToDTO)
                .toList();
    }
}

