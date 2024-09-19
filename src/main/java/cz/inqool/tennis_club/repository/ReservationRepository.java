package cz.inqool.tennis_club.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.model.Reservation;

@Repository
public class ReservationRepository extends BaseRepository implements IdentifiableRepository<Reservation, UUID> {

    /**
     * Find reservations wiht speficified filters
     *
     * @param phoneNumber phone number of user
     * @param future      if true, only future reservations are returned
     * @param courtId     id of court
     * @param order       ordering, e.g. "ASC" or "DESC"
     * @return list of reservations
     */
    public List<Reservation> find(String phoneNumber, boolean future, UUID courtId, String order) {
        return entityManager
                .createQuery(
                        "SELECT r FROM Reservation r WHERE (:phoneNumber IS NULL OR r.user.phoneName.phoneNumber = :phoneNumber) "
                                + "AND (:future IS FALSE OR r.startTime > CURRENT_TIMESTAMP) "
                                + "AND (:courtId IS NULL OR r.court.id = :courtId) "
                                + "AND r.deletedAt IS NULL "
                                + "ORDER BY r.startTime " + order,
                        Reservation.class)
                .setParameter("phoneNumber", phoneNumber)
                .setParameter("future", future)
                .setParameter("courtId", courtId)
                .getResultList();
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return entityManager
                .createQuery("SELECT r FROM Reservation r WHERE r.id = :id AND r.deletedAt IS NULL", Reservation.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Reservation> findByIdWithDeleted(UUID id) {
        return Optional.ofNullable(entityManager.find(Reservation.class, id));
    }

    /**
     * Checks if a court is already reserved in a given interval
     *
     * @param reservationId id of reservation to exclude, use for update
     * @param courtId       id of court
     * @param startTime     start time of reservation
     * @param endTime       end time of reservation
     * @return true if court is already reserved
     */
    public boolean existsForCourtInInterval(UUID reservationId, UUID courtId, LocalDateTime startTime,
            LocalDateTime endTime) {
        return entityManager
                .createQuery(
                        "SELECT COUNT(r) > 0 FROM Reservation r WHERE r.court.id = :courtId "
                                + "AND (:reservationId IS NULL OR r.id != :reservationId) "
                                + "AND r.deletedAt IS NULL "
                                + "AND (:startTime < r.endTime AND r.startTime < :endTime)",
                        Boolean.class)
                .setParameter("reservationId", reservationId)
                .setParameter("courtId", courtId)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .getSingleResult();
    }

}
