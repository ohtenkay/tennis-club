package cz.inqool.tennis_club.model.update;

import java.util.UUID;

import cz.inqool.tennis_club.model.Court;

public record CourtUpdate(
        UUID id,
        UUID surfaceTypeId,
        String name,
        String description) {

    public CourtUpdate(UUID id, CourtUpdateBody courtUpdateBody) {
        this(id, courtUpdateBody.surfaceTypeId(), courtUpdateBody.name(), courtUpdateBody.description());
    }

    public CourtUpdate(Court court) {
        this(court.getId(), court.getSurfaceType().getId(), court.getName(), court.getDescription());
    }

    public String json() {
        return "{"
                + "\"id\":\"" + id + "\","
                + "\"surfaceTypeId\":\"" + surfaceTypeId + "\","
                + "\"name\":\"" + name + "\","
                + "\"description\":\"" + description + "\""
                + "}";
    }

}
