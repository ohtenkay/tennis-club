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

    public SurfaceType getSurfaceType(UUID id) {
        return surfaceTypeRepository.findById(id)
                .orElseThrow(() -> new SurfaceTypeNotFoundException(id));
    }

    @Transactional
    public SurfaceType createSurfaceType(SurfaceTypeCreate surfaceTypeCreate) {
        val surfaceType = new SurfaceType(surfaceTypeCreate);

        surfaceTypeRepository.save(surfaceType);
        return surfaceType;
    }

    @Transactional
    public SurfaceType updateSurfaceType(SurfaceTypeUpdate surfaceTypeUpdate) {
        val surfaceType = getSurfaceType(surfaceTypeUpdate.id());
        surfaceType.setName(surfaceTypeUpdate.name());
        surfaceType.setPricePerMinute(surfaceTypeUpdate.pricePerMinute());

        surfaceTypeRepository.update(surfaceType);
        return surfaceType;
    }

    @Transactional
    public void deleteSurfaceType(UUID id) {
        val surfaceType = getSurfaceType(id);

        surfaceTypeRepository.delete(surfaceType);
    }
}
