package cz.inqool.tennis_club.model.send;

import java.time.Instant;
import java.util.UUID;

public record ReservationSend(
        UUID courtId,
        String phoneNumber,
        Instant startTime,
        Instant endTime,
        String gameType) {

    public ReservationSend {
        phoneNumber = phoneNumber == null ? null : phoneNumber.replaceAll("\\s", "");
    }
}
