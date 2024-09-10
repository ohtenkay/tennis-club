package cz.inqool.tennis_club.repository;

import java.time.Instant;
import java.util.UUID;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.exception.CourtNotFoundException;
import cz.inqool.tennis_club.exception.SurfaceTypeNotFoundException;
import cz.inqool.tennis_club.model.Court;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.val;

@Repository
public class CourtRepository {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private SurfaceTypeRepository surfaceTypeRepository;

    @Transactional
    public Court create(UUID surfaceTypeId, String name, String description) {
        val surfaceType = surfaceTypeRepository.findById(surfaceTypeId)
                .orElseThrow(() -> new SurfaceTypeNotFoundException(surfaceTypeId));
        val court = new Court(surfaceType, name);
        court.setDescription(description);

        entityManager.persist(court);
        return court;
    }

    public Optional<Court> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(Court.class, id));
    }

    @Transactional
    public void deleteById(UUID id) {
        val court = findById(id).orElseThrow(() -> new CourtNotFoundException(id));

        court.setUpdatedAt(Instant.now());
        court.setDeletedAt(Instant.now());

        entityManager.merge(court);
    }

}
