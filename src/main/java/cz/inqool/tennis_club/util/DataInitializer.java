package cz.inqool.tennis_club.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import cz.inqool.tennis_club.model.Court;
import cz.inqool.tennis_club.model.SurfaceType;
import cz.inqool.tennis_club.model.User;
import cz.inqool.tennis_club.model.create.CourtCreate;
import cz.inqool.tennis_club.model.create.ReservationCreate;
import cz.inqool.tennis_club.model.create.SurfaceTypeCreate;
import cz.inqool.tennis_club.model.create.UserCreate;
import cz.inqool.tennis_club.model.response.ReservationResponse;
import cz.inqool.tennis_club.service.CourtService;
import cz.inqool.tennis_club.service.ReservationService;
import cz.inqool.tennis_club.service.SurfaceTypeService;
import cz.inqool.tennis_club.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final SurfaceTypeService surfaceTypeService;
    private final CourtService courtService;
    private final UserService userService;
    private final ReservationService reservationService;

    @Override
    public void run(ApplicationArguments args) {
        if (!args.containsOption("seed")) {
            return;
        }

        val entities = seedSurfaceTypesAndCourts();

        log.info("Created surface types: \n\t");
        entities.first().forEach(surfaceType -> log.info(surfaceType.getId() + "\n"));

        log.info("Created courts: \n\t");
        entities.second().forEach(court -> log.info(court.getId() + "\n"));
    }

    public Pair<List<SurfaceType>, List<Court>> seedSurfaceTypesAndCourts() {
        val st1 = surfaceTypeService.createSurfaceType(new SurfaceTypeCreate("Clay", new BigDecimal(100)));
        val st2 = surfaceTypeService.createSurfaceType(new SurfaceTypeCreate("Synthetic", new BigDecimal(200)));

        val c1 = courtService.createCourt(new CourtCreate(st1.getId(), "Court 1", "Regular court"));
        val c2 = courtService.createCourt(new CourtCreate(st1.getId(), "Court 2", "More regular court"));
        val c3 = courtService.createCourt(new CourtCreate(st2.getId(), "Court 3", "Synthetic court"));
        val c4 = courtService.createCourt(new CourtCreate(st2.getId(), "Court 4", "Synthetic court, but bigger"));

        return Pair.of(List.of(st1, st2), List.of(c1, c2, c3, c4));
    }

    public Pair<List<User>, List<ReservationResponse>> seedUsersAndReservations(List<Court> courts) {
        val u1 = userService.createUser(new UserCreate("999999999", "Geralt"));
        val u2 = userService.createUser(new UserCreate("888888888", "Yennefer"));

        val r1 = reservationService
                .createReservation(new ReservationCreate(courts.get(0).getId(), u1.getPhoneName().getPhoneNumber(),
                        u1.getPhoneName().getName(), LocalDateTime.of(2021, 1, 1, 10, 0),
                        LocalDateTime.of(2021, 1, 1, 11, 0), "SINGLES"));
        val r2 = reservationService
                .createReservation(new ReservationCreate(courts.get(1).getId(), u2.getPhoneName().getPhoneNumber(),
                        u2.getPhoneName().getName(), LocalDateTime.of(2021, 1, 1, 12, 0),
                        LocalDateTime.of(2021, 1, 1, 13, 0), "DOUBLES"));
        val r3 = reservationService
                .createReservation(new ReservationCreate(courts.get(2).getId(), u1.getPhoneName().getPhoneNumber(),
                        u1.getPhoneName().getName(), LocalDateTime.of(2021, 1, 1, 14, 0),
                        LocalDateTime.of(2021, 1, 1, 15, 0), "SINGLES"));

        return Pair.of(List.of(u1, u2), List.of(r1, r2, r3));
    }

}
