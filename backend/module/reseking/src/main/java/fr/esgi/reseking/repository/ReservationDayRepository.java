package fr.esgi.reseking.repository;

import fr.esgi.reseking.model.ReservationDay;
import fr.esgi.reseking.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationDayRepository
        extends JpaRepository<ReservationDay, Integer> {

    Optional<ReservationDay> findFirstBySpot_IdAndDateAndStatusIn(
            Integer spotId,
            LocalDate date,
            List<Status> statuses
    );

    List<ReservationDay> findBySpot_IdAndDateInAndStatusIn(
            Integer spotId,
            List<LocalDate> dates,
            List<Status> statuses
    );

    List<ReservationDay> findByDateAndStatusAndCheckedInAtIsNull(LocalDate date, Status status);
}
