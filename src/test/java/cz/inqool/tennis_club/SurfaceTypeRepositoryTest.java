package cz.inqool.tennis_club;

import java.time.Instant;

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
    void trestSaveSurfaceType() {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setName("Clay");
        surfaceType.setPricePerMinute(100);
        surfaceType.setCreatedAt(Instant.now());
        surfaceType.setUpdatedAt(Instant.now());

        surfaceTypeRepository.save(surfaceType);

        System.out.println("Saved surface type: " + surfaceType.getId());
    }

}
