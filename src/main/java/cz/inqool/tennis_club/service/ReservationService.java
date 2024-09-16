package cz.inqool.tennis_club.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import cz.inqool.tennis_club.model.Reservation;
import cz.inqool.tennis_club.model.send.ReservationSend;

@Service
public class ReservationService {

    public List<Reservation> getAllReservations() {
        return null;
    }

    public Reservation getReservationById(UUID id) {
        return null;
    }

    public Reservation createReservation(ReservationSend reservationSend) {
        return null;
    }

    public Reservation updateReservation(UUID id, ReservationSend reservationSend) {
        return null;
    }

    public void deleteReservation(UUID id) {
    }
}
