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

    public String json() {
        return "{\n" +
                "  \"courtId\": \"" + courtId + "\",\n" +
                "  \"phoneNumber\": \"" + phoneNumber + "\",\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"startTime\": \"" + startTime + "\",\n" +
                "  \"endTime\": \"" + endTime + "\",\n" +
                "  \"gameType\": \"" + gameType + "\"\n" +
                "}";
    }

}
