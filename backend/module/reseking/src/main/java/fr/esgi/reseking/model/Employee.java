package fr.esgi.reseking.model;

import fr.esgi.reseking.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "employee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String occupation;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "employee")
    private List<Reservation> reservations;

    @OneToOne
    @JoinColumn(name = "active_reservation_id")
    private Reservation activeReservation;
}
