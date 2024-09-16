package cz.inqool.tennis_club.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import cz.inqool.tennis_club.exception.UserNotFoundException;
import cz.inqool.tennis_club.model.PhoneName;
import cz.inqool.tennis_club.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.val;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

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

    @Transactional
    public User create(String name, String phoneNumber) {
        val phoneName = new PhoneName(phoneNumber, name);
        val user = new User(phoneName);

        entityManager.persist(user);
        return user;
    }

    @Transactional
    public void deleteById(UUID id) {
        val user = findById(id).orElseThrow(() -> new UserNotFoundException(id));

        user.setUpdatedAt(Instant.now());
        user.setDeletedAt(Instant.now());
        user.getPhoneName().setUpdatedAt(Instant.now());
        user.getPhoneName().setDeletedAt(Instant.now());

        entityManager.merge(user);
    }

}
