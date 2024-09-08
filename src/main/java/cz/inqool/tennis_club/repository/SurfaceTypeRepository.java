package cz.inqool.tennis_club.repository;

import java.time.Instant;
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

    @Transactional
    public void save(SurfaceType surfaceType) {
        entityManager.persist(surfaceType);
    }

    public SurfaceType findById(UUID id) {
        return entityManager.find(SurfaceType.class, id);
    }

    @Transactional
    public void deleteById(UUID id) {
        SurfaceType surfaceType = entityManager.find(SurfaceType.class, id);
        surfaceType.setDeletedAt(Instant.now());
        surfaceType.setUpdatedAt(Instant.now());
        entityManager.persist(surfaceType);
    }

}
