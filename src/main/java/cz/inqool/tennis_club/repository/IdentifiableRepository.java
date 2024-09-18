package cz.inqool.tennis_club.repository;

import java.util.Optional;

public interface IdentifiableRepository<T, ID> {

    /**
     * Find entity by id
     *
     * @param id id of entity
     * @return entity with given id
     */
    Optional<T> findById(ID id);

    /**
     * Find entity by id including deleted entities
     *
     * @param id id of entity
     * @return entity with given id
     */
    Optional<T> findByIdWithDeleted(ID id);

}
