package cz.inqool.tennis_club.model.update;

import java.time.LocalDateTime;
import java.util.UUID;

import cz.inqool.tennis_club.model.Reservation;

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

    public ReservationUpdate(Reservation reservation) {
        this(reservation.getId(), reservation.getCourt().getId(), reservation.getUser().getId(),
                reservation.getStartTime(),
                reservation.getEndTime(), reservation.getGameType());
    }

    public String json() {
        return "{" +
                "\"courtId\":\"" + courtId + "\"," +
                "\"userId\":\"" + userId + "\"," +
                "\"startTime\":\"" + startTime + "\"," +
                "\"endTime\":\"" + endTime + "\"," +
                "\"gameType\":\"" + gameType + "\"" +
                "}";
    }

}
