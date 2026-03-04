package fr.esgi.reseking.service;

import fr.esgi.reseking.controller.dto.ManagerDashboardDTO;
import fr.esgi.reseking.model.enums.SpotType;
import fr.esgi.reseking.model.enums.Status;
import fr.esgi.reseking.repository.ParkingSpotRepository;
import fr.esgi.reseking.repository.ReservationDayRepository;
import fr.esgi.reseking.repository.ReservationRepository;
import fr.esgi.reseking.util.DashboardUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class DashboardService {

    private final ReservationRepository reservationRepository;
    private final ReservationDayRepository reservationDayRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    public DashboardService(ReservationRepository reservationRepository,
                            ReservationDayRepository reservationDayRepository,
                            ParkingSpotRepository parkingSpotRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationDayRepository = reservationDayRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    public ManagerDashboardDTO getManagerDashboard(LocalDate from, LocalDate to) {
        long uniqueUsers = reservationRepository.countDistinctEmployeesByDateRange(from, to);

        long totalSpots = parkingSpotRepository.count();
        long occupiedDays = reservationDayRepository.countByDateBetweenAndStatusIn(from, to, List.of(Status.BOOKED, Status.CHECKED_IN));
        double averageOccupancyRate = DashboardUtil.calculateAverageOccupancyRate(from, to, totalSpots, occupiedDays);

        long totalReservationDays = reservationDayRepository.countByDateBetweenAndStatusIn(from, to, Arrays.asList(Status.values()));
        long forfeitedDays = reservationDayRepository.countByDateBetweenAndStatusIn(from, to, List.of(Status.FORFEITED));
        double noShowRate = DashboardUtil.calculateRate(totalReservationDays, forfeitedDays);

        long electricSpots = parkingSpotRepository.countByType(SpotType.ELECTRIC);
        double electricSpotRatio = DashboardUtil.calculateRate(totalSpots, electricSpots);

        return new ManagerDashboardDTO(uniqueUsers, averageOccupancyRate, noShowRate, electricSpotRatio);
    }
}


