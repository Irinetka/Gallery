package com.example.gallery.repository;

import com.example.gallery.models.Exhibit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryExhibitRepository implements ExhibitRepository {
    private final Map<Long, Exhibit> storage = new HashMap<>();

    @Override
    public Exhibit create(Exhibit exhibit) {
        storage.put(exhibit.getId(), exhibit);
        return exhibit;
    }

    @Override
    public Optional<Exhibit> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Exhibit> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Exhibit> findByAuthorId(Long authorId) {
        return storage.values().stream()
                .filter(e -> e.getAuthor() != null && e.getAuthor().getId().equals(authorId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Exhibit> findByCategoryId(Long categoryId) {
        return storage.values().stream()
                .filter(e -> e.getCategory() != null && e.getCategory().getId().equals(categoryId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Exhibit> findByYearRange(Integer startYear, Integer endYear) {
        return storage.values().stream()
                .filter(e -> e.getYear() != null && e.getYear() >= startYear && e.getYear() <= endYear)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}