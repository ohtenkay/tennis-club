package cz.inqool.tennis_club.controller;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import cz.inqool.tennis_club.exception.CourtNotFoundException;
import cz.inqool.tennis_club.model.Court;
import cz.inqool.tennis_club.model.SurfaceType;
import cz.inqool.tennis_club.model.create.CourtCreate;
import cz.inqool.tennis_club.model.update.CourtUpdate;
import cz.inqool.tennis_club.service.CourtService;
import cz.inqool.tennis_club.util.DataInitializer;
import lombok.val;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourtControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataInitializer dataInitializer;
    @Autowired
    private CourtService courtService;
    private static final String COURTS = "/api/courts";
    private static List<SurfaceType> surfaceTypes;
    private static List<Court> courts;

    @BeforeAll
    public void setUp() {
        val surfaceTypesAndCourts = dataInitializer.seedSurfaceTypesAndCourts();
        surfaceTypes = surfaceTypesAndCourts.first();
        courts = surfaceTypesAndCourts.second();
    }

    @Test
    public void testGetCourts() throws Exception {
        mockMvc.perform(get(COURTS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetCourtById_Correct() throws Exception {
        val court = courts.get(0);
        mockMvc.perform(get(COURTS + "/" + court.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(court.getId().toString()))
                .andExpect(jsonPath("$.name").value(court.getName()))
                .andExpect(jsonPath("$.surfaceType.id").value(court.getSurfaceType().getId().toString()))
                .andExpect(jsonPath("$.surfaceType.name").value(court.getSurfaceType().getName()));
    }

    @Test
    public void testGetCourtById_Incorrect() throws Exception {
        val randomId = UUID.randomUUID();
        val response = mockMvc.perform(get(COURTS + "/" + randomId))
                .andExpect(status().isNotFound())
                .andReturn();

        val errorMessage = response.getResponse().getContentAsString();
        assertTrue(errorMessage.contains(randomId.toString()));
    }

    @Test
    public void testPostCourt_Correct() throws Exception {
        val surfaceType = surfaceTypes.get(0);
        val court = new CourtCreate(surfaceType.getId(), "Court 4", "Description");

        mockMvc.perform(post(COURTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(court.json()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(court.name()))
                .andExpect(jsonPath("$.surfaceType.id").value(surfaceType.getId().toString()))
                .andExpect(jsonPath("$.surfaceType.name").value(surfaceType.getName()));
    }

    @Test
    public void testPostCourt_Incorrect_WrongSurfaceTypeId() throws Exception {
        val court = new CourtCreate(UUID.randomUUID(), "Court 4", "Description");

        val response = mockMvc.perform(post(COURTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(court.json()))
                .andExpect(status().isNotFound())
                .andReturn();

        val errorMessage = response.getResponse().getContentAsString();
        assertTrue(errorMessage.contains(court.surfaceTypeId().toString()));
    }

    @Test
    public void testPostCourt_Incorrect_EmptyName() throws Exception {
        val surfaceType = surfaceTypes.get(0);
        val court = new CourtCreate(surfaceType.getId(), "", "Description");

        val response = mockMvc.perform(post(COURTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(court.json()))
                .andExpect(status().isBadRequest())
                .andReturn();

        val errorMessage = response.getResponse().getContentAsString();
        assertTrue(errorMessage.contains("name"));
    }

    @Test
    public void testPutCourt_Correct() throws Exception {
        val court = courts.get(0);
        court.setName("Court 5");
        val courtUpdate = new CourtUpdate(court);

        mockMvc.perform(put(COURTS + "/" + court.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(courtUpdate.json()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(court.getId().toString()))
                .andExpect(jsonPath("$.name").value(courtUpdate.name()))
                .andExpect(jsonPath("$.surfaceType.id").value(court.getSurfaceType().getId().toString()))
                .andExpect(jsonPath("$.surfaceType.name").value(court.getSurfaceType().getName()));
    }

    @Test
    public void testDeleteCourt() throws Exception {
        val court = courts.get(1);
        mockMvc.perform(delete(COURTS + "/" + court.getId()))
                .andExpect(status().isNoContent());

        assertThrows(CourtNotFoundException.class, () -> courtService.getCourtById(court.getId()));
    }

}
