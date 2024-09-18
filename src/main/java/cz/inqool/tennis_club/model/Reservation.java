package cz.inqool.tennis_club.model;

import java.time.LocalDateTime;
import java.util.UUID;

import cz.inqool.tennis_club.model.create.ReservationCreate;
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

    // TODO: thin about this
    public Reservation(Court court, User user, ReservationCreate reservationCreate) {
        this.court = court;
        this.user = user;
        this.startTime = reservationCreate.startTime();
        this.endTime = reservationCreate.endTime();
        this.gameType = reservationCreate.gameType();
    }

}
