package cz.inqool.tennis_club.exception;

public class InvalidOrderException extends RuntimeException {

    public InvalidOrderException(String order) {
        super("Invalid parameter order: " + order);
    }

}
