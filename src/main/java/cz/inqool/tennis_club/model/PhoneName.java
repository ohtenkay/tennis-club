package cz.inqool.tennis_club.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class PhoneName {

    @Id
    @Column(unique = true, nullable = false)
    @NonNull
    private String phoneNumber;

    @NotNull
    @NonNull
    private String name;

    @NotNull
    private Instant createdAt = Instant.now();

    @NotNull
    private Instant updatedAt = Instant.now();

    private Instant deletedAt;

}
