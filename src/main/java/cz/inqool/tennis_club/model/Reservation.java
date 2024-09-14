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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "courtId", nullable = false, unique = true)
    @NotNull
    private Court court;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, unique = true)
    @NotNull
    private User user;

    @NotNull
    private Instant startTime;

    @NotNull
    private Instant endTime;

    @NotNull
    @Setter(AccessLevel.NONE)
    private String gameType;

    @NotNull
    private Instant createdAt = Instant.now();

    @NotNull
    private Instant updatedAt = Instant.now();

    private Instant deletedAt;

    public Reservation(Court court, User user, Instant startTime, Instant endTime, String gameType) {
        // TODO: checking of the time range, think about custom validadion
        this.court = court;
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
        setGameType(gameType);
    }

    public void setGameType(String gameType) {
        if (!gameType.equals("SINGLES") && !gameType.equals("DOUBLES")) {
            throw new IllegalArgumentException("Invalid game type: " + gameType);
        }
        this.gameType = gameType;
    }

}
