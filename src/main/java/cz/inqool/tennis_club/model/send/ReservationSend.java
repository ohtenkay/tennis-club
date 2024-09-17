package cz.inqool.tennis_club.model.send;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record ReservationSend(
        UUID courtId,
        String phoneNumber,
        Instant startTime,
        Instant endTime,
        String gameType) {

    public ReservationSend {
        phoneNumber = Optional.ofNullable(phoneNumber)
                .map(pn -> pn.replaceAll("\\s+", ""))
                .map(pn -> pn.startsWith("00420") ? pn.replaceFirst("00420", "+420") : pn)
                .map(pn -> pn.startsWith("420") ? pn.replaceFirst("420", "+420") : pn)
                .map(pn -> pn.startsWith("+420") ? pn : "+420" + pn)
                .orElse(phoneNumber);
    }
}
