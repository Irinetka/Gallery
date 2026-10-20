package com.example.gallery.service;

import com.example.gallery.models.Author;
import com.example.gallery.repository.AuthorRepository;
import com.example.gallery.repository.InMemoryAuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthorServiceTest {
    private AuthorRepository authorRepository;
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorRepository = new InMemoryAuthorRepository();
        authorService = new AuthorService(authorRepository);
    }

    @Test
    void testCreateAuthorSuccess() {
        Author author = new Author(1L, "Aivazovsky Ivan");
        Author created = authorService.createAuthor(author);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("Aivazovsky Ivan", created.getFullName());
    }

    @Test
    void testCreateAuthorDuplicateId() {
        authorService.createAuthor(new Author(1L, "Aivazovsky Ivan"));

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
        authorService.createAuthor(new Author(1L, "Aivazovsky Ivan"));

        Author found = authorService.getAuthor(1L).orElse(null);

        assertNotNull(found);
        assertEquals("Aivazovsky Ivan", found.getFullName());
    }

    @Test
    void testGetAuthorNotFound() {
        Author found = authorService.getAuthor(999L).orElse(null);

        assertNull(found);
    }

    @Test
    void testGetAuthorInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> authorService.getAuthor(-1L));
    }

    @Test
    void testShowAuthors() {
        authorService.createAuthor(new Author(1L, "Aivazovsky Ivan"));
        authorService.createAuthor(new Author(2L, "Vasnetsov Viktor"));
        authorService.createAuthor(new Author(3L, "Repin Ilya"));

        List<Author> authors = authorService.showAuthors();

        assertEquals(3, authors.size());
    }

    @Test
    void testShowAuthorsEmpty() {
        List<Author> authors = authorService.showAuthors();

        assertTrue(authors.isEmpty());
    }

    @Test
    void testDeleteAuthorSuccess() {
        authorService.createAuthor(new Author(1L, "Aivazovsky Ivan"));

        authorService.deleteAuthor(1L);

        assertTrue(authorService.getAuthor(1L).isEmpty());
    }

    @Test
    void testDeleteAuthorNotFound() {
        assertThrows(IllegalArgumentException.class, () -> authorService.deleteAuthor(999L));
    }

    @Test
    void testDeleteAuthorInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> authorService.deleteAuthor(-1L));
    }
}