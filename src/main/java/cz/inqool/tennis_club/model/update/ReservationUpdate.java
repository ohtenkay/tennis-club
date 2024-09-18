package cz.inqool.tennis_club.model.update;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationUpdate(
        UUID id,
        UUID courtId,
        UUID userId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String gameType) {

    public ReservationUpdate(UUID id, ReservationUpdateBody reservationUpdateBody) {
        this(id, reservationUpdateBody.courtId(), reservationUpdateBody.userId(), reservationUpdateBody.startTime(),
                reservationUpdateBody.endTime(), reservationUpdateBody.gameType());
    }

}
