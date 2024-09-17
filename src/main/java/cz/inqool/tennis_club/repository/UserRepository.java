package cz.inqool.tennis_club.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.model.User;
import jakarta.transaction.Transactional;

@Repository
public class UserRepository extends BaseRepository {

    public boolean existsByPhoneNumber(String phoneNumber) {
        return findByPhoneNumber(phoneNumber).isPresent();
    }

    public boolean phoneNumberHasBeenUsed(String phoneNumber) {
        return entityManager
                .createQuery("SELECT COUNT(u) FROM PhoneName u WHERE u.phoneNumber = :phoneNumber", Long.class)
                .setParameter("phoneNumber", phoneNumber)
                .getSingleResult() > 0;
    }

    public Optional<User> findById(UUID id) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.id = :id AND u.deletedAt IS NULL", User.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
    }

    public Optional<User> findByIdWithDeleted(UUID id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return entityManager
                .createQuery(
                        "SELECT u FROM User u WHERE u.phoneName.phoneNumber = :phoneNumber AND u.deletedAt IS NULL",
                        User.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultStream()
                .findFirst();
    }

    @Transactional
    public void update(User user) {
        user.setUpdatedAt(Instant.now());
        user.getPhoneName().setUpdatedAt(Instant.now());

        entityManager.merge(user);
    }

    @Transactional
    public void delete(User user) {
        user.setUpdatedAt(Instant.now());
        user.setDeletedAt(Instant.now());
        user.getPhoneName().setUpdatedAt(Instant.now());
        user.getPhoneName().setDeletedAt(Instant.now());

        entityManager.merge(user);
    }

}
