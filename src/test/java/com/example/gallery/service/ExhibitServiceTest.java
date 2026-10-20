package com.example.gallery.service;

import com.example.gallery.models.Author;
import com.example.gallery.models.Category;
import com.example.gallery.models.Exhibit;
import com.example.gallery.repository.AuthorRepository;
import com.example.gallery.repository.CategoryRepository;
import com.example.gallery.repository.ExhibitRepository;
import com.example.gallery.repository.InMemoryAuthorRepository;
import com.example.gallery.repository.InMemoryCategoryRepository;
import com.example.gallery.repository.InMemoryExhibitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class ExhibitServiceTest {
    private ExhibitService exhibitService;
    private AuthorService authorService;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        AuthorRepository authorRepo = new InMemoryAuthorRepository();
        CategoryRepository categoryRepo = new InMemoryCategoryRepository();
        ExhibitRepository exhibitRepo = new InMemoryExhibitRepository();

        authorService = new AuthorService(authorRepo);
        categoryService = new CategoryService(categoryRepo);
        exhibitService = new ExhibitService(exhibitRepo, authorRepo, categoryRepo);

        authorService.createAuthor(new Author(1L, "Aivazovsky Ivan"));
        categoryService.createCategory(new Category(1L, "Painting"));
    }

    @Test
    void testCreateExhibit() {
        Author author = authorService.getAuthor(1L).orElseThrow();
        Category category = categoryService.getCategory(1L).orElseThrow();
        Exhibit exhibit = new Exhibit(1L, "The Ninth Wave", "INV-001", 1850,
                "221x332", "Oil on canvas", "Painting", "Hall 1", author, category);

        Exhibit created = exhibitService.createExhibit(exhibit);

        assertNotNull(created);
        assertEquals("The Ninth Wave", created.getTitle());
    }

    @Test
    void testSearchExhibits() {
        Author author = authorService.getAuthor(1L).orElseThrow();
        Category category = categoryService.getCategory(1L).orElseThrow();

        exhibitService.createExhibit(new Exhibit(1L, "The Ninth Wave", "INV-001", 1850, "221x332", "Oil", "Painting", "Hall 1", author, category));
        exhibitService.createExhibit(new Exhibit(2L, "Black Sea", "INV-002", 1881, "150x200", "Oil", "Painting", "Hall 2", author, category));

        List<Exhibit> result = exhibitService.searchExhibits("Ninth");
        assertEquals(1, result.size());
        assertEquals("The Ninth Wave", result.get(0).getTitle());
    }

    @Test
    void testFilterByYearRange() {
        Author author = authorService.getAuthor(1L).orElseThrow();
        Category category = categoryService.getCategory(1L).orElseThrow();

        exhibitService.createExhibit(new Exhibit(1L, "Painting 1850", "INV-001", 1850, "100x100", "Oil", "Painting", "Hall 1", author, category));
        exhibitService.createExhibit(new Exhibit(2L, "Painting 1900", "INV-002", 1900, "100x100", "Oil", "Painting", "Hall 2", author, category));

        List<Exhibit> result = exhibitService.showExhibitsByYearRange(1840, 1860);
        assertEquals(1, result.size());
        assertEquals(1850, result.get(0).getYear());
    }

    @Test
    void testDeleteExhibit() {
        Author author = authorService.getAuthor(1L).orElseThrow();
        Category category = categoryService.getCategory(1L).orElseThrow();

        exhibitService.createExhibit(new Exhibit(1L, "Test", "INV-001", 1850, "100x100", "Oil", "Painting", "Hall 1", author, category));

        exhibitService.deleteExhibit(1L);
        assertTrue(exhibitService.getExhibit(1L).isEmpty());
    }
}