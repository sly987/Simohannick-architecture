package fr.esgi.reseking.service;

import fr.esgi.reseking.controller.dto.ParkingSpotDTO;
import fr.esgi.reseking.controller.validator.CheckInValidator;
import fr.esgi.reseking.exception.DataNotFoundException;
import fr.esgi.reseking.model.ParkingSpot;
import fr.esgi.reseking.model.Reservation;
import fr.esgi.reseking.model.enums.Status;
import fr.esgi.reseking.repository.ParkingSpotRepository;
import fr.esgi.reseking.repository.ReservationRepository;
import fr.esgi.reseking.util.ParkingSpotMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ReservationRepository reservationRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository, ReservationRepository reservationRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ParkingSpotDTO> getAllParkingSpots() {
        return parkingSpotRepository.findAll()
                .stream()
                .map(ParkingSpotMapper::toDTO)
                .toList();
    }

    public void checkIn(String row, String column, String employeeEmail) {
        ParkingSpot spot = parkingSpotRepository.findByRowAndColumn(row, column)
                .orElseThrow(() -> new DataNotFoundException("Parking spot not found at row " + row + " column " + column));

        LocalDate today = LocalDate.now();
        Reservation activeReservation = reservationRepository.findActiveForSpotOnDate(spot.getId(), today, List.of(Status.BOOKED))
                .orElseThrow(() -> new DataNotFoundException("No active reservation found for this parking spot"));

        CheckInValidator.validateReservationOwnership(activeReservation, employeeEmail);
        CheckInValidator.validateNotAlreadyCheckedIn(activeReservation, today);

        activeReservation.addCheckIn(today, LocalDateTime.now());

        reservationRepository.save(activeReservation);
    }
}
