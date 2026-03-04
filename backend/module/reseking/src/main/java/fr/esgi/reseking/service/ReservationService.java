package fr.esgi.reseking.service;

import fr.esgi.reseking.controller.dto.ReservationDTO;
import fr.esgi.reseking.exception.DataNotFoundException;
import fr.esgi.reseking.messaging.RabbitProducer;
import fr.esgi.reseking.messaging.event.ReservationCreatedEvent;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.model.ParkingSpot;
import fr.esgi.reseking.model.Reservation;
import fr.esgi.reseking.model.ReservationDay;
import fr.esgi.reseking.model.enums.Status;
import fr.esgi.reseking.repository.EmployeeRepository;
import fr.esgi.reseking.repository.ParkingSpotRepository;
import fr.esgi.reseking.repository.ReservationDayRepository;
import fr.esgi.reseking.repository.ReservationRepository;
import fr.esgi.reseking.util.ReservationMapper;
import fr.esgi.reseking.controller.validator.ReservationValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final EmployeeRepository employeeRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ReservationDayRepository reservationDayRepository;
    private final RabbitProducer rabbitProducer;

    public ReservationService(ReservationRepository reservationRepository,
                              EmployeeRepository employeeRepository,
                              ParkingSpotRepository parkingSpotRepository,
                              ReservationDayRepository reservationDayRepository,
                              RabbitProducer rabbitProducer) {
        this.reservationRepository = reservationRepository;
        this.employeeRepository = employeeRepository;
        this.parkingSpotRepository = parkingSpotRepository;
        this.reservationDayRepository = reservationDayRepository;
        this.rabbitProducer = rabbitProducer;
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

        ReservationValidator.validateEmployeeHasNoActiveReservation(hasActiveReservation, employee.getId());

        List<LocalDate> requestedDays = dto.getStartDate().datesUntil(dto.getEndDate().plusDays(1))
                .filter(date -> date.getDayOfWeek().getValue() < 6)
                .toList();
        ReservationValidator.validateReservationDuration(requestedDays.size(), employee);


        List<ReservationDay> existingDays =
                reservationDayRepository.findBySpot_IdAndDateInAndStatusIn(
                        spot.getId(),
                        requestedDays,
                        List.of(Status.BOOKED, Status.CHECKED_IN)
                );
        ReservationValidator.validateSpotAvailabilityForDays(existingDays, spot.getRow(), spot.getColumn());

        Reservation reservation = ReservationMapper.mapToEntity(dto, employee, spot);
        Reservation saved = reservationRepository.save(reservation);

        List<ReservationDay> days = requestedDays.stream()
                .map(date -> createReservationDay(date, saved, spot))
                .toList();

        reservationDayRepository.saveAll(days);

        ReservationCreatedEvent event = buildReservationCreatedEvent(saved, employee, spot);
        rabbitProducer.sendReservationCreated(event);

        return saved.getId();
    }

    private ReservationDay createReservationDay(LocalDate date, Reservation reservation, ParkingSpot spot) {
        ReservationDay day = new ReservationDay();
        day.setDate(date);
        day.setStatus(Status.BOOKED);
        day.setReservation(reservation);
        day.setSpot(spot);
        return day;
    }

    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationMapper::mapToDTO)
                .toList();
    }

    private ReservationCreatedEvent buildReservationCreatedEvent(Reservation reservation, Employee employee, ParkingSpot spot) {
        return ReservationCreatedEvent.builder()
                .reservationId(reservation.getId())
                .employeeEmail(employee.getEmail())
                .spotLabel(spot.getLabel())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .build();
    }

    public void deleteReservation(Integer reservationId) {
        reservationRepository.findById(reservationId)
                .ifPresent(reservationRepository::delete);
    }
}

