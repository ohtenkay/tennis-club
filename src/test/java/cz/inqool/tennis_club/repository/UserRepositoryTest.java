package cz.inqool.tennis_club.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.val;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreate() {
        val name = "Geralt";
        val phoneNumber = "+420 123 456 789";

        val id = userRepository.create(name, phoneNumber);
        val savedUser = userRepository.findById(id);

        assertNotNull(savedUser);
        assertEquals(name, savedUser.getPhoneName().getName());
        assertEquals(phoneNumber, savedUser.getPhoneName().getPhoneNumber());
    }

    @Test
    void testDeleteById() {
        val id = userRepository.create("Ciri", "+420 222 456 789");
        userRepository.deleteById(id);
        val deletedUser = userRepository.findById(id);

        assertNotNull(deletedUser);
        assertNotNull(deletedUser.getDeletedAt());
        assertNotNull(deletedUser.getPhoneName().getDeletedAt());
    }

}
