package cz.inqool.tennis_club.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cz.inqool.tennis_club.model.SurfaceType;
import lombok.val;

@SpringBootTest
public class SurfaceTypeRepositoryTest {

    @Autowired
    private SurfaceTypeRepository surfaceTypeRepository;

    @Test
    void testSave() {
        val surfaceType = new SurfaceType("Clay", new BigDecimal(100));
        surfaceTypeRepository.save(surfaceType);

        val savedSurfaceType = surfaceTypeRepository.findById(surfaceType.getId());

        assertTrue(savedSurfaceType.isPresent());
        assertEquals(surfaceType.getName(), savedSurfaceType.get().getName());
        assertEquals(surfaceType.getPricePerMinute(), savedSurfaceType.get().getPricePerMinute());
    }

    @Test
    void testDelete() {
        val savedSurfaceType = new SurfaceType("Synthetic", new BigDecimal(200));
        surfaceTypeRepository.save(savedSurfaceType);

        surfaceTypeRepository.delete(savedSurfaceType);
        val deletedSurfaceType = surfaceTypeRepository.findById(savedSurfaceType.getId());

        assertTrue(deletedSurfaceType.isPresent());
        assertNotNull(deletedSurfaceType.get().getDeletedAt());
    }

}
