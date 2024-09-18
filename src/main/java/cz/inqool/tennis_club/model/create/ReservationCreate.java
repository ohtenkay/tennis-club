package cz.inqool.tennis_club.model.create;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationCreate(
        UUID courtId,
        String phoneNumber,
        String name,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String gameType) {
}
