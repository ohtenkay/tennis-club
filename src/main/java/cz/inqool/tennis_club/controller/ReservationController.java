package cz.inqool.tennis_club.controller;

import static cz.inqool.tennis_club.util.PhoneNumberUtils.normalizePhoneNumber;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cz.inqool.tennis_club.model.Reservation;
import cz.inqool.tennis_club.model.create.ReservationCreate;
import cz.inqool.tennis_club.model.response.ReservationResponse;
import cz.inqool.tennis_club.model.update.ReservationUpdate;
import cz.inqool.tennis_club.model.update.ReservationUpdateBody;
import cz.inqool.tennis_club.service.ReservationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public List<Reservation> getReservations(@RequestParam(required = false) String phoneNumber,
            @RequestParam(defaultValue = "false") boolean future, @RequestParam(required = false) UUID courtId,
            @RequestParam(defaultValue = "asc") String order) {
        return reservationService.getReservations(normalizePhoneNumber(phoneNumber), future, courtId, order);
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable UUID id) {
        return reservationService.getReservationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse createReservation(@RequestBody ReservationCreate reservationCreate) {
        return reservationService.createReservation(reservationCreate);
    }

    @PutMapping("/{id}")
    public ReservationResponse updateReservation(@PathVariable UUID id,
            @RequestBody ReservationUpdateBody reservationUpdateBody) {
        return reservationService.updateReservation(new ReservationUpdate(id, reservationUpdateBody));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable UUID id) {
        reservationService.deleteReservation(id);
    }
}
