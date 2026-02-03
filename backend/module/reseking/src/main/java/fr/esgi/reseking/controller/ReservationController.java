package fr.esgi.reseking.controller;

import fr.esgi.reseking.controller.dto.ReservationDTO;
import fr.esgi.reseking.model.ReservationEntity;
import fr.esgi.reseking.repository.ReservationRepository;
import fr.esgi.reseking.util.ReservationMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @PostMapping("/reservation")
    public void add(@RequestBody ReservationDTO reservation) {
        final ReservationEntity reservationEntity = ReservationMapper.map(reservation);
        reservationRepository.save(reservationEntity);
    }
}
