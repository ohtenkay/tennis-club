package cz.inqool.tennis_club.service;

import static cz.inqool.tennis_club.util.DateTimeUtils.getClosestIntervalMark;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import cz.inqool.tennis_club.exception.CourtAlreadyReservedException;
import cz.inqool.tennis_club.exception.CourtNotFoundException;
import cz.inqool.tennis_club.exception.InvalidOrderException;
import cz.inqool.tennis_club.exception.InvalidReservationTimeException;
import cz.inqool.tennis_club.exception.ReservationNotFoundException;
import cz.inqool.tennis_club.exception.UserNotFoundException;
import cz.inqool.tennis_club.model.Reservation;
import cz.inqool.tennis_club.model.create.ReservationCreate;
import cz.inqool.tennis_club.model.create.UserCreate;
import cz.inqool.tennis_club.model.response.ReservationResponse;
import cz.inqool.tennis_club.model.update.ReservationUpdate;
import cz.inqool.tennis_club.repository.ReservationRepository;
import cz.inqool.tennis_club.util.DateTimeUtils.IntervalBoundary;
import cz.inqool.tennis_club.util.Pair;
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
    public ReservationResponse createReservation(ReservationCreate reservationCreate) {
        val court = courtService.getCourtById(reservationCreate.courtId());
        val user = userService.getOrCreateUser(new UserCreate(reservationCreate));

        val reservationTime = processReservationTime(reservationCreate.startTime(), reservationCreate.endTime());
        val startTime = reservationTime.first();
        val endTime = reservationTime.second();

        checkReservationTime(null, court.getId(), startTime, endTime);

        val reservation = new Reservation(court, user, startTime, endTime, reservationCreate.gameType());

        reservationRepository.save(reservation);
        return new ReservationResponse(reservation);
    }

    @Transactional
    public ReservationResponse updateReservation(ReservationUpdate reservationUpdate) {
        val court = courtService.getCourtById(reservationUpdate.courtId());
        val user = userService.getUserById(reservationUpdate.userId());
        val reservation = getReservationById(reservationUpdate.id());

        val reservationTime = processReservationTime(reservationUpdate.startTime(), reservationUpdate.endTime());
        val startTime = reservationTime.first();
        val endTime = reservationTime.second();

        checkReservationTime(reservation.getId(), court.getId(), startTime, endTime);

        reservation.setCourt(court);
        reservation.setUser(user);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setGameType(reservationUpdate.gameType());

        reservationRepository.update(reservation);
        return new ReservationResponse(reservation);
    }

    @Transactional
    public void deleteReservation(UUID id) {
        val reservation = getReservationById(id);

        reservationRepository.delete(reservation);
    }

    private void checkReservationTime(UUID reservationId, UUID courtId, LocalDateTime startTime,
            LocalDateTime endTime) {
        if (reservationRepository.existsForCourtInInterval(reservationId, courtId, startTime, endTime)) {
            throw new CourtAlreadyReservedException(courtId, startTime, endTime);
        }
    }

    private Pair<LocalDateTime, LocalDateTime> processReservationTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new InvalidReservationTimeException("The start time must be before the end time");
        }

        if (Duration.between(startTime, endTime).toHours() > 8) {
            throw new InvalidReservationTimeException("The reservation time cannot be more than 8 hours long");
        }

        val newStartTime = getClosestIntervalMark(startTime, 15, IntervalBoundary.UPPER);
        val newEndTime = getClosestIntervalMark(endTime, 15, IntervalBoundary.LOWER);

        return new Pair<>(newStartTime, newEndTime);
    }

}
