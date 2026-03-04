package fr.esgi.reseking.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDashboardDTO {
    private long uniqueUsers;
    private double averageOccupancyRate;
    private double noShowRate;
    private double electricSpotRatio;
}
