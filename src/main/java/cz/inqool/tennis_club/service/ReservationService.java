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

    /**
     * Get reservations based on the provided parameters
     *
     * @param phoneNumber phone number of the user
     * @param future      if true, only future reservations are returned
     * @param courtId     id of the court
     * @param order       ordering, e.g. "ASC" or "DESC"
     * @return list of reservations
     * @throws UserNotFoundException  if the user is specified and does not exist
     * @throws CourtNotFoundException if the court is specified and does not exist
     * @throws InvalidOrderException  if the order is not "ASC" or "DESC",
     *                                case-insensitive
     */
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

    /**
     * Get reservation by id
     *
     * @param id id of the reservation
     * @return reservation
     * @throws ReservationNotFoundException if the reservation does not exist
     */
    public Reservation getReservationById(UUID id) {
        return reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException(id));
    }

    /**
     * Create a new reservation. The reservation time is truncated to the nearest 15
     * minute marks e.g. 10:07 -> 10:00 and 10:23 -> 10:30 for startTime and endTime
     * respecitvely. If a user with the provided phone number does not exist, it is
     * created.
     *
     * @param reservationCreate reservation data
     * @return created reservation
     * @throws CourtNotFoundException        if the court does not exist
     * @throws CourtAlreadyReservedException if the court is already reserved in the
     *                                       given interval
     */
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

    /**
     * Update an existing reservation. The reservation time is truncated to the
     * nearest 15 minute marks e.g. 10:07 -> 10:00 and 10:23 -> 10:30 for startTime
     * and endTime respectively.
     *
     * @param reservationUpdate reservation data
     * @return updated reservation
     * @throws CourtNotFoundException        if the court does not exist
     * @throws ReservationNotFoundException  if the reservation does not exist
     * @throws UserNotFoundException         if the user does not exist
     * @throws CourtAlreadyReservedException if the court is already reserved in the
     *                                       given interval
     */
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

    /**
     * Delete a reservation
     *
     * @param id id of the reservation
     * @throws ReservationNotFoundException if the reservation does not exist
     */
    @Transactional
    public void deleteReservation(UUID id) {
        val reservation = getReservationById(id);

        reservationRepository.delete(reservation);
    }

    /**
     * Check if a reservation exists
     *
     * @param id id of the reservation
     * @throws CourtAlreadyReservedException if the court is already reserved in the
     *                                       given interval
     */
    private void checkReservationTime(UUID reservationId, UUID courtId, LocalDateTime startTime,
            LocalDateTime endTime) {
        if (reservationRepository.existsForCourtInInterval(reservationId, courtId, startTime, endTime)) {
            throw new CourtAlreadyReservedException(courtId, startTime, endTime);
        }
    }

    /**
     * Process reservation time. The start time must be before the end time and the
     * reservation time cannot be more than 8 hours long. The start and end times
     * are truncated to the nearest 15 minute marks e.g. 10:07 -> 10:00 and 10:23
     * -> 10:30 for startTime and endTime respectively.
     *
     * @param startTime start time of reservation
     * @param endTime   end time of reservation
     * @return pair of processed start and end times
     * @throws InvalidReservationTimeException if the start time is after the end
     *                                         time or the reservation time is more
     *                                         than 8 hours long
     *
     */
    private Pair<LocalDateTime, LocalDateTime> processReservationTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new InvalidReservationTimeException("The start time must be before the end time");
        }

        if (Duration.between(startTime, endTime).toHours() > 8) {
            throw new InvalidReservationTimeException("The reservation time cannot be more than 8 hours long");
        }

        val newStartTime = getClosestIntervalMark(startTime, 15, IntervalBoundary.LOWER);
        val newEndTime = getClosestIntervalMark(endTime, 15, IntervalBoundary.UPPER);

        return new Pair<>(newStartTime, newEndTime);
    }

}
