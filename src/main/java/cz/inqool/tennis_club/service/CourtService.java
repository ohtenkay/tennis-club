package cz.inqool.tennis_club.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import cz.inqool.tennis_club.exception.CourtNotFoundException;
import cz.inqool.tennis_club.exception.SurfaceTypeNotFoundException;
import cz.inqool.tennis_club.model.Court;
import cz.inqool.tennis_club.model.create.CourtCreate;
import cz.inqool.tennis_club.model.update.CourtUpdate;
import cz.inqool.tennis_club.repository.CourtRepository;
import cz.inqool.tennis_club.repository.SurfaceTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class CourtService {

    public final SurfaceTypeRepository surfaceTypeRepository;
    public final CourtRepository courtRepository;

    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    public Court getCourtById(UUID id) {
        return courtRepository.findById(id).orElseThrow(() -> new CourtNotFoundException(id));
    }

    @Transactional
    public Court createCourt(CourtCreate courtCreate) {
        val surfaceType = surfaceTypeRepository.findById(courtCreate.surfaceTypeId())
                .orElseThrow(() -> new SurfaceTypeNotFoundException(courtCreate.surfaceTypeId()));
        val court = new Court(surfaceType, courtCreate.name());
        court.setDescription(courtCreate.description());

        courtRepository.save(court);
        return court;
    }

    @Transactional
    public Court updateCourt(UUID id, CourtUpdate courtUpdate) {
        val surfaceType = surfaceTypeRepository.findById(courtUpdate.surfaceTypeId())
                .orElseThrow(() -> new SurfaceTypeNotFoundException(courtUpdate.surfaceTypeId()));
        val court = getCourtById(id);
        court.setSurfaceType(surfaceType);
        court.setName(courtUpdate.name());
        court.setDescription(courtUpdate.description());

        courtRepository.update(court);
        return court;
    }

    @Transactional
    public void deleteCourt(UUID id) {
        val court = getCourtById(id);

        courtRepository.delete(court);
    }
}
