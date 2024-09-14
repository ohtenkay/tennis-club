// package cz.inqool.tennis_club.repository;
//
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.junit.jupiter.api.Assertions.assertTrue;
//
// import java.math.BigDecimal;
// import java.time.Instant;
// import java.time.temporal.ChronoUnit;
// import java.util.UUID;
//
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.dao.InvalidDataAccessApiUsageException;
//
// import cz.inqool.tennis_club.exception.CourtNotFoundException;
// import cz.inqool.tennis_club.exception.ReservationNotFoundException;
// import cz.inqool.tennis_club.exception.UserNotFoundException;
// import lombok.val;
//
// @SpringBootTest
// public class ReservationRepositoryTest {
//
// @Autowired
// private CourtRepository courtRepository;
// @Autowired
// private SurfaceTypeRepository surfaceTypeRepository;
// @Autowired
// private UserRepository userRepository;
// @Autowired
// private ReservationRepository reservationRepository;
//
// @Test
// void testCreate() {
// val startTime = Instant.now();
// val endTime = Instant.now().plus(1, ChronoUnit.HOURS);
// val gameType = "SINGLES";
// val invalidGameType = "Singles";
// val surfaceType = surfaceTypeRepository.create("Clay", new BigDecimal(100));
// val court = courtRepository.create(surfaceType.getId(), "Court 1", "Regular
// court");
// val user = userRepository.create("Triss", "+420 333 456 789");
//
// val reservation = reservationRepository.create(court.getId(), user.getId(),
// startTime, endTime, gameType);
//
// assertNotNull(reservation);
// assertEquals(gameType, reservation.getGameType());
// assertThrows(UserNotFoundException.class,
// () -> reservationRepository.create(court.getId(), UUID.randomUUID(),
// startTime, endTime, gameType));
// assertThrows(CourtNotFoundException.class,
// () -> reservationRepository.create(UUID.randomUUID(), user.getId(),
// startTime, endTime, gameType));
//
// Exception exception = assertThrows(InvalidDataAccessApiUsageException.class,
// () -> reservationRepository.create(court.getId(), user.getId(), startTime,
// endTime, invalidGameType));
//
// Throwable cause = exception.getCause();
// assertTrue(cause instanceof IllegalArgumentException);
// assertEquals("Invalid game type: Singles", cause.getMessage());
//
// // TODO: Add tests for time interval checking
// }
//
// @Test
// void testDeleteById() {
// val savedSurfaceType = surfaceTypeRepository.create("Synthetic", new
// BigDecimal(200));
// val savedCourt = courtRepository.create(savedSurfaceType.getId(), "Court 2",
// "More regular court");
// val savedUser = userRepository.create("Yennefer", "+420 444 456 789");
//
// val savedReservation = reservationRepository.create(savedCourt.getId(),
// savedUser.getId(), Instant.now(),
// Instant.now().plus(1, ChronoUnit.HOURS), "SINGLES");
// reservationRepository.deleteById(savedReservation.getId());
// val deletedReservation =
// reservationRepository.findById(savedReservation.getId());
//
// assertTrue(deletedReservation.isPresent());
// assertNotNull(deletedReservation.get().getDeletedAt());
// assertNull(deletedReservation.get().getUser().getDeletedAt());
// assertThrows(ReservationNotFoundException.class, () ->
// reservationRepository.deleteById(UUID.randomUUID()));
// }
//
// }
