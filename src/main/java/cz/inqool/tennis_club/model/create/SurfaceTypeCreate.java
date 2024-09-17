package cz.inqool.tennis_club.model.create;

import java.math.BigDecimal;

public record SurfaceTypeCreate(
        String name,
        BigDecimal pricePerMinute) {
}
