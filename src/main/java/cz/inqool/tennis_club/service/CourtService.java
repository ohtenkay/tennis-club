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

    /**
     * Check if court exists
     *
     * @param id
     * @return true if court exists
     */
    public boolean courtExists(UUID id) {
        return courtRepository.existsById(id);
    }

    /**
     * Get all courts
     *
     * @return list of courts
     */
    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    /**
     * Get court by id
     *
     * @param id
     * @return court
     * @throws CourtNotFoundException if court does not exist
     */
    public Court getCourtById(UUID id) {
        return courtRepository.findById(id).orElseThrow(() -> new CourtNotFoundException(id));
    }

    /**
     * Create a new court
     *
     * @param courtCreate
     * @return created court
     * @throws SurfaceTypeNotFoundException if surface type does not exist
     */
    @Transactional
    public Court createCourt(CourtCreate courtCreate) {
        val surfaceType = surfaceTypeService.getSurfaceTypeById(courtCreate.surfaceTypeId());
        val court = new Court(surfaceType, courtCreate.name());
        court.setDescription(courtCreate.description());

        courtRepository.save(court);
        return court;
    }

    /**
     * Update court
     *
     * @param courtUpdate
     * @return updated court
     * @throws SurfaceTypeNotFoundException if surface type does not exist
     * @throws CourtNotFoundException       if court does not exist
     */
    @Transactional
    public Court updateCourt(CourtUpdate courtUpdate) {
        val surfaceType = surfaceTypeService.getSurfaceTypeById(courtUpdate.surfaceTypeId());
        val court = getCourtById(courtUpdate.id());
        court.setSurfaceType(surfaceType);
        court.setName(courtUpdate.name());
        court.setDescription(courtUpdate.description());

        courtRepository.update(court);
        return court;
    }

    /**
     * Delete court
     *
     * @param id
     * @throws CourtNotFoundException if court does not exist
     */
    @Transactional
    public void deleteCourt(UUID id) {
        val court = getCourtById(id);

        courtRepository.delete(court);
    }

}
