package cz.inqool.tennis_club.model.update;

import java.time.Instant;
import java.util.UUID;

public record ReservationUpdate(
        UUID id,
        UUID courtId,
        UUID userId,
        Instant startTime,
        Instant endTime,
        String gameType) {

    public ReservationUpdate(UUID id, ReservationUpdateBody reservationUpdateBody) {
        this(id, reservationUpdateBody.courtId(), reservationUpdateBody.userId(), reservationUpdateBody.startTime(),
                reservationUpdateBody.endTime(), reservationUpdateBody.gameType());
    }

}
