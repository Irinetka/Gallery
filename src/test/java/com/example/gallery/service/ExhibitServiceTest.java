package com.example.gallery.service;

import com.example.gallery.models.Author;
import com.example.gallery.models.Category;
import com.example.gallery.models.Exhibit;
import com.example.gallery.repository.AuthorRepository;
import com.example.gallery.repository.CategoryRepository;
import com.example.gallery.repository.ExhibitRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тесты для ExhibitService.
 * Использует @SpringBootTest + Mockito (без подключения к БД).
 */
@SpringBootTest
class ExhibitServiceTest {

    @Mock
    private ExhibitRepository exhibitRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MultiThreadService multiThreadService;

    @InjectMocks
    private ExhibitService exhibitService;

    @Test
    void testCreateExhibit() {
        Author author = new Author(1L, "Aivazovsky Ivan");
        Category category = new Category(1L, "Painting");
        Exhibit exhibit = new Exhibit(1L, "The Ninth Wave", "INV-001", 1850,
                "221x332", "Oil on canvas", "Painting", "Hall 1", author, category);

        when(exhibitRepository.findById(1L)).thenReturn(Optional.empty());
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(exhibitRepository.save(any())).thenReturn(exhibit);

        Exhibit created = exhibitService.createExhibit(exhibit);

        assertNotNull(created);
        assertEquals("The Ninth Wave", created.getTitle());
    }

    @Test
    void testSearchExhibitsWithMultithreading() {
        Author author = new Author(1L, "Aivazovsky Ivan");
        Category category = new Category(1L, "Painting");

        Exhibit e1 = new Exhibit(1L, "The Ninth Wave", "INV-001", 1850,
                "221x332", "Oil", "Painting", "Hall 1", author, category);
        Exhibit e2 = new Exhibit(2L, "Black Sea", "INV-002", 1881,
                "150x200", "Oil", "Painting", "Hall 2", author, category);
        Exhibit e3 = new Exhibit(3L, "Bogatyrs", "INV-003", 1898,
                "295x446", "Oil", "Painting", "Hall 3", author, category);

        when(exhibitRepository.findAll()).thenReturn(List.of(e1, e2, e3));
        when(multiThreadService.searchExhibitsInParallel(anyList(), eq("Ninth")))
                .thenReturn(List.of(e1));

        List<Exhibit> result = exhibitService.searchExhibitsWithMultithreading("Ninth");

        assertEquals(1, result.size());
        assertEquals("The Ninth Wave", result.get(0).getTitle());
    }

    @Test
    void testSearchExhibitsEmptyResult() {
        Author author = new Author(1L, "Aivazovsky Ivan");
        Category category = new Category(1L, "Painting");

        when(exhibitRepository.findAll()).thenReturn(List.of(
                new Exhibit(1L, "The Ninth Wave", "INV-001", 1850,
                        "221x332", "Oil", "Painting", "Hall 1", author, category)
        ));
        when(multiThreadService.searchExhibitsInParallel(anyList(), eq("NonExistent")))
                .thenReturn(List.of());

        List<Exhibit> result = exhibitService.searchExhibitsWithMultithreading("NonExistent");

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchExhibitsEmptyQuery() {
        assertThrows(IllegalArgumentException.class, () ->
                exhibitService.searchExhibitsWithMultithreading(""));
    }

    @Test
    void testFilterByAuthor() {
        Author author = new Author(1L, "Aivazovsky Ivan");
        Category category = new Category(1L, "Painting");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(exhibitRepository.findByAuthorId(1L)).thenReturn(List.of(
                new Exhibit(1L, "Painting 1", "INV-001", 1850,
                        "100x100", "Oil", "Painting", "Hall 1", author, category),
                new Exhibit(2L, "Painting 2", "INV-002", 1860,
                        "100x100", "Oil", "Painting", "Hall 2", author, category)
        ));

        List<Exhibit> result = exhibitService.showExhibitsByAuthor(1L);

        assertEquals(2, result.size());
    }

    @Test
    void testFilterByYearRange() {
        Author author = new Author(1L, "Aivazovsky Ivan");
        Category category = new Category(1L, "Painting");

        when(exhibitRepository.findByYearBetween(1840, 1860)).thenReturn(List.of(
                new Exhibit(1L, "Painting 1850", "INV-001", 1850,
                        "100x100", "Oil", "Painting", "Hall 1", author, category)
        ));

        List<Exhibit> result = exhibitService.showExhibitsByYearRange(1840, 1860);

        assertEquals(1, result.size());
    }

    @Test
    void testDeleteExhibit() {
        Author author = new Author(1L, "Aivazovsky Ivan");
        Category category = new Category(1L, "Painting");

        when(exhibitRepository.findById(1L)).thenReturn(Optional.of(
                new Exhibit(1L, "Test", "INV-001", 1850,
                        "100x100", "Oil", "Painting", "Hall 1", author, category)
        ));

        exhibitService.deleteExhibit(1L);

        verify(exhibitRepository, times(1)).deleteById(1L);  // ✅ Проверяем вызов метода
    }
}