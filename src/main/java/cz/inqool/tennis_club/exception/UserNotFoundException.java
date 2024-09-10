package cz.inqool.tennis_club.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID surfaceTypeId) {
        super("User with ID " + surfaceTypeId + " not found");
    }
}
