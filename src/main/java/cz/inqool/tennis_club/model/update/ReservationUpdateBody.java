package cz.inqool.tennis_club.model.update;

import java.time.Instant;
import java.util.UUID;

public record ReservationUpdateBody(
        UUID courtId,
        UUID userId,
        Instant startTime,
        Instant endTime,
        String gameType) {
}
