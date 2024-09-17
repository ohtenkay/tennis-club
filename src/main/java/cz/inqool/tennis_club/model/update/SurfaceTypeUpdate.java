package cz.inqool.tennis_club.model.update;

import java.math.BigDecimal;
import java.util.UUID;

public record SurfaceTypeUpdate(
        UUID id,
        String name,
        BigDecimal pricePerMinute) {
}
