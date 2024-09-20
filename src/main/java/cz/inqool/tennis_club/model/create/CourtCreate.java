package cz.inqool.tennis_club.model.create;

import java.util.UUID;

public record CourtCreate(
        UUID surfaceTypeId,
        String name,
        String description) {

    public String json() {
        return "{"
                + "\"surfaceTypeId\":\"" + surfaceTypeId + "\","
                + "\"name\":\"" + name + "\","
                + "\"description\":\"" + description + "\""
                + "}";
    }

}
