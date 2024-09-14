package cz.inqool.tennis_club.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

// TODO: Add valdations for name and description and so on and handle exceptions if they fail
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "surfaceTypeId", nullable = false, unique = true)
    @NotNull
    @NonNull
    private SurfaceType surfaceType;

    @NotNull
    @NonNull
    private String name;

    private String description;

    @NotNull
    private Instant createdAt = Instant.now();

    @NotNull
    private Instant updatedAt = Instant.now();

    private Instant deletedAt;

}
