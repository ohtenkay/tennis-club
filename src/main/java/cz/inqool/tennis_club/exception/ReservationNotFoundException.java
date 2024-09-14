package cz.inqool.tennis_club.exception;

import java.util.UUID;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(UUID reservationId) {
        super("Could not find reservation with ID: " + reservationId);
    }

}
