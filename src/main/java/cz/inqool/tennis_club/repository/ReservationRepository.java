package cz.inqool.tennis_club.repository;

import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.exception.CourtNotFoundException;
import cz.inqool.tennis_club.exception.ReservationNotFoundException;
import cz.inqool.tennis_club.exception.UserNotFoundException;
import cz.inqool.tennis_club.model.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.val;

@Repository
public class ReservationRepository {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourtRepository courtRepository;

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

    @Transactional
    public void save(Reservation reservation) {
        entityManager.persist(reservation);
    }

    @Transactional
    public void update(Reservation reservation) {
        reservation.setUpdatedAt(Instant.now());

        entityManager.merge(reservation);
    }

    @Transactional
    public void delete(Reservation reservation) {
        reservation.setUpdatedAt(Instant.now());
        reservation.setDeletedAt(Instant.now());

        entityManager.merge(reservation);
    }

    @Transactional
    public Reservation create(UUID courtId, UUID userId, Instant startTime, Instant endTime, String gameType) {
        val court = courtRepository.findById(courtId).orElseThrow(() -> new CourtNotFoundException(courtId));
        val user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        val reservation = new Reservation(court, user, startTime, endTime, gameType);

        entityManager.persist(reservation);
        return reservation;
    }

    @Transactional
    public void deleteById(UUID id) {
        val reservation = findById(id).orElseThrow(() -> new ReservationNotFoundException(id));

        reservation.setUpdatedAt(Instant.now());
        reservation.setDeletedAt(Instant.now());

        entityManager.merge(reservation);
    }

}
