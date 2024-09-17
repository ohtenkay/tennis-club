package cz.inqool.tennis_club.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.model.SurfaceType;

@Repository
public class SurfaceTypeRepository extends BaseRepository {

    public Optional<SurfaceType> findById(UUID id) {
        return entityManager
                .createQuery("SELECT s FROM SurfaceType s WHERE s.id = :id AND s.deletedAt IS NULL", SurfaceType.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
    }

    public Optional<SurfaceType> findByIdWithDeleted(UUID id) {
        return Optional.ofNullable(entityManager.find(SurfaceType.class, id));
    }

}
