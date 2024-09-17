package cz.inqool.tennis_club.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.model.Reservation;

@Repository
public class ReservationRepository extends BaseRepository {

    public List<Reservation> findAll() {
        return entityManager.createQuery("SELECT r FROM Reservation r WHERE r.deletedAt IS NULL", Reservation.class)
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

}
