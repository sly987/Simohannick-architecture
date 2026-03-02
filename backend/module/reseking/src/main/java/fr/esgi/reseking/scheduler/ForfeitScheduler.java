package fr.esgi.reseking.scheduler;

import fr.esgi.reseking.model.ReservationDay;
import fr.esgi.reseking.model.enums.Status;
import fr.esgi.reseking.repository.ReservationDayRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForfeitScheduler {

    private final ReservationDayRepository reservationDayRepository;

    public ForfeitScheduler(ReservationDayRepository reservationDayRepository) {
        this.reservationDayRepository = reservationDayRepository;
    }

    @Scheduled(cron = "0 0 11 * * *", zone = "Europe/Paris")
    public void forfeitUnclaimedSpots() {
        LocalDate today = LocalDate.now();

        List<ReservationDay> toForfeit =
                reservationDayRepository.findByDateAndStatusAndCheckedInAtIsNull(today, Status.BOOKED);

        toForfeit.forEach(day -> day.setStatus(Status.FORFEITED));

        reservationDayRepository.saveAll(toForfeit);
        System.out.println("Forfeit scheduler running at " + LocalDateTime.now());
    }
}