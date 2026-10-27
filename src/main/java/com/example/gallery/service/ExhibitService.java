package com.example.gallery.service;

import com.example.gallery.models.Author;
import com.example.gallery.models.Category;
import com.example.gallery.models.Exhibit;
import com.example.gallery.repository.AuthorRepository;
import com.example.gallery.repository.CategoryRepository;
import com.example.gallery.repository.ExhibitRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExhibitService {
    private final ExhibitRepository exhibitRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final MultiThreadService multiThreadService;

    public ExhibitService(ExhibitRepository exhibitRepository,
                          AuthorRepository authorRepository,
                          CategoryRepository categoryRepository,
                          MultiThreadService multiThreadService) {
        this.exhibitRepository = exhibitRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.multiThreadService = multiThreadService;
    }

    public Exhibit createExhibit(Exhibit exhibit) {
        validateExhibit(exhibit);
        if (exhibitRepository.findById(exhibit.getId()).isPresent()) {
            throw new IllegalStateException("Exhibit with id " + exhibit.getId() + " already exists");
        }

        Author author = authorRepository.findById(exhibit.getAuthor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
        Category category = categoryRepository.findById(exhibit.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Exhibit created = exhibitRepository.create(exhibit);
        author.addExhibit(created);
        category.addExhibit(created);
        return created;
    }

    public Optional<Exhibit> getExhibit(Long id) {
        validateId(id);
        return exhibitRepository.findById(id);
    }

    public List<Exhibit> showAllExhibits() {
        return exhibitRepository.findAll();
    }

    public List<Exhibit> showExhibitsByAuthor(Long authorId) {
        validateId(authorId);
        if (authorRepository.findById(authorId).isEmpty()) {
            throw new IllegalArgumentException("Author not found");
        }
        return exhibitRepository.findByAuthorId(authorId);
    }

    public List<Exhibit> showExhibitsByCategory(Long categoryId) {
        validateId(categoryId);
        if (categoryRepository.findById(categoryId).isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }
        return exhibitRepository.findByCategoryId(categoryId);
    }

    public List<Exhibit> showExhibitsByYearRange(Integer startYear, Integer endYear) {
        if (startYear == null || endYear == null || startYear > endYear) {
            throw new IllegalArgumentException("Invalid year range");
        }
        return exhibitRepository.findByYearRange(startYear, endYear);
    }

    public void deleteExhibit(Long id) {
        validateId(id);
        Exhibit exhibit = exhibitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exhibit with id " + id + " not found"));
        if (exhibit.getAuthor() != null) {
            exhibit.getAuthor().getExhibits().remove(exhibit);
        }
        if (exhibit.getCategory() != null) {
            exhibit.getCategory().getExhibits().remove(exhibit);
        }
        exhibitRepository.delete(id);
    }

    /**
     * Поиск экспонатов с многопоточной обработкой результатов.
     * Сначала фильтрует по названию, затем обрабатывает найденные экспонаты
     * в нескольких потоках (загрузка метаданных, анализ автора и т.д.).
     */
    public List<Exhibit> searchExhibitsWithMultithreading(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }

        String normalizedQuery = query.toLowerCase();
        List<Exhibit> allExhibits = exhibitRepository.findAll();

        List<Exhibit> filtered = new ArrayList<>();
        for (Exhibit exhibit : allExhibits) {
            if (exhibit.getTitle() != null &&
                    exhibit.getTitle().toLowerCase().contains(normalizedQuery)) {
                filtered.add(exhibit);
            }
        }

        if (!filtered.isEmpty()) {
            System.out.println();
            System.out.println("Processing " + filtered.size() + " found exhibit(s) in parallel...");
            multiThreadService.processExhibitsInParallel(filtered);
        } else {
            System.out.println("No exhibits found for query: " + query);
        }

        return filtered;
    }

    private void validateExhibit(Exhibit exhibit) {
        if (exhibit == null) {
            throw new IllegalArgumentException("Exhibit cannot be null");
        }
        validateId(exhibit.getId());
        if (exhibit.getTitle() == null || exhibit.getTitle().isBlank()) {
            throw new IllegalArgumentException("Exhibit title cannot be empty");
        }
        if (exhibit.getAuthor() == null || exhibit.getAuthor().getId() == null) {
            throw new IllegalArgumentException("Exhibit author cannot be null");
        }
        if (exhibit.getCategory() == null || exhibit.getCategory().getId() == null) {
            throw new IllegalArgumentException("Exhibit category cannot be null");
        }
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id must be a positive number");
        }
    }
}