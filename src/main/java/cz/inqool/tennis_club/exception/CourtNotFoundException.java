package cz.inqool.tennis_club.exception;

import java.util.UUID;

public class CourtNotFoundException extends RuntimeException {
    public CourtNotFoundException(UUID surfaceTypeId) {
        super("Court with ID " + surfaceTypeId + " not found");
    }
}
