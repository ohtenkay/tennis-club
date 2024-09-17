package cz.inqool.tennis_club.model.create;

import java.time.Instant;
import java.util.UUID;

public record ReservationCreate(
        UUID courtId,
        String phoneNumber,
        String name,
        Instant startTime,
        Instant endTime,
        String gameType) {
}
