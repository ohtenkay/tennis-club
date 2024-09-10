package cz.inqool.tennis_club.exception;

import java.util.UUID;

public class SurfaceTypeNotFoundException extends RuntimeException {
    public SurfaceTypeNotFoundException(UUID surfaceTypeId) {
        super("Surface type with ID " + surfaceTypeId + " not found");
    }
}
