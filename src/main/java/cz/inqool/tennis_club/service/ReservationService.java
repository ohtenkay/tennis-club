package cz.inqool.tennis_club.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import cz.inqool.tennis_club.exception.ReservationNotFoundException;
import cz.inqool.tennis_club.exception.UserNotFoundException;
import cz.inqool.tennis_club.model.Reservation;
import cz.inqool.tennis_club.model.send.ReservationSend;
import cz.inqool.tennis_club.repository.ReservationRepository;
import cz.inqool.tennis_club.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final CourtService courtService;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(UUID id) {
        return reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException(id));
    }

    @Transactional
    public Reservation createReservation(ReservationSend reservationSend) {
        val court = courtService.getCourtById(reservationSend.courtId());
        // TODO: Or create
        val user = userRepository.findByPhoneNumber(reservationSend.phoneNumber())
                .orElseThrow(() -> new UserNotFoundException(reservationSend.phoneNumber()));
        // TODO: Check if the court is available
        // TODO: validate the reservation time and change it to fifteen minutes
        // intervals
        val reservation = new Reservation(court, user, reservationSend.startTime(), reservationSend.endTime(),
                reservationSend.gameType());

        reservationRepository.save(reservation);
        return reservation;
    }

    @Transactional
    public Reservation updateReservation(UUID id, ReservationSend reservationSend) {
        val court = courtService.getCourtById(reservationSend.courtId());
        val user = userRepository.findByPhoneNumber(reservationSend.phoneNumber())
                .orElseThrow(() -> new UserNotFoundException(reservationSend.phoneNumber()));
        val reservation = getReservationById(id);
        // TODO: The same as in createReservation
        reservation.setCourt(court);
        reservation.setUser(user);
        reservation.setStartTime(reservationSend.startTime());
        reservation.setEndTime(reservationSend.endTime());
        reservation.setGameType(reservationSend.gameType());

        reservationRepository.update(reservation);
        return reservation;
    }

    @Transactional
    public void deleteReservation(UUID id) {
        val reservation = getReservationById(id);

        reservationRepository.delete(reservation);
    }
}
