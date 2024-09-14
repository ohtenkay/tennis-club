package cz.inqool.tennis_club.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cz.inqool.tennis_club.exception.SurfaceTypeNotFoundException;
import cz.inqool.tennis_club.model.SurfaceType;
import cz.inqool.tennis_club.model.create.CourtCreate;
import cz.inqool.tennis_club.repository.SurfaceTypeRepository;
import lombok.val;

@SpringBootTest
public class CourtServiceTest {

    @Autowired
    private CourtService courtService;
    @Autowired
    private SurfaceTypeRepository surfaceTypeRepository;

    @Test
    void testCreate() {
        val name = "Court 1";
        val description = "Regular court";
        val surfaceType = new SurfaceType("Clay", new BigDecimal(100));
        surfaceTypeRepository.save(surfaceType);

        val court = courtService.createCourt(new CourtCreate(surfaceType.getId(), name, description));

        assertNotNull(court);
        assertEquals(name, court.getName());
        assertEquals(description, court.getDescription());
        assertThrows(SurfaceTypeNotFoundException.class,
                () -> courtService.createCourt(new CourtCreate(UUID.randomUUID(), name, description)));
    }
    //
    // @Test
    // void testDeleteById() {
    // val savedSurfaceType = surfaceTypeRepository.create("Synthetic", new
    // BigDecimal(200));
    //
    // val savedCourt = courtRepository.create(savedSurfaceType.getId(), "Court 2",
    // "More regular court");
    // courtRepository.deleteById(savedCourt.getId());
    // val deletedCourt = courtRepository.findById(savedCourt.getId());
    //
    // assertTrue(deletedCourt.isPresent());
    // assertNotNull(deletedCourt.get().getDeletedAt());
    // assertNull(deletedCourt.get().getSurfaceType().getDeletedAt());
    // assertThrows(CourtNotFoundException.class, () ->
    // courtRepository.deleteById(UUID.randomUUID()));
    // }

}
