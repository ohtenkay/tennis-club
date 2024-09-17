package cz.inqool.tennis_club.model.update;

import java.util.UUID;

public record CourtUpdate(
        UUID id,
        UUID surfaceTypeId,
        String name,
        String description) {

    public CourtUpdate(UUID id, CourtUpdateBody courtUpdateBody) {
        this(id, courtUpdateBody.surfaceTypeId(), courtUpdateBody.name(), courtUpdateBody.description());
    }

}
