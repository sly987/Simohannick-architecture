package fr.esgi.reseking.repository;

import fr.esgi.reseking.model.ReservationDay;
import fr.esgi.reseking.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("select count(rd) from ReservationDay rd where rd.date between :from and :to and rd.status in :statuses")
    long countByDateBetweenAndStatusIn(@Param("from") LocalDate from, @Param("to") LocalDate to, @Param("statuses") List<Status> statuses);
}
