package cz.inqool.tennis_club.controller.advice;

import static cz.inqool.tennis_club.util.ExceptionUtils.getRootCause;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cz.inqool.tennis_club.controller.ReservationController;
import cz.inqool.tennis_club.exception.CourtNotFoundException;
import cz.inqool.tennis_club.exception.InvalidOrderException;
import cz.inqool.tennis_club.exception.PhoneNumberUsedBeforeException;
import cz.inqool.tennis_club.exception.ReservationNotFoundException;
import cz.inqool.tennis_club.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.val;

@RestControllerAdvice(assignableTypes = ReservationController.class)
public class ReservationAdvice {

    @ExceptionHandler(ReservationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String ReservationNotFoundHandler(ReservationNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CourtNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String CourtNotFoundHandler(CourtNotFoundException ex) {
        return "Error when working with reservation: \n\t" + ex.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String UserNotFoundHandler(UserNotFoundException ex) {
        return "Error when working with reservation: \n\t" + ex.getMessage();
    }

    @ExceptionHandler(PhoneNumberUsedBeforeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String PhoneNumberUsedBeforeHandler(PhoneNumberUsedBeforeException ex) {
        return "Error when working with reservation: \n\t" + ex.getMessage();
    }

    @ExceptionHandler(InvalidOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String InvalidOrderHandler(InvalidOrderException ex) {
        return "Error when working with reservation: \n\t" + ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String ExceptionHandler(Exception ex) throws Exception {
        val rootCause = getRootCause(ex);
        if (rootCause instanceof ConstraintViolationException) {
            return "Error when working with reservation: \n\t" + rootCause.getMessage();
        }

        throw ex;
    }

}
