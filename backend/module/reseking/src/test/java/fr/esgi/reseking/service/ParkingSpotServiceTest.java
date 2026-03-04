package fr.esgi.reseking.service;

import fr.esgi.reseking.controller.dto.ParkingSpotDTO;
import fr.esgi.reseking.exception.DataNotFoundException;
import fr.esgi.reseking.exception.ResourceAlreadyReservedException;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.model.ParkingSpot;
import fr.esgi.reseking.model.Reservation;
import fr.esgi.reseking.model.ReservationDay;
import fr.esgi.reseking.model.enums.Role;
import fr.esgi.reseking.model.enums.SpotType;
import fr.esgi.reseking.model.enums.Status;
import fr.esgi.reseking.repository.ParkingSpotRepository;
import fr.esgi.reseking.repository.ReservationDayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingSpotServiceTest {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @Mock
    private ReservationDayRepository reservationDayRepository;

    @InjectMocks
    private ParkingSpotService parkingSpotService;

    private ParkingSpot parkingSpot;
    private ReservationDay reservationDay;
    private Reservation reservation;
    private Employee employee;

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

        reservation = new Reservation();
        reservation.setId(1);
        reservation.setEmployee(employee);
        reservation.setSpot(parkingSpot);
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.now().plusDays(2));
        reservation.setStatus(Status.BOOKED);

        reservationDay = new ReservationDay();
        reservationDay.setId(1);
        reservationDay.setDate(LocalDate.now());
        reservationDay.setStatus(Status.BOOKED);
        reservationDay.setReservation(reservation);
        reservationDay.setSpot(parkingSpot);
    }

    @Test
    void getAllParkingSpots_shouldReturnListOfParkingSpotDTOs() {
        ParkingSpot spot1 = new ParkingSpot();
        spot1.setId(1);
        spot1.setRow("A");
        spot1.setColumn("01");
        spot1.setType(SpotType.STANDARD);

        ParkingSpot spot2 = new ParkingSpot();
        spot2.setId(2);
        spot2.setRow("A");
        spot2.setColumn("02");
        spot2.setType(SpotType.ELECTRIC);

        when(parkingSpotRepository.findAll()).thenReturn(List.of(spot1, spot2));

        List<ParkingSpotDTO> result = parkingSpotService.getAllParkingSpots();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(parkingSpotRepository, times(1)).findAll();
    }

    @Test
    void getAllParkingSpots_shouldReturnEmptyList_whenNoSpotsExist() {
        when(parkingSpotRepository.findAll()).thenReturn(List.of());

        List<ParkingSpotDTO> result = parkingSpotService.getAllParkingSpots();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(parkingSpotRepository, times(1)).findAll();
    }

    @Test
    void checkIn_shouldSucceed_whenValidReservationExists() {
        when(parkingSpotRepository.findByRowAndColumn("A", "01")).thenReturn(Optional.of(parkingSpot));
        when(reservationDayRepository.findFirstBySpot_IdAndDateAndStatusIn(
                eq(1),
                any(LocalDate.class),
                eq(List.of(Status.BOOKED, Status.CHECKED_IN))
        )).thenReturn(Optional.of(reservationDay));

        parkingSpotService.checkIn("A", "01", "test@example.com");

        verify(parkingSpotRepository, times(1)).findByRowAndColumn("A", "01");
        verify(reservationDayRepository, times(1)).findFirstBySpot_IdAndDateAndStatusIn(
                eq(1),
                any(LocalDate.class),
                eq(List.of(Status.BOOKED, Status.CHECKED_IN))
        );
        verify(reservationDayRepository, times(1)).save(any(ReservationDay.class));
        assertEquals(Status.CHECKED_IN, reservationDay.getStatus());
        assertNotNull(reservationDay.getCheckedInAt());
    }

    @Test
    void checkIn_shouldThrowException_whenParkingSpotNotFound() {
        when(parkingSpotRepository.findByRowAndColumn("Z", "99")).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> parkingSpotService.checkIn("Z", "99", "test@example.com")
        );

        assertEquals("Parking spot not found at row Z column 99", exception.getMessage());
        verify(parkingSpotRepository, times(1)).findByRowAndColumn("Z", "99");
        verify(reservationDayRepository, never()).findFirstBySpot_IdAndDateAndStatusIn(anyInt(), any(), anyList());
        verify(reservationDayRepository, never()).save(any());
    }

    @Test
    void checkIn_shouldThrowException_whenNoActiveReservationForToday() {
        when(parkingSpotRepository.findByRowAndColumn("A", "01")).thenReturn(Optional.of(parkingSpot));
        when(reservationDayRepository.findFirstBySpot_IdAndDateAndStatusIn(
                eq(1),
                any(LocalDate.class),
                eq(List.of(Status.BOOKED, Status.CHECKED_IN))
        )).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                () -> parkingSpotService.checkIn("A", "01", "test@example.com")
        );

        assertEquals("No active reservation for today on this parking spot", exception.getMessage());
        verify(parkingSpotRepository, times(1)).findByRowAndColumn("A", "01");
        verify(reservationDayRepository, times(1)).findFirstBySpot_IdAndDateAndStatusIn(
                eq(1),
                any(LocalDate.class),
                eq(List.of(Status.BOOKED, Status.CHECKED_IN))
        );
        verify(reservationDayRepository, never()).save(any());
    }

    @Test
    void checkIn_shouldThrowException_whenReservationBelongsToAnotherEmployee() {
        when(parkingSpotRepository.findByRowAndColumn("A", "01")).thenReturn(Optional.of(parkingSpot));
        when(reservationDayRepository.findFirstBySpot_IdAndDateAndStatusIn(
                eq(1),
                any(LocalDate.class),
                eq(List.of(Status.BOOKED, Status.CHECKED_IN))
        )).thenReturn(Optional.of(reservationDay));

        ResourceAlreadyReservedException exception = assertThrows(
                ResourceAlreadyReservedException.class,
                () -> parkingSpotService.checkIn("A", "01", "wrong@example.com")
        );

        assertEquals("This reservation belongs to another employee", exception.getMessage());
        verify(parkingSpotRepository, times(1)).findByRowAndColumn("A", "01");
        verify(reservationDayRepository, never()).save(any());
    }

    @Test
    void checkIn_shouldThrowException_whenAlreadyCheckedIn() {
        reservationDay.setStatus(Status.CHECKED_IN);
        reservationDay.setCheckedInAt(LocalDateTime.now().minusHours(1));

        when(parkingSpotRepository.findByRowAndColumn("A", "01")).thenReturn(Optional.of(parkingSpot));
        when(reservationDayRepository.findFirstBySpot_IdAndDateAndStatusIn(
                eq(1),
                any(LocalDate.class),
                eq(List.of(Status.BOOKED, Status.CHECKED_IN))
        )).thenReturn(Optional.of(reservationDay));

        ResourceAlreadyReservedException exception = assertThrows(
                ResourceAlreadyReservedException.class,
                () -> parkingSpotService.checkIn("A", "01", "test@example.com")
        );

        assertEquals("Already checked in for date " + LocalDate.now(), exception.getMessage());
        verify(parkingSpotRepository, times(1)).findByRowAndColumn("A", "01");
        verify(reservationDayRepository, never()).save(any());
    }
}

