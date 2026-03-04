package fr.esgi.reseking.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private Integer id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String registrationNumber;
    private Integer employeeId;
    private Integer spotId;
    private String status;
}
