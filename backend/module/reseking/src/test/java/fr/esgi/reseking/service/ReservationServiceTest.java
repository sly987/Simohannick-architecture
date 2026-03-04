package fr.esgi.reseking.service;

import fr.esgi.reseking.controller.dto.ReservationDTO;
import fr.esgi.reseking.exception.DataNotFoundException;
import fr.esgi.reseking.exception.ReservationDurationExceededException;
import fr.esgi.reseking.exception.ResourceAlreadyReservedException;
import fr.esgi.reseking.messaging.RabbitProducer;
import fr.esgi.reseking.messaging.event.ReservationCreatedEvent;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.model.ParkingSpot;
import fr.esgi.reseking.model.Reservation;
import fr.esgi.reseking.model.ReservationDay;
import fr.esgi.reseking.model.enums.Role;
import fr.esgi.reseking.model.enums.SpotType;
import fr.esgi.reseking.model.enums.Status;
import fr.esgi.reseking.repository.EmployeeRepository;
import fr.esgi.reseking.repository.ParkingSpotRepository;
import fr.esgi.reseking.repository.ReservationDayRepository;
import fr.esgi.reseking.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @Mock
    private ReservationDayRepository reservationDayRepository;

    @Mock
    private RabbitProducer rabbitProducer;

    @InjectMocks
    private ReservationService reservationService;

    private Employee employee;
    private ParkingSpot parkingSpot;
    private ReservationDTO reservationDTO;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1);
        employee.setEmail("test@example.com");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setRole(Role.EMPLOYEE);

        parkingSpot = new ParkingSpot();
        parkingSpot.setId(1);
        parkingSpot.setRow("A");
        parkingSpot.setColumn("01");
        parkingSpot.setType(SpotType.STANDARD);

        reservationDTO = new ReservationDTO();
        reservationDTO.setEmployeeId(1);
        reservationDTO.setSpotId(1);
        reservationDTO.setStartDate(LocalDate.of(2026, 3, 10));
        reservationDTO.setEndDate(LocalDate.of(2026, 3, 12));
        reservationDTO.setRegistrationNumber("ABC123");

        reservation = new Reservation();
        reservation.setId(1);
        reservation.setEmployee(employee);
        reservation.setSpot(parkingSpot);
        reservation.setStartDate(reservationDTO.getStartDate());
        reservation.setEndDate(reservationDTO.getEndDate());
        reservation.setRegistrationNumber(reservationDTO.getRegistrationNumber());
        reservation.setStatus(Status.BOOKED);
    }

    @Test
    void addReservation_shouldSucceed_whenValidData() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(parkingSpotRepository.findById(1)).thenReturn(Optional.of(parkingSpot));
        when(reservationRepository.existsByEmployeeAndStatusIn(employee, Status.BOOKED)).thenReturn(false);
        when(reservationDayRepository.findBySpot_IdAndDateInAndStatusIn(
                eq(1),
                anyList(),
                eq(List.of(Status.BOOKED, Status.CHECKED_IN))
        )).thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Integer result = reservationService.addReservation(reservationDTO);

        assertNotNull(result);
        assertEquals(1, result);
        verify(employeeRepository, times(1)).findById(1);
        verify(parkingSpotRepository, times(1)).findById(1);
        verify(reservationRepository, times(1)).existsByEmployeeAndStatusIn(employee, Status.BOOKED);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(reservationDayRepository, times(1)).saveAll(anyList());
        verify(rabbitProducer, times(1)).sendReservationCreated(any(ReservationCreatedEvent.class));
    }

    @Test
    void addReservation_shouldThrowException_whenEmployeeNotFound() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> reservationService.addReservation(reservationDTO)
        );

        assertEquals("Employee not found with id: 1", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1);
        verify(parkingSpotRepository, never()).findById(anyInt());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void addReservation_shouldThrowException_whenParkingSpotNotFound() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(parkingSpotRepository.findById(1)).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> reservationService.addReservation(reservationDTO)
        );

        assertEquals("Parking spot not found with id: 1", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1);
        verify(parkingSpotRepository, times(1)).findById(1);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void addReservation_shouldThrowException_whenEmployeeHasActiveReservation() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(parkingSpotRepository.findById(1)).thenReturn(Optional.of(parkingSpot));
        when(reservationRepository.existsByEmployeeAndStatusIn(employee, Status.BOOKED)).thenReturn(true);

        ResourceAlreadyReservedException exception = assertThrows(
                ResourceAlreadyReservedException.class,
                () -> reservationService.addReservation(reservationDTO)
        );

        assertEquals("Employee with id 1 already has an active reservation.", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1);
        verify(parkingSpotRepository, times(1)).findById(1);
        verify(reservationRepository, times(1)).existsByEmployeeAndStatusIn(employee, Status.BOOKED);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void addReservation_shouldThrowException_whenDurationExceedsLimit() {
        reservationDTO.setStartDate(LocalDate.of(2026, 3, 10));
        reservationDTO.setEndDate(LocalDate.of(2026, 3, 20));

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(parkingSpotRepository.findById(1)).thenReturn(Optional.of(parkingSpot));
        when(reservationRepository.existsByEmployeeAndStatusIn(employee, Status.BOOKED)).thenReturn(false);

        ReservationDurationExceededException exception = assertThrows(
                ReservationDurationExceededException.class,
                () -> reservationService.addReservation(reservationDTO)
        );

        assertTrue(exception.getMessage().contains("Reservation duration exceeds the maximum allowed for role"));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void addReservation_shouldThrowException_whenSpotAlreadyOccupied() {
        ReservationDay occupiedDay = new ReservationDay();
        occupiedDay.setId(1);
        occupiedDay.setDate(LocalDate.of(2026, 3, 10));
        occupiedDay.setStatus(Status.BOOKED);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(parkingSpotRepository.findById(1)).thenReturn(Optional.of(parkingSpot));
        when(reservationRepository.existsByEmployeeAndStatusIn(employee, Status.BOOKED)).thenReturn(false);
        when(reservationDayRepository.findBySpot_IdAndDateInAndStatusIn(
                eq(1),
                anyList(),
                eq(List.of(Status.BOOKED, Status.CHECKED_IN))
        )).thenReturn(List.of(occupiedDay));

        ResourceAlreadyReservedException exception = assertThrows(
                ResourceAlreadyReservedException.class,
                () -> reservationService.addReservation(reservationDTO)
        );

        assertTrue(exception.getMessage().contains("is already occupied for one or more requested days"));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void addReservation_shouldFilterWeekends() {
        reservationDTO.setStartDate(LocalDate.of(2026, 3, 6));
        reservationDTO.setEndDate(LocalDate.of(2026, 3, 9));

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(parkingSpotRepository.findById(1)).thenReturn(Optional.of(parkingSpot));
        when(reservationRepository.existsByEmployeeAndStatusIn(employee, Status.BOOKED)).thenReturn(false);
        when(reservationDayRepository.findBySpot_IdAndDateInAndStatusIn(
                eq(1),
                anyList(),
                eq(List.of(Status.BOOKED, Status.CHECKED_IN))
        )).thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        reservationService.addReservation(reservationDTO);

        verify(reservationDayRepository).saveAll(argThat(days -> {
            List<ReservationDay> dayList = (List<ReservationDay>) days;
            return dayList.size() == 2;
        }));
    }

    @Test
    void addReservation_shouldSucceed_whenManagerReservesForMaxDuration() {
        employee.setRole(Role.MANAGER);
        reservationDTO.setStartDate(LocalDate.of(2026, 3, 10));
        reservationDTO.setEndDate(LocalDate.of(2026, 4, 16));

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(parkingSpotRepository.findById(1)).thenReturn(Optional.of(parkingSpot));
        when(reservationRepository.existsByEmployeeAndStatusIn(employee, Status.BOOKED)).thenReturn(false);
        when(reservationDayRepository.findBySpot_IdAndDateInAndStatusIn(
                eq(1),
                anyList(),
                eq(List.of(Status.BOOKED, Status.CHECKED_IN))
        )).thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Integer result = reservationService.addReservation(reservationDTO);

        assertNotNull(result);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void getAllReservations_shouldReturnListOfReservations() {
        Reservation reservation1 = new Reservation();
        reservation1.setId(1);
        reservation1.setEmployee(employee);
        reservation1.setSpot(parkingSpot);
        reservation1.setStatus(Status.BOOKED);

        Reservation reservation2 = new Reservation();
        reservation2.setId(2);
        reservation2.setEmployee(employee);
        reservation2.setSpot(parkingSpot);
        reservation2.setStatus(Status.BOOKED);

        when(reservationRepository.findAll()).thenReturn(List.of(reservation1, reservation2));

        List<ReservationDTO> result = reservationService.getAllReservations();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void getAllReservations_shouldReturnEmptyList_whenNoReservationsExist() {
        when(reservationRepository.findAll()).thenReturn(List.of());

        List<ReservationDTO> result = reservationService.getAllReservations();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void deleteReservation_shouldSucceed_whenReservationExists() {
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));

        reservationService.deleteReservation(1);

        verify(reservationRepository, times(1)).findById(1);
        verify(reservationRepository, times(1)).delete(reservation);
    }

    @Test
    void deleteReservation_shouldDoNothing_whenReservationDoesNotExist() {
        when(reservationRepository.findById(999)).thenReturn(Optional.empty());

        reservationService.deleteReservation(999);

        verify(reservationRepository, times(1)).findById(999);
        verify(reservationRepository, never()).delete(any());
    }

    @Test
    void cancelReservation_shouldSucceed_whenReservationExists() {
        ReservationDay day1 = new ReservationDay();
        day1.setId(1);
        day1.setReservation(reservation);

        ReservationDay day2 = new ReservationDay();
        day2.setId(2);
        day2.setReservation(reservation);

        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));
        when(reservationDayRepository.findByReservation_Id(1)).thenReturn(List.of(day1, day2));

        reservationService.cancelReservation(1);

        assertEquals(Status.CANCELLED, reservation.getStatus());
        verify(reservationRepository, times(1)).findById(1);
        verify(reservationRepository, times(1)).save(reservation);
        verify(reservationDayRepository, times(1)).findByReservation_Id(1);
        verify(reservationDayRepository, times(1)).deleteAll(List.of(day1, day2));
    }

    @Test
    void cancelReservation_shouldThrowException_whenReservationNotFound() {
        when(reservationRepository.findById(999)).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> reservationService.cancelReservation(999)
        );

        assertEquals("Reservation not found with id: 999", exception.getMessage());
        verify(reservationRepository, times(1)).findById(999);
        verify(reservationRepository, never()).save(any());
        verify(reservationDayRepository, never()).deleteAll(anyList());
    }

    @Test
    void cancelReservation_shouldDeleteAllAssociatedDays() {
        List<ReservationDay> days = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ReservationDay day = new ReservationDay();
            day.setId(i + 1);
            day.setReservation(reservation);
            days.add(day);
        }

        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));
        when(reservationDayRepository.findByReservation_Id(1)).thenReturn(days);

        reservationService.cancelReservation(1);

        verify(reservationDayRepository, times(1)).deleteAll(argThat(list ->
            ((List<ReservationDay>) list).size() == 5
        ));
    }
}

