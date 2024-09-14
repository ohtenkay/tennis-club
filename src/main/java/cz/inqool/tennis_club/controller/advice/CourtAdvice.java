package cz.inqool.tennis_club.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cz.inqool.tennis_club.exception.CourtNotFoundException;
import cz.inqool.tennis_club.exception.SurfaceTypeNotFoundException;

@RestControllerAdvice
class CourtAdvice {

    @ExceptionHandler(CourtNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String CourtNotFoundHandler(CourtNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(SurfaceTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String SurfaceTypeNotFoundHandler(SurfaceTypeNotFoundException ex) {
        return "Error when creating Court: \n\t" + ex.getMessage();
    }

}
