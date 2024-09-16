package cz.inqool.tennis_club.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cz.inqool.tennis_club.exception.UserNotFoundException;
import lombok.val;

@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreate() {
        val name = "Geralt";
        val phoneNumber = "+420 123 456 789";

        val user = userRepository.create(name, phoneNumber);

        assertNotNull(user);
        assertEquals(name, user.getPhoneName().getName());
        assertEquals(phoneNumber, user.getPhoneName().getPhoneNumber());
    }

    @Test
    void testDelete() {
        val savedUser = userRepository.create("Ciri", "+420 222 456 789");
        userRepository.deleteById(savedUser.getId());
        val deletedUser = userRepository.findByIdWithDeleted(savedUser.getId());

        assertTrue(deletedUser.isPresent());
        assertNotNull(deletedUser.get().getDeletedAt());
        assertNotNull(deletedUser.get().getPhoneName().getDeletedAt());
        assertThrows(UserNotFoundException.class, () -> userRepository.deleteById(UUID.randomUUID()));
    }

}
