package com.example.gallery.service;

import com.example.gallery.exception.DuplicateEntityException;
import com.example.gallery.exception.EntityNotFoundException;
import com.example.gallery.exception.ValidationException;
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
            throw new DuplicateEntityException("Exhibition with id " + exhibition.getId() + " already exists",
                    "Use a free ID or update the existing exhibition");
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
                .orElseThrow(() -> new EntityNotFoundException("Exhibition not found",
                        "Check the exhibition ID before adding a tag"));
        ex.addTag(tag);
        exhibitionRepository.save(ex);
    }

    @Transactional
    public void removeTag(Long id, String tag) {
        Exhibition ex = exhibitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exhibition not found",
                        "Check the exhibition ID before removing a tag"));
        ex.removeTag(tag);
        exhibitionRepository.save(ex);
    }

    @Transactional
    public Exhibition update(Long id, Exhibition exhibition) {
        validateId(id);
        Exhibition existing = exhibitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exhibition not found",
                        "Check the exhibition ID; it may have been deleted"));
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
            throw new EntityNotFoundException("Exhibition not found",
                    "Nothing to delete: exhibition with ID " + id + " does not exist");
        exhibitionRepository.deleteById(id);
    }

    private void validateExhibition(Exhibition exhibition) {
        if (exhibition == null) throw new ValidationException("Exhibition cannot be null",
                "Provide a complete exhibition object");
        validateId(exhibition.getId());
        if (exhibition.getTitle() == null || exhibition.getTitle().isBlank())
            throw new ValidationException("Exhibition title cannot be empty", "Fill the 'title' field");
        if (exhibition.getType() == null)
            throw new ValidationException("Exhibition type cannot be null",
                    "Set 'type' to PERMANENT or TEMPORARY");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new ValidationException("Id must be positive",
                "ID must be a positive number");
    }
}