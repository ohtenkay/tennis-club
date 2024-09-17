package cz.inqool.tennis_club.model.update;

import static cz.inqool.tennis_club.util.PhoneNumberUtils.normalizePhoneNumber;

import java.util.UUID;

public record UserUpdate(
        UUID id,
        String phoneNumber,
        String name) {

    public UserUpdate {
        phoneNumber = normalizePhoneNumber(phoneNumber);
    }

}
