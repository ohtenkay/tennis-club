package cz.inqool.tennis_club.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import cz.inqool.tennis_club.exception.PhoneNumberUsedBeforeException;
import cz.inqool.tennis_club.exception.UserNotFoundException;
import cz.inqool.tennis_club.model.PhoneName;
import cz.inqool.tennis_club.model.User;
import cz.inqool.tennis_club.model.create.UserCreate;
import cz.inqool.tennis_club.model.update.UserUpdate;
import cz.inqool.tennis_club.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new UserNotFoundException(phoneNumber));
    }

    @Transactional
    public User createUser(UserCreate userCreate) {
        if (userRepository.phoneNumberHasBeenUsed(userCreate.phoneNumber())) {
            throw new PhoneNumberUsedBeforeException(userCreate.phoneNumber());
        }

        val user = new User(userCreate);

        userRepository.save(user);
        return user;
    }

    @Transactional
    public User getOrCreateUser(UserCreate userCreate) {
        return userRepository.findByPhoneNumber(userCreate.phoneNumber()).orElseGet(() -> createUser(userCreate));
    }

    @Transactional
    public User updateUser(UserUpdate userUpdate) {
        val user = getUserById(userUpdate.id());

        // If the phone number hasn't changed, just update the name
        if (user.getPhoneName().getPhoneNumber().equals(userUpdate.phoneNumber())) {
            user.getPhoneName().setName(userUpdate.name());
            userRepository.update(user);
            return user;
        }

        if (userRepository.phoneNumberHasBeenUsed(userUpdate.phoneNumber())) {
            throw new PhoneNumberUsedBeforeException(userUpdate.phoneNumber());
        }

        // Phone number has changed and is unique, update the phoneNumber and name
        val newPhoneName = new PhoneName(userUpdate.phoneNumber(), userUpdate.name());
        user.setPhoneName(newPhoneName);

        userRepository.update(user);
        return user;
    }

    @Transactional
    public void deleteUser(UUID id) {
        val user = getUserById(id);

        userRepository.delete(user);
    }

}
