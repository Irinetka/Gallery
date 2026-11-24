package com.example.gallery.service;

import com.example.gallery.models.Exhibition;
import com.example.gallery.models.ExhibitionType;
import com.example.gallery.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExhibitionService {
    private final ExhibitionRepository exhibitionRepository;

    @Transactional
    public Exhibition createExhibition(Exhibition exhibition) {
        validateExhibition(exhibition);
        if (exhibitionRepository.findById(exhibition.getId()).isPresent())
            throw new IllegalStateException("Exhibition with id " + exhibition.getId() + " already exists");
        return exhibitionRepository.save(exhibition);
    }

    public Optional<Exhibition> getExhibition(Long id) {
        validateId(id);
        return exhibitionRepository.findById(id);
    }

    public List<Exhibition> showAllExhibitions() { return exhibitionRepository.findAll(); }

    public List<Exhibition> showExhibitionsByType(ExhibitionType type) {
        return exhibitionRepository.findByType(type);
    }

    public List<Exhibition> search(String title, ExhibitionType type) {
        if (title != null && type != null)
            return exhibitionRepository.findByTitleContainingIgnoreCaseAndType(title, type);
        if (title != null)
            return exhibitionRepository.findByTitleContainingIgnoreCase(title);
        if (type != null)
            return exhibitionRepository.findByType(type);
        return exhibitionRepository.findAll();
    }

    @Transactional
    public void addTag(Long id, String tag) {
        Exhibition ex = exhibitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exhibition not found"));
        ex.addTag(tag);
        exhibitionRepository.save(ex);
    }

    @Transactional
    public void removeTag(Long id, String tag) {
        Exhibition ex = exhibitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exhibition not found"));
        ex.removeTag(tag);
        exhibitionRepository.save(ex);
    }

    @Transactional
    public Exhibition update(Long id, Exhibition exhibition) {
        validateId(id);
        Exhibition existing = exhibitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exhibition not found"));
        if (exhibition.getTitle() != null) existing.setTitle(exhibition.getTitle());
        if (exhibition.getType() != null) existing.setType(exhibition.getType());
        if (exhibition.getLocation() != null) existing.setLocation(exhibition.getLocation());
        if (exhibition.getStartDate() != null) existing.setStartDate(exhibition.getStartDate());
        if (exhibition.getEndDate() != null) existing.setEndDate(exhibition.getEndDate());
        return exhibitionRepository.save(existing);
    }

    @Transactional
    public void deleteExhibition(Long id) {
        validateId(id);
        if (exhibitionRepository.findById(id).isEmpty())
            throw new IllegalArgumentException("Exhibition not found");
        exhibitionRepository.deleteById(id);
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
        if (id == null || id <= 0) throw new IllegalArgumentException("Id must be positive");
    }
}