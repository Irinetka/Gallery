package com.example.gallery.repository;

import com.example.gallery.models.Author;
import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Author create(Author author);
    Optional<Author> findById(Long id);
    List<Author> findAll();
    void delete(Long id);
}