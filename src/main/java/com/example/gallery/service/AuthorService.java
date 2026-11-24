package com.example.gallery.service;

import com.example.gallery.exception.DuplicateEntityException;
import com.example.gallery.exception.EntityNotFoundException;
import com.example.gallery.exception.ValidationException;
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
            throw new DuplicateEntityException("Author with id " + author.getId() + " already exists",
                    "Use another ID or update the existing author via PUT /api/authors/" + author.getId());
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
                .orElseThrow(() -> new EntityNotFoundException("Author not found",
                        "Check the author ID; create the author first via POST /api/authors"));
        if (author.getFullName() != null) existing.setFullName(author.getFullName());
        return authorRepository.save(existing);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        validateId(id);
        if (authorRepository.findById(id).isEmpty())
            throw new EntityNotFoundException("Author not found",
                    "Nothing to delete: author with ID " + id + " does not exist");
        authorRepository.deleteById(id);
    }

    private void validateAuthor(Author author) {
        if (author == null) throw new ValidationException("Author cannot be null",
                "Provide a full author object in the request body");
        validateId(author.getId());
        if (author.getFullName() == null || author.getFullName().isBlank())
            throw new ValidationException("Author full name cannot be empty",
                    "Fill the 'fullName' field (non-blank string)");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new ValidationException("Id must be positive",
                "ID must be a positive number, e.g. 1, 2, 3...");
    }
}