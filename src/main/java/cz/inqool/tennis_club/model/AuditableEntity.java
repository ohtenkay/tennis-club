package cz.inqool.tennis_club.model;

import java.time.Instant;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AuditableEntity {

    @NotNull
    private Instant createdAt = Instant.now();

    @NotNull
    private Instant updatedAt = Instant.now();

    private Instant deletedAt;

}
