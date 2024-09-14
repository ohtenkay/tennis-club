package cz.inqool.tennis_club.exception;

import java.util.UUID;

public class SurfaceTypeNotFoundException extends RuntimeException {

    public SurfaceTypeNotFoundException(UUID surfaceTypeId) {
        super("Could not find surface type with ID: " + surfaceTypeId);
    }

}
