package fr.esgi.reseking.repository;

import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.model.ParkingSpot;
import fr.esgi.reseking.model.Reservation;
import fr.esgi.reseking.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    boolean existsByEmployeeAndStatusIn(Employee employee, Status... statuses);

    @Query("select r from Reservation r where r.spot.id = :spotId and r.startDate <= :today and r.endDate >= :today and r.status in :statuses")
    Optional<Reservation> findActiveForSpotOnDate(@Param("spotId") Integer spotId, @Param("today") LocalDate today, @Param("statuses") List<Status> statuses);

    @Query("select count(distinct r.employee.id) from Reservation r where r.startDate <= :to and r.endDate >= :from")
    long countDistinctEmployeesByDateRange(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
