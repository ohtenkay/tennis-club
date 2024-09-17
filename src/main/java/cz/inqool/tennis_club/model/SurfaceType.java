package cz.inqool.tennis_club.model;

import java.math.BigDecimal;
import java.util.UUID;

import cz.inqool.tennis_club.model.create.SurfaceTypeCreate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SurfaceType extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    private BigDecimal pricePerMinute;

    public SurfaceType(SurfaceTypeCreate surfaceTypeCreate) {
        this.name = surfaceTypeCreate.name();
        this.pricePerMinute = surfaceTypeCreate.pricePerMinute();
    }

}
