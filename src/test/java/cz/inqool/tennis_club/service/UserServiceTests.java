package cz.inqool.tennis_club.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cz.inqool.tennis_club.exception.PhoneNumberUsedBeforeException;
import cz.inqool.tennis_club.exception.UserNotFoundException;
import cz.inqool.tennis_club.model.create.UserCreate;
import cz.inqool.tennis_club.model.update.UserUpdate;
import cz.inqool.tennis_club.repository.UserRepository;
import lombok.val;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreate() {
        val name = "Geralt";
        val phoneNumber = "+420 123 456 789";

        val user = userService.createUser(new UserCreate(phoneNumber, name));

        assertNotNull(user);
        assertEquals(name, user.getPhoneName().getName());
        assertEquals(phoneNumber.replaceAll("\\s+", ""),
                user.getPhoneName().getPhoneNumber());

        assertThrows(PhoneNumberUsedBeforeException.class,
                () -> userService.createUser(new UserCreate(phoneNumber, "Yennefer")));
    }

    @Test
    void testDelete() {
        val savedUser = userService.createUser(new UserCreate("420222456789", "Ciri"));
        userService.deleteUser(savedUser.getId());

        val deletedUser = userRepository.findByIdWithDeleted(savedUser.getId());

        assertTrue(deletedUser.isPresent());
        assertNotNull(deletedUser.get().getDeletedAt());
        assertNotNull(deletedUser.get().getPhoneName().getDeletedAt());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(UUID.randomUUID()));
    }

    @Test
    void testUpdate() {
        val savedUser = userService.createUser(new UserCreate("420222456788", "Ciri"));
        val updatedName = "Cirilla";

        val updatedUser = userService
                .updateUser(new UserUpdate(savedUser.getId(), savedUser.getPhoneName().getPhoneNumber(), updatedName));

        assertEquals(updatedName, updatedUser.getPhoneName().getName());
        assertEquals(savedUser.getId(), updatedUser.getId());

        val anotherUser = userService.createUser(new UserCreate("420222456787", "Triss"));

        assertThrows(PhoneNumberUsedBeforeException.class,
                () -> userService.updateUser(
                        new UserUpdate(savedUser.getId(), anotherUser.getPhoneName().getPhoneNumber(), updatedName)));
    }

}
