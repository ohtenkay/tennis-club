package cz.inqool.tennis_club.repository;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Repository;

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

    @Transactional
    public UUID create(String name, String phoneNumber) {
        val phoneName = new PhoneName(phoneNumber, name);
        val user = new User(phoneName);

        entityManager.persist(user);
        return user.getId();
    }

    public User findById(UUID id) {
        return entityManager.find(User.class, id);
    }

    @Transactional
    public void deleteById(UUID id) {
        val user = findById(id);

        user.setUpdatedAt(Instant.now());
        user.setDeletedAt(Instant.now());
        user.getPhoneName().setUpdatedAt(Instant.now());
        user.getPhoneName().setDeletedAt(Instant.now());

        entityManager.merge(user);
    }

}
