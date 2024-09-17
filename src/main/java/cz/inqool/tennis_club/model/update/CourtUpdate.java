package cz.inqool.tennis_club.model.update;

import java.util.UUID;

public record CourtUpdate(
        UUID surfaceTypeId,
        String name,
        String description) {
}
