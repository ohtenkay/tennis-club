package cz.inqool.tennis_club.exception;

import java.util.UUID;

public class CourtNotFoundException extends RuntimeException {
    public CourtNotFoundException(UUID courtId) {
        super("Court with ID " + courtId + " not found");
    }
}
