package com.example.gallery.repository;

import com.example.gallery.models.Exhibition;
import com.example.gallery.models.ExhibitionType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryExhibitionRepository implements ExhibitionRepository {
    private final Map<Long, Exhibition> storage = new HashMap<>();

    @Override
    public Exhibition create(Exhibition exhibition) {
        storage.put(exhibition.getId(), exhibition);
        return exhibition;
    }

    @Override
    public Optional<Exhibition> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Exhibition> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Exhibition> findByType(ExhibitionType type) {
        return storage.values().stream()
                .filter(e -> e.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}