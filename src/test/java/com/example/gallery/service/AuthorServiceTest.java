package com.example.gallery.service;

import com.example.gallery.models.Author;
import com.example.gallery.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тесты для AuthorService.
 * Использует @SpringBootTest + Mockito (без подключения к БД).
 */
@SpringBootTest
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void testCreateAuthorSuccess() {
        Author author = new Author(1L, "Aivazovsky Ivan");
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        when(authorRepository.save(author)).thenReturn(author);

        Author created = authorService.createAuthor(author);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("Aivazovsky Ivan", created.getFullName());
    }

    @Test
    void testCreateAuthorDuplicateId() {
        authorService.createAuthor(new Author(1L, "Aivazovsky Ivan"));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author(1L, "Aivazovsky Ivan")));

        Author duplicate = new Author(1L, "Vasnetsov Viktor");

        assertThrows(IllegalStateException.class, () -> authorService.createAuthor(duplicate));
    }

    @Test
    void testCreateAuthorNull() {
        assertThrows(IllegalArgumentException.class, () -> authorService.createAuthor(null));
    }

    @Test
    void testCreateAuthorInvalidId() {
        Author author = new Author(-1L, "Aivazovsky Ivan");

        assertThrows(IllegalArgumentException.class, () -> authorService.createAuthor(author));
    }

    @Test
    void testCreateAuthorEmptyFullName() {
        Author author = new Author(1L, "");

        assertThrows(IllegalArgumentException.class, () -> authorService.createAuthor(author));
    }

    @Test
    void testCreateAuthorBlankFullName() {
        Author author = new Author(1L, "   ");

        assertThrows(IllegalArgumentException.class, () -> authorService.createAuthor(author));
    }

    @Test
    void testGetAuthorSuccess() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author(1L, "Aivazovsky Ivan")));

        Author found = authorService.getAuthor(1L).orElse(null);

        assertNotNull(found);
        assertEquals("Aivazovsky Ivan", found.getFullName());
    }

    @Test
    void testGetAuthorNotFound() {
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        Author found = authorService.getAuthor(999L).orElse(null);

        assertNull(found);
    }

    @Test
    void testGetAuthorInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> authorService.getAuthor(-1L));
    }

    @Test
    void testShowAuthors() {
        when(authorRepository.findAll()).thenReturn(List.of(
                new Author(1L, "Aivazovsky Ivan"),
                new Author(2L, "Vasnetsov Viktor"),
                new Author(3L, "Repin Ilya")
        ));

        List<Author> authors = authorService.showAuthors();

        assertEquals(3, authors.size());
    }

    @Test
    void testShowAuthorsEmpty() {
        when(authorRepository.findAll()).thenReturn(List.of());

        List<Author> authors = authorService.showAuthors();

        assertTrue(authors.isEmpty());
    }

    @Test
    void testDeleteAuthorSuccess() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author(1L, "Aivazovsky Ivan")));

        authorService.deleteAuthor(1L);

        // Проверяем, что deleteById был вызван ровно один раз
        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAuthorNotFound() {
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authorService.deleteAuthor(999L));
    }

    @Test
    void testDeleteAuthorInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> authorService.deleteAuthor(-1L));
    }
}