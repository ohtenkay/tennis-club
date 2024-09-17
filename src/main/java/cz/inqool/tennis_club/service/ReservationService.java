package cz.inqool.tennis_club.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import cz.inqool.tennis_club.exception.CourtNotFoundException;
import cz.inqool.tennis_club.exception.InvalidOrderException;
import cz.inqool.tennis_club.exception.ReservationNotFoundException;
import cz.inqool.tennis_club.exception.UserNotFoundException;
import cz.inqool.tennis_club.model.Reservation;
import cz.inqool.tennis_club.model.create.ReservationCreate;
import cz.inqool.tennis_club.model.create.UserCreate;
import cz.inqool.tennis_club.model.update.ReservationUpdate;
import cz.inqool.tennis_club.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final CourtService courtService;
    private final UserService userService;
    private final ReservationRepository reservationRepository;

    public List<Reservation> getReservations(String phoneNumber, boolean future, UUID courtId,
            String order) {
        if (phoneNumber != null && !userService.userExists(phoneNumber)) {
            throw new UserNotFoundException(phoneNumber);
        }

        if (courtId != null && !courtService.courtExists(courtId)) {
            throw new CourtNotFoundException(courtId);
        }

        if (!List.of("asc", "desc").contains(order.toLowerCase())) {
            throw new InvalidOrderException(order);
        }

        return reservationRepository.find(phoneNumber, future, courtId, order);
    }

    public Reservation getReservationById(UUID id) {
        return reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException(id));
    }

    @Transactional
    public Reservation createReservation(ReservationCreate reservationCreate) {
        val court = courtService.getCourtById(reservationCreate.courtId());
        val user = userService.getOrCreateUser(new UserCreate(reservationCreate));

        // TODO: Check if the court is available
        // TODO: validate the reservation time and change it to fifteen minutes
        // intervals
        val reservation = new Reservation(court, user, reservationCreate);

        reservationRepository.save(reservation);
        return reservation;
    }

    @Transactional
    public Reservation updateReservation(ReservationUpdate reservationUpdate) {
        val court = courtService.getCourtById(reservationUpdate.courtId());
        val user = userService.getUserById(reservationUpdate.userId());
        val reservation = getReservationById(reservationUpdate.id());
        reservation.setCourt(court);
        reservation.setUser(user);
        // TODO: Check if the court is available
        // TODO: validate the reservation time and change it to fifteen minutes
        reservation.setStartTime(reservationUpdate.startTime());
        reservation.setEndTime(reservationUpdate.endTime());
        reservation.setGameType(reservationUpdate.gameType());

        reservationRepository.update(reservation);
        return reservation;
    }

    @Transactional
    public void deleteReservation(UUID id) {
        val reservation = getReservationById(id);

        reservationRepository.delete(reservation);
    }
}
