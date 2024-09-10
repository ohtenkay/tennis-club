package cz.inqool.tennis_club.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.exception.SurfaceTypeNotFoundException;
import cz.inqool.tennis_club.model.SurfaceType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import lombok.val;

@Repository
public class SurfaceTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SurfaceType create(String name, BigDecimal pricePerMinute) {
        val surfaceType = new SurfaceType(name, pricePerMinute);

        entityManager.persist(surfaceType);

        return surfaceType;
    }

    public Optional<SurfaceType> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(SurfaceType.class, id));
    }

    @Transactional
    public void deleteById(UUID id) {
        val surfaceType = findById(id).orElseThrow(() -> new SurfaceTypeNotFoundException(id));

        surfaceType.setUpdatedAt(Instant.now());
        surfaceType.setDeletedAt(Instant.now());

        entityManager.merge(surfaceType);
    }

}
