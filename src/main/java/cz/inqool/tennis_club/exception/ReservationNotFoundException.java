package cz.inqool.tennis_club.exception;

import java.util.UUID;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(UUID reservationId) {
        super("User with ID " + reservationId + " not found");
    }
}
