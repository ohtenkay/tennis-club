package cz.inqool.tennis_club.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.model.User;
import jakarta.transaction.Transactional;

@Repository
public class UserRepository extends BaseRepository implements IdentifiableRepository<User, UUID> {

    /**
     * Check if user with the given phone number exists
     *
     * @param phoneNumber to search by
     * @return true if user with the given phone number exists
     */
    public boolean existsByPhoneNumber(String phoneNumber) {
        return findByPhoneNumber(phoneNumber).isPresent();
    }

    /**
     * Check if phone number has been used before
     *
     * @param phoneNumber to search by
     * @return true if phone number has been used before
     */
    public boolean phoneNumberHasBeenUsed(String phoneNumber) {
        return entityManager
                .createQuery("SELECT COUNT(u) FROM PhoneName u WHERE u.phoneNumber = :phoneNumber", Long.class)
                .setParameter("phoneNumber", phoneNumber)
                .getSingleResult() > 0;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.id = :id AND u.deletedAt IS NULL", User.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Optional<User> findByIdWithDeleted(UUID id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    /**
     * Find user by phone number
     *
     * @param phoneNumber to search by
     * @return user with the given phone number
     */
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return entityManager
                .createQuery(
                        "SELECT u FROM User u WHERE u.phoneName.phoneNumber = :phoneNumber AND u.deletedAt IS NULL",
                        User.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultStream()
                .findFirst();
    }

    /**
     * Update user, together with its associated phoneName entity
     *
     * @param user to update
     */
    @Transactional
    public void update(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        user.getPhoneName().setUpdatedAt(LocalDateTime.now());

        entityManager.merge(user);
    }

    /**
     * Delete user, together with its associated phoneName entity
     *
     * @param user to delete
     */
    @Transactional
    public void delete(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        user.setDeletedAt(LocalDateTime.now());
        user.getPhoneName().setUpdatedAt(LocalDateTime.now());
        user.getPhoneName().setDeletedAt(LocalDateTime.now());

        entityManager.merge(user);
    }

}
