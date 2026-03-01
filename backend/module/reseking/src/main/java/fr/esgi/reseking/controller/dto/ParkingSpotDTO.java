package fr.esgi.reseking.controller.dto;

import fr.esgi.reseking.model.enums.SpotType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpotDTO {

    private Integer id;
    private String row;
    private String column;
    private SpotType type;
    private List<String> reservations;
    private String activeReservation;
}