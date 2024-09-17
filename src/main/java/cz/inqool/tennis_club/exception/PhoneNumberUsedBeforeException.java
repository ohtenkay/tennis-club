package cz.inqool.tennis_club.exception;

public class PhoneNumberUsedBeforeException extends RuntimeException {

    public PhoneNumberUsedBeforeException(String phoneNumber) {
        super("Phone number: " + phoneNumber + " has been used before. Do you wish to reactivate it?");
    }

}
