package fr.esgi.reseking.scheduler;

import fr.esgi.reseking.model.Reservation;
import fr.esgi.reseking.model.enums.Status;
import fr.esgi.reseking.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompletionScheduler {

    private final ReservationRepository reservationRepository;

    public CompletionScheduler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Paris")
    public void completeExpiredReservations() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<Reservation> expiredReservations = reservationRepository.findByEndDateAndStatusIn(
                yesterday,
                List.of(Status.BOOKED, Status.CHECKED_IN, Status.FORFEITED)
        );

        expiredReservations.forEach(reservation -> reservation.setStatus(Status.COMPLETED));

        reservationRepository.saveAll(expiredReservations);
    }
}

