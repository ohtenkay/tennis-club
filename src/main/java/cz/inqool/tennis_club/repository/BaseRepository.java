package cz.inqool.tennis_club.repository;

import cz.inqool.tennis_club.model.AuditableEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

public abstract class BaseRepository {

    @PersistenceContext
    protected EntityManager entityManager;

    @Transactional
    public void save(AuditableEntity entity) {
        entityManager.persist(entity);
    }

    @Transactional
    public void update(AuditableEntity entity) {
        entity.setUpdatedAt(entity.getUpdatedAt());

        entityManager.merge(entity);
    }

    @Transactional
    public void delete(AuditableEntity entity) {
        entity.setUpdatedAt(entity.getUpdatedAt());
        entity.setDeletedAt(entity.getUpdatedAt());

        entityManager.merge(entity);
    }

}
