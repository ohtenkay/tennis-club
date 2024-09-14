package cz.inqool.tennis_club.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.model.SurfaceType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class SurfaceTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<SurfaceType> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(SurfaceType.class, id));
    }

    @Transactional
    public void save(SurfaceType surfaceType) {
        entityManager.persist(surfaceType);
    }

    @Transactional
    public void update(SurfaceType surfaceType) {
        surfaceType.setUpdatedAt(Instant.now());

        entityManager.merge(surfaceType);
    }

    @Transactional
    public void delete(SurfaceType surfaceType) {
        surfaceType.setUpdatedAt(Instant.now());
        surfaceType.setDeletedAt(Instant.now());

        entityManager.merge(surfaceType);
    }

}
