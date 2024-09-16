package cz.inqool.tennis_club.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID userId) {
        super("Could not find user with ID: " + userId);
    }

    public UserNotFoundException(String phoneNumber) {
        super("Could not find user with phone number: " + phoneNumber);
    }

}
