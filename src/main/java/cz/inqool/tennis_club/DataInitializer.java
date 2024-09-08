package cz.inqool.tennis_club;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import cz.inqool.tennis_club.model.SurfaceType;
import cz.inqool.tennis_club.repository.SurfaceTypeRepository;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private SurfaceTypeRepository surfaceTypeRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!args.containsOption("seed")) {
            return;
        }

        surfaceTypeRepository.save(new SurfaceType("Clay", new BigDecimal(100)));
        surfaceTypeRepository.save(new SurfaceType("Synthetic", new BigDecimal(200)));

    }

}
