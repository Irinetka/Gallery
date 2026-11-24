package com.example.gallery.controller;

import com.example.gallery.exception.EntityNotFoundException;
import com.example.gallery.models.Exhibition;
import com.example.gallery.models.ExhibitionType;
import com.example.gallery.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exhibitions") // <-- ИСПРАВЛЕНО: правильный путь
@RequiredArgsConstructor
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    @GetMapping
    public List<Exhibition> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) ExhibitionType type) {
        return exhibitionService.search(title, type);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exhibition> getById(@PathVariable Long id) {
         return exhibitionService.getExhibition(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Exhibition not found",
                        "Check the exhibition ID; it may have been deleted"));
    }

    @PostMapping
    public ResponseEntity<Exhibition> create(@RequestBody Exhibition exhibition) {
        return ResponseEntity.ok(exhibitionService.createExhibition(exhibition));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exhibition> update(@PathVariable Long id, @RequestBody Exhibition exhibition) {
        return ResponseEntity.ok(exhibitionService.update(id, exhibition));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exhibitionService.deleteExhibition(id);
        return ResponseEntity.noContent().build();
    }
}