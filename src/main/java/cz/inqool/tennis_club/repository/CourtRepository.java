package cz.inqool.tennis_club.repository;

import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.model.Court;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CourtRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Court> findAll() {
        return entityManager.createQuery("SELECT c FROM Court c WHERE c.deletedAt IS NULL", Court.class)
                .getResultList();
    }

    public Optional<Court> findById(UUID id) {
        return entityManager.createQuery("SELECT c FROM Court c WHERE c.id = :id AND c.deletedAt IS NULL", Court.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
    }

    public Optional<Court> findByIdWithDeleted(UUID id) {
        return Optional.ofNullable(entityManager.find(Court.class, id));
    }

    @Transactional
    public void save(Court court) {
        entityManager.persist(court);
    }

    @Transactional
    public void update(Court court) {
        court.setUpdatedAt(Instant.now());

        entityManager.merge(court);
    }

    @Transactional
    public void delete(Court court) {
        court.setUpdatedAt(Instant.now());
        court.setDeletedAt(Instant.now());

        entityManager.merge(court);
    }

}
