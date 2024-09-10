package cz.inqool.tennis_club;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

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

        surfaceTypeRepository.create("Clay", new BigDecimal(100));
        surfaceTypeRepository.create("Synthetic", new BigDecimal(200));

    }

}
