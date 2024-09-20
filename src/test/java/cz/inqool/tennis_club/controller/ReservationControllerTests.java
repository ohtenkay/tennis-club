package cz.inqool.tennis_club.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import cz.inqool.tennis_club.exception.ReservationNotFoundException;
import cz.inqool.tennis_club.model.Court;
import cz.inqool.tennis_club.model.User;
import cz.inqool.tennis_club.model.create.ReservationCreate;
import cz.inqool.tennis_club.model.response.ReservationResponse;
import cz.inqool.tennis_club.model.update.ReservationUpdate;
import cz.inqool.tennis_club.service.ReservationService;
import cz.inqool.tennis_club.util.DataInitializer;
import lombok.val;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataInitializer dataInitializer;
    @Autowired
    private ReservationService reservationService;
    private static final String RESERVATIONS = "/api/reservations";
    private static List<Court> courts;
    private static List<User> users;
    private static List<ReservationResponse> reservations;

    @BeforeAll
    public void setUp() {
        val surfaceTypesAndCourts = dataInitializer.seedSurfaceTypesAndCourts();
        courts = surfaceTypesAndCourts.second();

        val usersAndReservations = dataInitializer.seedUsersAndReservations(courts);
        users = usersAndReservations.first();
        reservations = usersAndReservations.second();
    }

    @Test
    public void testGetReservations_Correct() throws Exception {
        mockMvc.perform(get(RESERVATIONS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetReservations_WithUserFilter_Correct() throws Exception {
        val user = users.get(1);
        val phoneNumber = user.getPhoneName().getPhoneNumber();

        mockMvc.perform(get(RESERVATIONS + "?phoneNumber=" + phoneNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetReservations_WithCourtFilter_Correct() throws Exception {
        val courtId = courts.get(0).getId();

        mockMvc.perform(get(RESERVATIONS + "?courtId=" + courtId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetReservations_WithUserFilter_Incorrect() throws Exception {
        val phoneNumber = "123";

        val result = mockMvc.perform(get(RESERVATIONS + "?phoneNumber=" + phoneNumber))
                .andExpect(status().isNotFound())
                .andReturn();

        val message = result.getResponse().getContentAsString();
        assertTrue(message.contains(phoneNumber));
    }

    @Test
    public void testGetReservations_WithCourtFilter_Incorrect() throws Exception {
        val courtId = UUID.randomUUID();

        val result = mockMvc.perform(get(RESERVATIONS + "?courtId=" + courtId))
                .andExpect(status().isNotFound())
                .andReturn();

        val message = result.getResponse().getContentAsString();
        assertTrue(message.contains(courtId.toString()));
    }

    @Test
    public void testGetReservations_WithFutureFilter_Correct() throws Exception {
        mockMvc.perform(get(RESERVATIONS + "?future=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testGetReservations_WithOrderFilter_Incorrect() throws Exception {
        val order = "random";

        val result = mockMvc.perform(get(RESERVATIONS + "?order=" + order))
                .andExpect(status().isBadRequest())
                .andReturn();

        val message = result.getResponse().getContentAsString();
        assertTrue(message.contains(order));
    }

    @Test
    public void testGetReservationById_Correct() throws Exception {
        val reservationId = reservations.get(0).reservationId();

        mockMvc.perform(get(RESERVATIONS + "/" + reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservationId.toString()));
    }

    @Test
    public void testPostReservation_Correct() throws Exception {
        val reservationCreate = new ReservationCreate(courts.get(0).getId(), "420722888123", "Geralt",
                LocalDateTime.of(2022, 1, 1, 10, 0), LocalDateTime.of(2022, 1, 1, 11, 0), "SINGLES");
        val price = courts.get(0).getSurfaceType().getPricePerMinute().multiply(BigDecimal.valueOf(60));

        mockMvc.perform(post(RESERVATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservationCreate.json()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(price));

    }

    // The reservaiton is already created in the DataInitializer
    @Test
    public void testPostReservation_CourtAlreadyReserved() throws Exception {
        val reservationCreate = new ReservationCreate(courts.get(0).getId(), "420722888123", "Geralt",
                LocalDateTime.of(2021, 1, 1, 10, 0), LocalDateTime.of(2021, 1, 1, 11, 0), "SINGLES");

        val result = mockMvc.perform(post(RESERVATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservationCreate.json()))
                .andExpect(status().isConflict())
                .andReturn();

        val message = result.getResponse().getContentAsString();
        assertTrue(message.contains(courts.get(0).getId().toString()));
    }

    @Test
    public void testPostReservation_IncorrectCourtId() throws Exception {
        val randomId = UUID.randomUUID();
        val reservationCreate = new ReservationCreate(randomId, "420722888123", "Geralt", LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), "SINGLES");

        val result = mockMvc.perform(post(RESERVATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservationCreate.json()))
                .andExpect(status().isNotFound())
                .andReturn();

        val message = result.getResponse().getContentAsString();
        assertTrue(message.contains(randomId.toString()));
    }

    @Test
    public void testPostReservation_IncorrectPhoneNumber() throws Exception {
        val reservationCreate = new ReservationCreate(courts.get(1).getId(), "123", "Geralt", LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), "SINGLES");

        val result = mockMvc.perform(post(RESERVATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservationCreate.json()))
                .andExpect(status().isBadRequest())
                .andReturn();

        val message = result.getResponse().getContentAsString();
        assertTrue(message.contains("phoneNumber"));
    }

    @Test
    public void testPostReservation_IncorrectGameType() throws Exception {
        val reservationCreate = new ReservationCreate(courts.get(2).getId(), "420722888123", "Geralt",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), "Singles");

        val result = mockMvc.perform(post(RESERVATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservationCreate.json()))
                .andExpect(status().isBadRequest())
                .andReturn();

        val message = result.getResponse().getContentAsString();
        assertTrue(message.contains("gameType"));
    }

    @Test
    void testPutReservation_Correct() throws Exception {
        val reservationId = reservations.get(1).reservationId();
        val reservation = reservationService.getReservationById(reservationId);
        val newStartTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        val newEndTime = LocalDateTime.of(2022, 1, 1, 13, 30);
        reservation.setStartTime(newStartTime);
        reservation.setEndTime(newEndTime);

        val multiplier = reservation.getGameType().equals("DOUBLES") ? BigDecimal.valueOf(1.5) : BigDecimal.valueOf(1);
        val newPrice = courts.get(0).getSurfaceType().getPricePerMinute().multiply(multiplier)
                .multiply(BigDecimal.valueOf(90));

        val reservationUpdate = new ReservationUpdate(reservation);

        mockMvc.perform(put(RESERVATIONS + "/" + reservation.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservationUpdate.json()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(newPrice));
    }

    @Test
    public void testDeleteReservation_Correct() throws Exception {
        val reservationId = reservations.get(2).reservationId();

        mockMvc.perform(delete(RESERVATIONS + "/" + reservationId))
                .andExpect(status().isNoContent());

        assertThrows(ReservationNotFoundException.class, () -> reservationService.getReservationById(reservationId));
    }

}
