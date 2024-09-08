package cz.inqool.tennis_club;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cz.inqool.tennis_club.model.SurfaceType;
import cz.inqool.tennis_club.repository.SurfaceTypeRepository;

@SpringBootTest
public class SurfaceTypeRepositoryTest {

    @Autowired
    private SurfaceTypeRepository surfaceTypeRepository;

    @Test
    void testSave() {
        SurfaceType surfaceType = new SurfaceType("Clay", new BigDecimal(100));

        surfaceTypeRepository.save(surfaceType);

        SurfaceType savedSurfaceType = surfaceTypeRepository.findById(surfaceType.getId());
        assertNotNull(savedSurfaceType);
        assertEquals(surfaceType.getName(), savedSurfaceType.getName());
        assertEquals(surfaceType.getPricePerMinute(), savedSurfaceType.getPricePerMinute());
    }

    @Test
    void testDeleteById() {
        SurfaceType surfaceType = new SurfaceType("Synthetic", new BigDecimal(200));

        surfaceTypeRepository.save(surfaceType);

        surfaceTypeRepository.deleteById(surfaceType.getId());

        SurfaceType deletedSurfaceType = surfaceTypeRepository.findById(surfaceType.getId());
        assertNotNull(deletedSurfaceType);
        assertNotNull(deletedSurfaceType.getDeletedAt());
    }

}
