package com.example.gallery.service;

import com.example.gallery.models.Author;
import com.example.gallery.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional
    public Author createAuthor(Author author) {
        validateAuthor(author);
        if (authorRepository.findById(author.getId()).isPresent())
            throw new IllegalStateException("Author with id " + author.getId() + " already exists");
        return authorRepository.save(author);
    }

    public Optional<Author> getAuthor(Long id) {
        validateId(id);
        return authorRepository.findById(id);
    }

    public List<Author> showAuthors() { return authorRepository.findAll(); }

    @Transactional
    public Author updateAuthor(Long id, Author author) {
        validateId(id);
        Author existing = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
        if (author.getFullName() != null) existing.setFullName(author.getFullName());
        return authorRepository.save(existing);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        validateId(id);
        if (authorRepository.findById(id).isEmpty())
            throw new IllegalArgumentException("Author not found");
        authorRepository.deleteById(id);
    }

    private void validateAuthor(Author author) {
        if (author == null) throw new IllegalArgumentException("Author cannot be null");
        validateId(author.getId());
        if (author.getFullName() == null || author.getFullName().isBlank())
            throw new IllegalArgumentException("Author full name cannot be empty");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Id must be positive");
    }
}