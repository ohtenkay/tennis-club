package cz.inqool.tennis_club.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.val;

@SpringBootTest
public class SurfaceTypeRepositoryTest {

    @Autowired
    private SurfaceTypeRepository surfaceTypeRepository;

    @Test
    void testCreate() {
        val name = "Clay";
        val pricePerMinute = new BigDecimal(100);

        val id = surfaceTypeRepository.create(name, pricePerMinute);
        val savedSurfaceType = surfaceTypeRepository.findById(id);

        assertNotNull(savedSurfaceType);
        assertEquals(name, savedSurfaceType.getName());
        assertEquals(pricePerMinute, savedSurfaceType.getPricePerMinute());
    }

    @Test
    void testDeleteById() {
        val id = surfaceTypeRepository.create("Synthetic", new BigDecimal(200));
        surfaceTypeRepository.deleteById(id);
        val deletedSurfaceType = surfaceTypeRepository.findById(id);

        assertNotNull(deletedSurfaceType);
        assertNotNull(deletedSurfaceType.getDeletedAt());
    }

}
