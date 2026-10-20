package com.example.gallery.repository;

import com.example.gallery.models.Author;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryAuthorRepository implements AuthorRepository {
    private final Map<Long, Author> storage = new HashMap<>();

    @Override
    public Author create(Author author) {
        storage.put(author.getId(), author);
        return author;
    }

    @Override
    public Optional<Author> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Author> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}