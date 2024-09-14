package cz.inqool.tennis_club;

import java.math.BigDecimal;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import cz.inqool.tennis_club.model.SurfaceType;
import cz.inqool.tennis_club.model.create.CourtCreate;
import cz.inqool.tennis_club.repository.SurfaceTypeRepository;
import cz.inqool.tennis_club.service.CourtService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final SurfaceTypeRepository surfaceTypeRepository;
    private final CourtService courtService;

    @Override
    public void run(ApplicationArguments args) {
        if (!args.containsOption("seed")) {
            return;
        }

        val st1 = new SurfaceType("Clay", new BigDecimal(100));
        surfaceTypeRepository.save(st1);
        val st2 = new SurfaceType("Synthetic", new BigDecimal(200));
        surfaceTypeRepository.save(st2);

        val c1 = courtService.createCourt(new CourtCreate(st1.getId(), "Court 1", "Regular court"));
        val c2 = courtService.createCourt(new CourtCreate(st1.getId(), "Court 2", "More regular court"));
        val c3 = courtService.createCourt(new CourtCreate(st2.getId(), "Court 3", "Synthetic court"));
        val c4 = courtService.createCourt(new CourtCreate(st2.getId(), "Court 4", "Synthetic court, but bigger"));

        log.info("Created surface types: \n\t" + st1.getId() + "\n\t" + st2.getId() + "\n");
        log.info("Created courts: \n\t" + c1.getId() + "\n\t" + c2.getId() + "\n\t" + c3.getId() + "\n\t" + c4.getId());
    }

}
