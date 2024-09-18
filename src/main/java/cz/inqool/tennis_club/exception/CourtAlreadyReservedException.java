package cz.inqool.tennis_club.exception;

import java.time.LocalDateTime;
import java.util.UUID;

public class CourtAlreadyReservedException extends RuntimeException {

    public CourtAlreadyReservedException(UUID courtId, LocalDateTime startTime, LocalDateTime endTime) {
        super("Court with id " + courtId + " is already reserved somewhere between " + startTime + " and " + endTime);
    }

}
