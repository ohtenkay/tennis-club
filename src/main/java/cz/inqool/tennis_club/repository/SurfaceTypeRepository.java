package cz.inqool.tennis_club.repository;

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

}
