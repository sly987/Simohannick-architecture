package fr.esgi.reseking.service;

import fr.esgi.reseking.controller.dto.ParkingSpotDTO;
import fr.esgi.reseking.controller.validator.CheckInValidator;
import fr.esgi.reseking.exception.DataNotFoundException;
import fr.esgi.reseking.model.ParkingSpot;
import fr.esgi.reseking.model.ReservationDay;
import fr.esgi.reseking.model.enums.Status;
import fr.esgi.reseking.repository.ParkingSpotRepository;
import fr.esgi.reseking.repository.ReservationDayRepository;
import fr.esgi.reseking.mapper.ParkingSpotMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ReservationDayRepository reservationDayRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository, ReservationDayRepository reservationDayRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.reservationDayRepository = reservationDayRepository;
    }

    public List<ParkingSpotDTO> getAllParkingSpots() {
        return parkingSpotRepository.findAll()
                .stream()
                .map(ParkingSpotMapper::toDTO)
                .toList();
    }

    public void checkIn(String row, String column, String employeeEmail) {
        ParkingSpot spot = parkingSpotRepository.findByRowAndColumn(row, column)
                .orElseThrow(() -> new DataNotFoundException(
                        "Parking spot not found at row " + row + " column " + column));

        LocalDate today = LocalDate.now();

        ReservationDay day = reservationDayRepository.findFirstBySpot_IdAndDateAndStatusIn(spot.getId(), today, List.of(Status.BOOKED, Status.CHECKED_IN))
                .orElseThrow(() -> new DataNotFoundException("No active reservation for today on this parking spot"));

        CheckInValidator.validateReservationOwnership(day.getReservation(), employeeEmail);
        CheckInValidator.validateNotAlreadyCheckedIn(day);

        day.setCheckedInAt(LocalDateTime.now());
        day.setStatus(Status.CHECKED_IN);

        reservationDayRepository.save(day);
    }
}
