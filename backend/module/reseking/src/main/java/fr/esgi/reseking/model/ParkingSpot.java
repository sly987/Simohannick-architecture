package fr.esgi.reseking.model;

import fr.esgi.reseking.model.enums.SpotType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "parking_spot")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spot_id")
    private Integer id;

    @Column(name = "spot_row")
    private String row;

    @Column(name = "spot_column")
    private String column;

    @Enumerated(EnumType.STRING)
    private SpotType type;

    @OneToMany(mappedBy = "spot")
    private List<Reservation> reservations;

    public String getLabel() {
        return row + String.format("%02d", Integer.parseInt(column));
    }

}
