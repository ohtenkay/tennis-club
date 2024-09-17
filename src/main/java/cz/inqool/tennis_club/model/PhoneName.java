package cz.inqool.tennis_club.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PhoneName extends AuditableEntity {

    @Id
    @Column(unique = true, nullable = false)
    @NotNull
    @Pattern(regexp = "^\\+420[1-9][0-9]{8}$")
    private String phoneNumber;

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    public PhoneName(String phoneNumber, String name) {
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

}
