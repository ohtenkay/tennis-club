package cz.inqool.tennis_club.service;

import static cz.inqool.tennis_club.util.ExceptionUtils.getRootCause;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cz.inqool.tennis_club.exception.CourtNotFoundException;
import cz.inqool.tennis_club.exception.SurfaceTypeNotFoundException;
import cz.inqool.tennis_club.model.create.CourtCreate;
import cz.inqool.tennis_club.model.create.SurfaceTypeCreate;
import cz.inqool.tennis_club.model.update.CourtUpdate;
import cz.inqool.tennis_club.repository.CourtRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.val;

@SpringBootTest
public class CourtServiceTests {

    @Autowired
    private SurfaceTypeService surfaceTypeService;
    @Autowired
    private CourtService courtService;
    @Autowired
    private CourtRepository courtRepository;

    @Test
    void testCreate() {
        val name = "Court 1";
        val description = "Regular court";
        val surfaceType = surfaceTypeService.createSurfaceType(new SurfaceTypeCreate("Clay", new BigDecimal(100)));

        val court = courtService.createCourt(new CourtCreate(surfaceType.getId(), name, description));

        assertNotNull(court);
        assertEquals(name, court.getName());
        assertEquals(description, court.getDescription());
        assertThrows(SurfaceTypeNotFoundException.class,
                () -> courtService.createCourt(new CourtCreate(UUID.randomUUID(), name, description)));
    }

    @Test
    void testDelete() {
        val savedSurfaceType = surfaceTypeService
                .createSurfaceType(new SurfaceTypeCreate("Synthetic", new BigDecimal(200)));

        val savedCourt = courtService.createCourt(new CourtCreate(savedSurfaceType.getId(), "Court 2",
                "More regular court"));
        courtService.deleteCourt(savedCourt.getId());
        val deletedCourt = courtRepository.findByIdWithDeleted(savedCourt.getId());

        assertTrue(deletedCourt.isPresent());
        assertNotNull(deletedCourt.get().getDeletedAt());
        assertNull(deletedCourt.get().getSurfaceType().getDeletedAt());
        assertThrows(CourtNotFoundException.class, () -> courtService.deleteCourt(UUID.randomUUID()));
    }

    @Test
    void testUpdate() {
        val savedSurfaceType = surfaceTypeService
                .createSurfaceType(new SurfaceTypeCreate("Synthetic", new BigDecimal(200)));

        val savedCourt = courtService.createCourt(new CourtCreate(savedSurfaceType.getId(), "Court 2",
                "More regular court"));
        val updatedCourt = courtService.updateCourt(
                new CourtUpdate(savedCourt.getId(), savedSurfaceType.getId(), "Court 3", "Even more regular court"));

        assertEquals("Court 3", updatedCourt.getName());
        assertEquals("Even more regular court", updatedCourt.getDescription());

        assertThrows(SurfaceTypeNotFoundException.class,
                () -> courtService.updateCourt(
                        new CourtUpdate(savedCourt.getId(), UUID.randomUUID(), "Court 4", "Even more regular court")));
        assertThrows(CourtNotFoundException.class,
                () -> courtService.updateCourt(
                        new CourtUpdate(UUID.randomUUID(), savedSurfaceType.getId(), "Court 5",
                                "Even more regular court")));

        val nullDescriptionCourt = courtService.updateCourt(
                new CourtUpdate(savedCourt.getId(), savedSurfaceType.getId(), "Court 6", null));
        assertNull(nullDescriptionCourt.getDescription());

        try {
            courtService.updateCourt(
                    new CourtUpdate(savedCourt.getId(), savedSurfaceType.getId(), null, "Even more regular court"));
        } catch (Exception e) {
            assertTrue(getRootCause(e) instanceof ConstraintViolationException);
        }

        try {
            courtService.updateCourt(
                    new CourtUpdate(savedCourt.getId(), savedSurfaceType.getId(), "", "Even more regular court"));
        } catch (Exception e) {
            assertTrue(getRootCause(e) instanceof ConstraintViolationException);
        }
    }

}
