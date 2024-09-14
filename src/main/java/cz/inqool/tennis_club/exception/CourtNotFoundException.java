package cz.inqool.tennis_club.exception;

import java.util.UUID;

public class CourtNotFoundException extends RuntimeException {

    public CourtNotFoundException(UUID courtId) {
        super("Could not find court with ID: " + courtId);
    }

}
