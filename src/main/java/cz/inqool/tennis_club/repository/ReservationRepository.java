package cz.inqool.tennis_club.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.model.Reservation;

@Repository
public class ReservationRepository extends BaseRepository {

    public List<Reservation> find(String phoneNumber, boolean future, UUID courtId, String order) {
        return entityManager
                .createQuery(
                        "SELECT r FROM Reservation r WHERE (:phoneNumber IS NULL OR r.user.phoneName.phoneNumber = :phoneNumber) "
                                + "AND (NOT :future OR r.startTime > CURRENT_TIMESTAMP) "
                                + "AND (:courtId IS NULL OR r.court.id = :courtId) "
                                + "AND r.deletedAt IS NULL "
                                + "ORDER BY r.startTime " + order,
                        Reservation.class)
                .setParameter("phoneNumber", phoneNumber)
                .setParameter("future", future)
                .setParameter("courtId", courtId)
                .setParameter("order", order)
                .getResultList();
    }

    public Optional<Reservation> findById(UUID id) {
        return entityManager
                .createQuery("SELECT r FROM Reservation r WHERE r.id = :id AND r.deletedAt IS NULL", Reservation.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
    }

    public Optional<Reservation> findByIdWithDeleted(UUID id) {
        return Optional.ofNullable(entityManager.find(Reservation.class, id));
    }

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
