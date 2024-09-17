package cz.inqool.tennis_club.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import cz.inqool.tennis_club.exception.CourtNotFoundException;
import cz.inqool.tennis_club.model.Court;
import cz.inqool.tennis_club.model.create.CourtCreate;
import cz.inqool.tennis_club.model.update.CourtUpdate;
import cz.inqool.tennis_club.repository.CourtRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class CourtService {

    public final SurfaceTypeService surfaceTypeService;
    public final CourtRepository courtRepository;

    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    public Court getCourtById(UUID id) {
        return courtRepository.findById(id).orElseThrow(() -> new CourtNotFoundException(id));
    }

    @Transactional
    public Court createCourt(CourtCreate courtCreate) {
        val surfaceType = surfaceTypeService.getSurfaceType(courtCreate.surfaceTypeId());
        val court = new Court(surfaceType, courtCreate.name());
        court.setDescription(courtCreate.description());

        courtRepository.save(court);
        return court;
    }

    // TODO: merge courtupdate and id together
    @Transactional
    public Court updateCourt(UUID id, CourtUpdate courtUpdate) {
        val surfaceType = surfaceTypeService.getSurfaceType(courtUpdate.surfaceTypeId());
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
