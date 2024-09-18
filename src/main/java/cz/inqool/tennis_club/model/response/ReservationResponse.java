package cz.inqool.tennis_club.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import cz.inqool.tennis_club.model.Reservation;
import lombok.val;

public record ReservationResponse(
        BigDecimal price,
        UUID reservationId,
        UUID courtId,
        String phoneNumber,
        String name,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String gameType) {

    public ReservationResponse(Reservation reservation) {
        this(
                calculatePrice(reservation),
                reservation.getId(),
                reservation.getCourt().getId(),
                reservation.getUser().getPhoneName().getPhoneNumber(),
                reservation.getUser().getPhoneName().getName(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getGameType());
    }

    private static BigDecimal calculatePrice(Reservation reservation) {
        val multiplier = reservation.getGameType().equals("DOUBLES") ? BigDecimal.valueOf(1.5) : BigDecimal.valueOf(1);
        val durationInMinutes = BigDecimal.valueOf(
                reservation.getStartTime().until(reservation.getEndTime(), ChronoUnit.MINUTES));

        return reservation.getCourt().getSurfaceType().getPricePerMinute().multiply(durationInMinutes)
                .multiply(multiplier);
    }

}
