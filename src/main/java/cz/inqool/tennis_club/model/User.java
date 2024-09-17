package cz.inqool.tennis_club.model;

import java.util.UUID;

import cz.inqool.tennis_club.model.create.UserCreate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "AppUser")
@Getter
@Setter
@NoArgsConstructor
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "phoneNumber", nullable = false, unique = true)
    @NotNull
    private PhoneName phoneName;

    public User(UserCreate userCreate) {
        this.phoneName = new PhoneName(userCreate.phoneNumber(), userCreate.name());
    }

}
