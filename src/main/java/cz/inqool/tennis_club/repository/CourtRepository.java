package cz.inqool.tennis_club.repository;

import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.exception.CourtNotFoundException;
import cz.inqool.tennis_club.model.Court;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;

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
        return Optional.ofNullable(entityManager.find(Court.class, id));
    }

    @Transactional
    public void save(Court court) {
        entityManager.persist(court);
    }

    @Transactional
    public void deleteById(UUID id) {
        val court = findById(id).orElseThrow(() -> new CourtNotFoundException(id));

        court.setUpdatedAt(Instant.now());
        court.setDeletedAt(Instant.now());

        entityManager.merge(court);
    }

}
