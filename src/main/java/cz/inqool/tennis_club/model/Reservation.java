package cz.inqool.tennis_club.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation extends AuditableEntity {

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
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @NotNull
    @Pattern(regexp = "SINGLES|DOUBLES")
    private String gameType;

    public Reservation(Court court, User user, LocalDateTime startTime, LocalDateTime endTime, String gameType) {
        this.court = court;
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
        this.gameType = gameType;
    }

}
