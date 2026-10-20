package com.example.gallery.service;

import com.example.gallery.models.Author;
import com.example.gallery.repository.AuthorRepository;
import java.util.List;
import java.util.Optional;

public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author createAuthor(Author author) {
        validateAuthor(author);
        if (authorRepository.findById(author.getId()).isPresent())
            throw new IllegalStateException("Author with id " + author.getId() + " already exists");
        return authorRepository.create(author);
    }

    public Optional<Author> getAuthor(Long id) {
        validateId(id);
        return authorRepository.findById(id);
    }

    public List<Author> showAuthors() { return authorRepository.findAll(); }

    public void deleteAuthor(Long id) {
        validateId(id);
        if (authorRepository.findById(id).isEmpty())
            throw new IllegalArgumentException("Author with id " + id + " not found");
        authorRepository.delete(id);
    }

    private void validateAuthor(Author author) {
        if (author == null) throw new IllegalArgumentException("Author cannot be null");
        validateId(author.getId());
        if (author.getFullName() == null || author.getFullName().isBlank())
            throw new IllegalArgumentException("Author full name cannot be empty");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Id must be a positive number");
    }
}