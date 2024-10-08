package cz.inqool.tennis_club.repository;

import java.time.LocalDateTime;

import cz.inqool.tennis_club.model.AuditableEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

public abstract class BaseRepository {

    @PersistenceContext
    protected EntityManager entityManager;

    /**
     * Saves entity to the database
     *
     * @param entity to be saved
     */
    @Transactional
    public void save(AuditableEntity entity) {
        entityManager.persist(entity);
    }

    /**
     * Updates entity in the database
     *
     * @param entity to be updated
     */
    @Transactional
    public void update(AuditableEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());

        entityManager.merge(entity);
    }

    /**
     * Deletes entity from the database, soft delete
     *
     * @param entity to be deleted
     */
    @Transactional
    public void delete(AuditableEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setDeletedAt(LocalDateTime.now());

        entityManager.merge(entity);
    }

}
