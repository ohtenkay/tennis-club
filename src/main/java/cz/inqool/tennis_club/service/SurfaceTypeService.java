package cz.inqool.tennis_club.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import cz.inqool.tennis_club.exception.SurfaceTypeNotFoundException;
import cz.inqool.tennis_club.model.SurfaceType;
import cz.inqool.tennis_club.model.create.SurfaceTypeCreate;
import cz.inqool.tennis_club.model.update.SurfaceTypeUpdate;
import cz.inqool.tennis_club.repository.SurfaceTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class SurfaceTypeService {

    public final SurfaceTypeRepository surfaceTypeRepository;

    /**
     * Get surface type by id
     *
     * @param id surface type id
     * @return surface type
     * @throws SurfaceTypeNotFoundException if surface type does not exist
     */
    public SurfaceType getSurfaceTypeById(UUID id) {
        return surfaceTypeRepository.findById(id)
                .orElseThrow(() -> new SurfaceTypeNotFoundException(id));
    }

    /**
     * Create new surface type
     *
     * @param surfaceTypeCreate surface type data
     * @return created surface type
     */
    @Transactional
    public SurfaceType createSurfaceType(SurfaceTypeCreate surfaceTypeCreate) {
        val surfaceType = new SurfaceType(surfaceTypeCreate);

        surfaceTypeRepository.save(surfaceType);
        return surfaceType;
    }

    /**
     * Update surface type
     *
     * @param surfaceTypeUpdate surface type data
     * @return updated surface type
     * @throws SurfaceTypeNotFoundException if surface type does not exist
     */
    @Transactional
    public SurfaceType updateSurfaceType(SurfaceTypeUpdate surfaceTypeUpdate) {
        val surfaceType = getSurfaceTypeById(surfaceTypeUpdate.id());
        surfaceType.setName(surfaceTypeUpdate.name());
        surfaceType.setPricePerMinute(surfaceTypeUpdate.pricePerMinute());

        surfaceTypeRepository.update(surfaceType);
        return surfaceType;
    }

    /**
     * Delete surface type
     *
     * @param id surface type id
     * @throws SurfaceTypeNotFoundException if surface type does not exist
     */
    @Transactional
    public void deleteSurfaceType(UUID id) {
        val surfaceType = getSurfaceTypeById(id);

        surfaceTypeRepository.delete(surfaceType);
    }

}
