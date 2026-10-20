package com.example.gallery.service;

import com.example.gallery.models.Exhibit;
import com.example.gallery.models.Exhibition;
import com.example.gallery.models.ExhibitionType;
import com.example.gallery.repository.ExhibitRepository;
import com.example.gallery.repository.ExhibitionRepository;
import java.util.List;
import java.util.Optional;

public class ExhibitionService {
    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitRepository exhibitRepository;

    public ExhibitionService(ExhibitionRepository exhibitionRepository,
                             ExhibitRepository exhibitRepository) {
        this.exhibitionRepository = exhibitionRepository;
        this.exhibitRepository = exhibitRepository;
    }

    public Exhibition createExhibition(Exhibition exhibition) {
        validateExhibition(exhibition);
        if (exhibitionRepository.findById(exhibition.getId()).isPresent())
            throw new IllegalStateException("Exhibition with id " + exhibition.getId() + " already exists");
        return exhibitionRepository.create(exhibition);
    }

    public Optional<Exhibition> getExhibition(Long id) {
        validateId(id);
        return exhibitionRepository.findById(id);
    }

    public List<Exhibition> showAllExhibitions() { return exhibitionRepository.findAll(); }

    public List<Exhibition> showExhibitionsByType(ExhibitionType type) {
        return exhibitionRepository.findByType(type);
    }

    public void addExhibitToExhibition(Long exhibitionId, Long exhibitId) {
        Exhibition exhibition = getExistingExhibition(exhibitionId);
        Exhibit exhibit = exhibitRepository.findById(exhibitId)
                .orElseThrow(() -> new IllegalArgumentException("Exhibit not found"));
        exhibition.addExhibit(exhibit);
    }

    public void addTag(Long exhibitionId, String tag) {
        Exhibition exhibition = getExistingExhibition(exhibitionId);
        if (tag == null || tag.isBlank())
            throw new IllegalArgumentException("Tag cannot be empty");
        exhibition.addTag(tag);
    }

    public void removeTag(Long exhibitionId, String tag) {
        Exhibition exhibition = getExistingExhibition(exhibitionId);
        if (tag == null || tag.isBlank())
            throw new IllegalArgumentException("Tag cannot be empty");
        exhibition.removeTag(tag);
    }

    public void deleteExhibition(Long id) {
        validateId(id);
        if (exhibitionRepository.findById(id).isEmpty())
            throw new IllegalArgumentException("Exhibition with id " + id + " not found");
        exhibitionRepository.delete(id);
    }

    private Exhibition getExistingExhibition(Long id) {
        validateId(id);
        return exhibitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exhibition with id " + id + " not found"));
    }

    private void validateExhibition(Exhibition exhibition) {
        if (exhibition == null) throw new IllegalArgumentException("Exhibition cannot be null");
        validateId(exhibition.getId());
        if (exhibition.getTitle() == null || exhibition.getTitle().isBlank())
            throw new IllegalArgumentException("Exhibition title cannot be empty");
        if (exhibition.getType() == null)
            throw new IllegalArgumentException("Exhibition type cannot be null");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Id must be a positive number");
    }
}