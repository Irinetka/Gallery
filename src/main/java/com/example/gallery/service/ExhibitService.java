package com.example.gallery.service;

import com.example.gallery.exception.BusinessRuleException;
import com.example.gallery.exception.DuplicateEntityException;
import com.example.gallery.exception.EntityNotFoundException;
import com.example.gallery.exception.ValidationException;
import com.example.gallery.models.Author;
import com.example.gallery.models.Category;
import com.example.gallery.models.Exhibit;
import com.example.gallery.repository.AuthorRepository;
import com.example.gallery.repository.CategoryRepository;
import com.example.gallery.repository.ExhibitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExhibitService {
    private final ExhibitRepository exhibitRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final MultiThreadService multiThreadService;

    @Transactional
    public Exhibit createExhibit(Exhibit exhibit) {
        validateExhibit(exhibit);
        if (exhibitRepository.findById(exhibit.getId()).isPresent())
            throw new DuplicateEntityException("Exhibit with id " + exhibit.getId() + " already exists",
                    "Choose a free ID or use PUT to update the existing exhibit");
        Author author = authorRepository.findById(exhibit.getAuthor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found",
                        "Create the author first or pass an existing authorId"));
        Category category = categoryRepository.findById(exhibit.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found",
                        "Create the category first or pass an existing categoryId"));
        exhibit.setAuthor(author);
        exhibit.setCategory(category);
        return exhibitRepository.save(exhibit);
    }

    public Optional<Exhibit> getExhibit(Long id) {
        validateId(id);
        return exhibitRepository.findById(id);
    }

    public List<Exhibit> showAllExhibits() { return exhibitRepository.findAll(); }

    public List<Exhibit> showExhibitsByAuthor(Long authorId) {
        validateId(authorId);
        if (authorRepository.findById(authorId).isEmpty())
            throw new EntityNotFoundException("Author not found",
                    "List available authors via GET /api/authors and pick a valid ID");
        return exhibitRepository.findByAuthorId(authorId);
    }

    public List<Exhibit> showExhibitsByCategory(Long categoryId) {
        validateId(categoryId);
        if (categoryRepository.findById(categoryId).isEmpty())
            throw new EntityNotFoundException("Category not found",
                    "List available categories via GET /api/categories and pick a valid ID");
        return exhibitRepository.findByCategoryId(categoryId);
    }

    public List<Exhibit> showExhibitsByYearRange(Integer startYear, Integer endYear) {
        if (startYear == null || endYear == null || startYear > endYear)
            throw new BusinessRuleException("Invalid year range",
                    "Provide both years and ensure startYear <= endYear, e.g. 1800-1900");
        return exhibitRepository.findByYearBetween(startYear, endYear);
    }

    public List<Exhibit> search(String title, Long authorId, Long categoryId,
                                Integer startYear, Integer endYear) {
        return exhibitRepository.findAll().stream()
                .filter(e -> title == null || e.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(e -> authorId == null || e.getAuthor().getId().equals(authorId))
                .filter(e -> categoryId == null || e.getCategory().getId().equals(categoryId))
                .filter(e -> startYear == null || e.getYear() >= startYear)
                .filter(e -> endYear == null || e.getYear() <= endYear)
                .collect(Collectors.toList());
    }

    public List<Exhibit> searchExhibitsWithMultithreading(String query) {
        if (query == null || query.isBlank())
            throw new ValidationException("Search query cannot be empty",
                    "Enter a non-blank search string, e.g. 'wave'");
        List<Exhibit> all = exhibitRepository.findAll();
        return multiThreadService.searchExhibitsInParallel(all, query);
    }

    @Transactional
    public Exhibit update(Long id, Exhibit exhibit) {
        validateId(id);
        Exhibit existing = exhibitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exhibit not found",
                        "Check the exhibit ID; it may have been deleted"));
        if (exhibit.getTitle() != null) existing.setTitle(exhibit.getTitle());
        if (exhibit.getInventoryNumber() != null) existing.setInventoryNumber(exhibit.getInventoryNumber());
        if (exhibit.getYear() != null) existing.setYear(exhibit.getYear());
        if (exhibit.getSize() != null) existing.setSize(exhibit.getSize());
        if (exhibit.getMaterial() != null) existing.setMaterial(exhibit.getMaterial());
        if (exhibit.getTechnique() != null) existing.setTechnique(exhibit.getTechnique());
        if (exhibit.getLocation() != null) existing.setLocation(exhibit.getLocation());
        return exhibitRepository.save(existing);
    }

    @Transactional
    public void deleteExhibit(Long id) {
        validateId(id);
        if (exhibitRepository.findById(id).isEmpty())
            throw new EntityNotFoundException("Exhibit not found",
                    "Nothing to delete: exhibit with ID " + id + " does not exist");
        exhibitRepository.deleteById(id);
    }

    private void validateExhibit(Exhibit exhibit) {
        if (exhibit == null) throw new ValidationException("Exhibit cannot be null",
                "Provide a complete exhibit object");
        validateId(exhibit.getId());
        if (exhibit.getTitle() == null || exhibit.getTitle().isBlank())
            throw new ValidationException("Exhibit title cannot be empty", "Fill the 'title' field");
        if (exhibit.getAuthor() == null || exhibit.getAuthor().getId() == null)
            throw new ValidationException("Exhibit author cannot be null",
                    "Set 'author.id' to an existing author");
        if (exhibit.getCategory() == null || exhibit.getCategory().getId() == null)
            throw new ValidationException("Exhibit category cannot be null",
                    "Set 'category.id' to an existing category");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new ValidationException("Id must be positive",
                "ID must be a positive number");
    }
}