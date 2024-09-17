package cz.inqool.tennis_club.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cz.inqool.tennis_club.model.create.SurfaceTypeCreate;
import cz.inqool.tennis_club.model.update.SurfaceTypeUpdate;
import cz.inqool.tennis_club.repository.SurfaceTypeRepository;
import lombok.val;

@SpringBootTest
public class SurfaceTypeServiceTests {

    @Autowired
    private SurfaceTypeService surfaceTypeService;
    @Autowired
    private SurfaceTypeRepository surfaceTypeRepository;

    @Test
    void testSave() {
        val name = "Clay";
        val pricePerMinute = new BigDecimal(100);

        val surfaceType = surfaceTypeService.createSurfaceType(new SurfaceTypeCreate(name, pricePerMinute));

        assertNotNull(surfaceType);
        assertEquals(surfaceType.getName(), name);
        assertEquals(surfaceType.getPricePerMinute(), pricePerMinute);
    }

    @Test
    void testUpdate() {
        val savedSurfaceType = surfaceTypeService
                .createSurfaceType(new SurfaceTypeCreate("Grass", new BigDecimal(150)));
        val newName = "Synthetic";
        val newPricePerMinute = new BigDecimal(200);

        val updatedSurfaceType = surfaceTypeService.updateSurfaceType(new SurfaceTypeUpdate(savedSurfaceType.getId(),
                newName, newPricePerMinute));

        assertNotEquals(savedSurfaceType.getName(), updatedSurfaceType.getName());
        assertEquals(updatedSurfaceType.getName(), newName);
        assertNotEquals(savedSurfaceType.getPricePerMinute(), updatedSurfaceType.getPricePerMinute());
        assertEquals(updatedSurfaceType.getPricePerMinute(), newPricePerMinute);
    }

    @Test
    void testDelete() {
        val savedSurfaceType = surfaceTypeService
                .createSurfaceType(new SurfaceTypeCreate("Hard", new BigDecimal(120)));
        surfaceTypeService.deleteSurfaceType(savedSurfaceType.getId());

        val deletedSurfaceType = surfaceTypeRepository.findByIdWithDeleted(savedSurfaceType.getId());

        assertTrue(deletedSurfaceType.isPresent());
        assertNotNull(deletedSurfaceType.get().getDeletedAt());
    }

}
