package cz.inqool.tennis_club.repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.model.Court;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CourtRepository extends BaseRepository implements IdentifiableRepository<Court, UUID> {

    /**
     * Check if court with given id exists
     *
     * @param id id of court
     * @return true if court exists
     */
    public boolean existsById(UUID id) {
        return findById(id).isPresent();
    }

    /**
     * Find all courts
     *
     * @return list of all courts
     */
    public List<Court> findAll() {
        return entityManager.createQuery("SELECT c FROM Court c WHERE c.deletedAt IS NULL", Court.class)
                .getResultList();
    }

    @Override
    public Optional<Court> findById(UUID id) {
        return entityManager.createQuery("SELECT c FROM Court c WHERE c.id = :id AND c.deletedAt IS NULL", Court.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Court> findByIdWithDeleted(UUID id) {
        return Optional.ofNullable(entityManager.find(Court.class, id));
    }

}
