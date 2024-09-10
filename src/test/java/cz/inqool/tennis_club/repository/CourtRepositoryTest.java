package cz.inqool.tennis_club.repository;

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
import lombok.val;

@SpringBootTest
public class CourtRepositoryTest {

    @Autowired
    private CourtRepository courtRepository;
    @Autowired
    private SurfaceTypeRepository surfaceTypeRepository;

    @Test
    void testCreate() {
        val name = "Court 1";
        val description = "Regular court";
        val surfaceType = surfaceTypeRepository.create("Clay", new BigDecimal(100));

        val savedSurfaceType = courtRepository.create(surfaceType.getId(), name, description);

        assertNotNull(savedSurfaceType);
        assertEquals(name, savedSurfaceType.getName());
        assertEquals(description, savedSurfaceType.getDescription());
        assertThrows(SurfaceTypeNotFoundException.class,
                () -> courtRepository.create(UUID.randomUUID(), name, description));
    }

    @Test
    void testDeleteById() {
        val savedSurfaceType = surfaceTypeRepository.create("Synthetic", new BigDecimal(200));
        val savedCourt = courtRepository.create(savedSurfaceType.getId(), "Court 2", "More regular court");
        courtRepository.deleteById(savedCourt.getId());
        val deletedCourt = courtRepository.findById(savedCourt.getId());

        assertTrue(deletedCourt.isPresent());
        assertNotNull(deletedCourt.get().getDeletedAt());
        assertNull(deletedCourt.get().getSurfaceType().getDeletedAt());
        assertThrows(CourtNotFoundException.class, () -> courtRepository.deleteById(UUID.randomUUID()));
    }

}
