package cz.inqool.tennis_club.repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.model.Court;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CourtRepository extends BaseRepository {

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

}
