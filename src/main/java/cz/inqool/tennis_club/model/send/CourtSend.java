package cz.inqool.tennis_club.model.send;

import java.util.UUID;

public record CourtSend(
        UUID surfaceTypeId,
        String name,
        String description) {
}
