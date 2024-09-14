package cz.inqool.tennis_club.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.inqool.tennis_club.model.Court;
import cz.inqool.tennis_club.model.create.CourtCreate;
import cz.inqool.tennis_club.service.CourtService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/courts")
@RequiredArgsConstructor
public class CourtController {

    private final CourtService courtService;

    @GetMapping
    public List<Court> getAllCourts() {
        return courtService.getAllCourts();
    }

    @GetMapping("/{id}")
    public Court getCourtById(@PathVariable UUID id) {
        return courtService.getCourtById(id);
    }

    @PostMapping
    public Court createCourt(@RequestBody CourtCreate courtCreate) {
        return courtService.createCourt(courtCreate);
    }

    @PutMapping("/{id}")
    public Court updateCourt(@PathVariable UUID id, @RequestBody Court court) {
        return courtService.updateCourt(id, court);
    }

    @DeleteMapping("/{id}")
    public void deleteCourt(@PathVariable UUID id) {
        courtService.deleteCourt(id);
    }
}
