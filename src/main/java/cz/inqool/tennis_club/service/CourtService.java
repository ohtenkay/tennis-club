package cz.inqool.tennis_club.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import cz.inqool.tennis_club.exception.CourtNotFoundException;
import cz.inqool.tennis_club.exception.SurfaceTypeNotFoundException;
import cz.inqool.tennis_club.model.Court;
import cz.inqool.tennis_club.model.send.CourtSend;
import cz.inqool.tennis_club.repository.CourtRepository;
import cz.inqool.tennis_club.repository.SurfaceTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class CourtService {

    // TODO: use service instead
    public final SurfaceTypeRepository surfaceTypeRepository;
    public final CourtRepository courtRepository;

    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    public Court getCourtById(UUID id) {
        return courtRepository.findById(id).orElseThrow(() -> new CourtNotFoundException(id));
    }

    @Transactional
    public Court createCourt(CourtSend courtSend) {
        val surfaceType = surfaceTypeRepository.findById(courtSend.surfaceTypeId())
                .orElseThrow(() -> new SurfaceTypeNotFoundException(courtSend.surfaceTypeId()));
        val court = new Court(surfaceType, courtSend.name());
        court.setDescription(courtSend.description());

        courtRepository.save(court);
        return court;
    }

    @Transactional
    public Court updateCourt(UUID id, CourtSend courtSend) {
        val surfaceType = surfaceTypeRepository.findById(courtSend.surfaceTypeId())
                .orElseThrow(() -> new SurfaceTypeNotFoundException(courtSend.surfaceTypeId()));
        val court = getCourtById(id);
        court.setSurfaceType(surfaceType);
        court.setName(courtSend.name());
        court.setDescription(courtSend.description());

        courtRepository.update(court);
        return court;
    }

    @Transactional
    public void deleteCourt(UUID id) {
        val court = getCourtById(id);

        courtRepository.delete(court);
    }
}
