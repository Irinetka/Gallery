package com.example.gallery.service;

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
            throw new IllegalStateException("Exhibit with id " + exhibit.getId() + " already exists");
        Author author = authorRepository.findById(exhibit.getAuthor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
        Category category = categoryRepository.findById(exhibit.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
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
            throw new IllegalArgumentException("Author not found");
        return exhibitRepository.findByAuthorId(authorId);
    }

    public List<Exhibit> showExhibitsByCategory(Long categoryId) {
        validateId(categoryId);
        if (categoryRepository.findById(categoryId).isEmpty())
            throw new IllegalArgumentException("Category not found");
        return exhibitRepository.findByCategoryId(categoryId);
    }

    public List<Exhibit> showExhibitsByYearRange(Integer startYear, Integer endYear) {
        if (startYear == null || endYear == null || startYear > endYear)
            throw new IllegalArgumentException("Invalid year range");
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
            throw new IllegalArgumentException("Search query cannot be empty");
        List<Exhibit> all = exhibitRepository.findAll();
        return multiThreadService.searchExhibitsInParallel(all, query);
    }

    @Transactional
    public Exhibit update(Long id, Exhibit exhibit) {
        validateId(id);
        Exhibit existing = exhibitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exhibit not found"));
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
            throw new IllegalArgumentException("Exhibit not found");
        exhibitRepository.deleteById(id);
    }

    private void validateExhibit(Exhibit exhibit) {
        if (exhibit == null) throw new IllegalArgumentException("Exhibit cannot be null");
        validateId(exhibit.getId());
        if (exhibit.getTitle() == null || exhibit.getTitle().isBlank())
            throw new IllegalArgumentException("Exhibit title cannot be empty");
        if (exhibit.getAuthor() == null || exhibit.getAuthor().getId() == null)
            throw new IllegalArgumentException("Exhibit author cannot be null");
        if (exhibit.getCategory() == null || exhibit.getCategory().getId() == null)
            throw new IllegalArgumentException("Exhibit category cannot be null");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Id must be positive");
    }
}