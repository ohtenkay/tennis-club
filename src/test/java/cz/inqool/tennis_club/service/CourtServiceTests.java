package cz.inqool.tennis_club.service;

import static cz.inqool.tennis_club.exception.ExceptionUtils.getRootCause;
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
import cz.inqool.tennis_club.model.SurfaceType;
import cz.inqool.tennis_club.model.send.CourtSend;
import cz.inqool.tennis_club.repository.CourtRepository;
import cz.inqool.tennis_club.repository.SurfaceTypeRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.val;

@SpringBootTest
public class CourtServiceTests {

    @Autowired
    private CourtService courtService;
    @Autowired
    private CourtRepository courtRepository;
    @Autowired
    private SurfaceTypeRepository surfaceTypeRepository;

    @Test
    void testCreate() {
        val name = "Court 1";
        val description = "Regular court";
        val surfaceType = new SurfaceType("Clay", new BigDecimal(100));
        surfaceTypeRepository.save(surfaceType);

        val court = courtService.createCourt(new CourtSend(surfaceType.getId(), name, description));

        assertNotNull(court);
        assertEquals(name, court.getName());
        assertEquals(description, court.getDescription());
        assertThrows(SurfaceTypeNotFoundException.class,
                () -> courtService.createCourt(new CourtSend(UUID.randomUUID(), name, description)));
    }

    @Test
    void testDelete() {
        val savedSurfaceType = new SurfaceType("Synthetic", new BigDecimal(200));
        surfaceTypeRepository.save(savedSurfaceType);

        val savedCourt = courtService.createCourt(new CourtSend(savedSurfaceType.getId(), "Court 2",
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
        val savedSurfaceType = new SurfaceType("Synthetic", new BigDecimal(200));
        surfaceTypeRepository.save(savedSurfaceType);

        val savedCourt = courtService.createCourt(new CourtSend(savedSurfaceType.getId(), "Court 2",
                "More regular court"));
        val updatedCourt = courtService.updateCourt(savedCourt.getId(),
                new CourtSend(savedSurfaceType.getId(), "Court 3", "Even more regular court"));

        assertEquals("Court 3", updatedCourt.getName());
        assertEquals("Even more regular court", updatedCourt.getDescription());

        assertThrows(SurfaceTypeNotFoundException.class,
                () -> courtService.updateCourt(savedCourt.getId(),
                        new CourtSend(UUID.randomUUID(), "Court 4", "Even more regular court")));
        assertThrows(CourtNotFoundException.class,
                () -> courtService.updateCourt(UUID.randomUUID(),
                        new CourtSend(savedSurfaceType.getId(), "Court 5", "Even more regular court")));

        val nullDescriptionCourt = courtService.updateCourt(savedCourt.getId(),
                new CourtSend(savedSurfaceType.getId(), "Court 6", null));
        assertNull(nullDescriptionCourt.getDescription());

        try {
            courtService.updateCourt(savedCourt.getId(),
                    new CourtSend(savedSurfaceType.getId(), null, "Even more regular court"));
        } catch (Exception e) {
            assertTrue(getRootCause(e) instanceof ConstraintViolationException);
        }

        try {
            courtService.updateCourt(savedCourt.getId(),
                    new CourtSend(savedSurfaceType.getId(), "", "Even more regular court"));
        } catch (Exception e) {
            assertTrue(getRootCause(e) instanceof ConstraintViolationException);
        }
    }

}
