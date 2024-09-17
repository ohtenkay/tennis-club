package cz.inqool.tennis_club.model.create;

import static cz.inqool.tennis_club.util.PhoneNumberUtils.normalizePhoneNumber;

public record UserCreate(
        String phoneNumber,
        String name) {

    public UserCreate {
        phoneNumber = normalizePhoneNumber(phoneNumber);
    }
}
