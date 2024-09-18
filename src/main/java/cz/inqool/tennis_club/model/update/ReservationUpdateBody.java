package cz.inqool.tennis_club.model.update;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationUpdateBody(
        UUID courtId,
        UUID userId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String gameType) {
}
