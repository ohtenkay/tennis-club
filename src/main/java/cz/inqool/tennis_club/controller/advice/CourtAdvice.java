package cz.inqool.tennis_club.controller.advice;

import static cz.inqool.tennis_club.util.ExceptionUtils.getRootCause;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cz.inqool.tennis_club.controller.CourtController;
import cz.inqool.tennis_club.exception.CourtNotFoundException;
import cz.inqool.tennis_club.exception.SurfaceTypeNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.val;

@RestControllerAdvice(assignableTypes = CourtController.class)
public class CourtAdvice {

    @ExceptionHandler(CourtNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String CourtNotFoundHandler(CourtNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(SurfaceTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String SurfaceTypeNotFoundHandler(SurfaceTypeNotFoundException ex) {
        return "Error when working with court: \n\t" + ex.getMessage();
    }

    // Enables to see validation errors in the response
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String ConstrainViolationExceptionHandler(Exception ex) throws Exception {
        val rootCause = getRootCause(ex);
        if (rootCause instanceof ConstraintViolationException) {
            return "Error when working with court: \n\t" + rootCause.getMessage();
        }

        throw ex;
    }

}
