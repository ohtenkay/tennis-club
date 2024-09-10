package cz.inqool.tennis_club.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cz.inqool.tennis_club.exception.SurfaceTypeNotFoundException;
import lombok.val;

@SpringBootTest
public class SurfaceTypeRepositoryTest {

    @Autowired
    private SurfaceTypeRepository surfaceTypeRepository;

    @Test
    void testCreate() {
        val name = "Clay";
        val pricePerMinute = new BigDecimal(100);

        val savedSurfaceType = surfaceTypeRepository.create(name, pricePerMinute);

        assertNotNull(savedSurfaceType);
        assertEquals(name, savedSurfaceType.getName());
        assertEquals(pricePerMinute, savedSurfaceType.getPricePerMinute());
    }

    @Test
    void testDeleteById() {
        val savedSurfaceType = surfaceTypeRepository.create("Synthetic", new BigDecimal(200));
        surfaceTypeRepository.deleteById(savedSurfaceType.getId());
        val deletedSurfaceType = surfaceTypeRepository.findById(savedSurfaceType.getId());

        assertTrue(deletedSurfaceType.isPresent());
        assertNotNull(deletedSurfaceType.get().getDeletedAt());
        assertThrows(SurfaceTypeNotFoundException.class, () -> surfaceTypeRepository.deleteById(UUID.randomUUID()));
    }

}
