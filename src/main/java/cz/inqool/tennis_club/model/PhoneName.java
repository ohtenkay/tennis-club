package cz.inqool.tennis_club.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class PhoneName {

    // TODO: Add validation for phone number
    @Id
    @Column(unique = true, nullable = false)
    @NotNull
    private String phoneNumber;

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    private Instant createdAt = Instant.now();

    @NotNull
    private Instant updatedAt = Instant.now();

    private Instant deletedAt;

    public PhoneName(String phoneNumber, String name) {
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

}
