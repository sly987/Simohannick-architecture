package fr.esgi.reseking.model;

import fr.esgi.reseking.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private String registrationNumber;

    @ElementCollection
    @CollectionTable(name = "check_ins", joinColumns = @JoinColumn(name = "reservation_id"))
    @MapKeyColumn(name = "check_in_date")
    @Column(name = "checked_in_at")
    private Map<LocalDate, LocalDateTime> checkIns = new HashMap<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "spot_id")
    private ParkingSpot spot;

    public void addCheckIn(LocalDate date, LocalDateTime checkInTime) {
        this.checkIns.put(date, checkInTime);
    }

    public boolean isCheckedInForDate(LocalDate date) {
        return this.checkIns.containsKey(date);
    }

    public LocalDateTime getCheckInForDate(LocalDate date) {
        return this.checkIns.get(date);
    }
}
