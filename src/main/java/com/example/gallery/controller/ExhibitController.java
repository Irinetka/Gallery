package com.example.gallery.controller;

import com.example.gallery.exception.EntityNotFoundException;
import com.example.gallery.models.Exhibit;
import com.example.gallery.service.ExhibitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exhibits")
@RequiredArgsConstructor
public class ExhibitController {
    private final ExhibitService exhibitService;

    @GetMapping
    public List<Exhibit> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear) {
        return exhibitService.search(title, authorId, categoryId, startYear, endYear);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exhibit> getById(@PathVariable Long id) {
        //   return exhibitService.getExhibit(id).map(ResponseEntity::ok)
        //        .orElse(ResponseEntity.notFound().build());

        return exhibitService.getExhibit(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Exhibit not found",
                        "Check the exhibit ID; it may have been deleted"));
    }

    @PostMapping
    public ResponseEntity<Exhibit> create(@RequestBody Exhibit exhibit) {
        return ResponseEntity.ok(exhibitService.createExhibit(exhibit));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exhibit> update(@PathVariable Long id, @RequestBody Exhibit exhibit) {
        return ResponseEntity.ok(exhibitService.update(id, exhibit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exhibitService.deleteExhibit(id);
        return ResponseEntity.noContent().build();
    }
}