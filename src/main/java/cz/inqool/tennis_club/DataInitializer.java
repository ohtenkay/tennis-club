package cz.inqool.tennis_club;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import cz.inqool.tennis_club.repository.CourtRepository;
import cz.inqool.tennis_club.repository.SurfaceTypeRepository;
import lombok.val;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private SurfaceTypeRepository surfaceTypeRepository;
    @Autowired
    private CourtRepository courtRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!args.containsOption("seed")) {
            return;
        }

        val st1 = surfaceTypeRepository.create("Clay", new BigDecimal(100));
        val st2 = surfaceTypeRepository.create("Synthetic", new BigDecimal(200));

        courtRepository.create(st1.getId(), "Court 1", "Regular court");
        courtRepository.create(st1.getId(), "Court 2", "More regular court");
        courtRepository.create(st2.getId(), "Court 3", "Synthetic court");
        courtRepository.create(st2.getId(), "Court 4", "Synthetic court, but bigger");

    }

}
