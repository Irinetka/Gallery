package com.example.gallery.repository;

import com.example.gallery.models.Exhibition;
import com.example.gallery.models.ExhibitionType;
import java.util.List;
import java.util.Optional;

public interface ExhibitionRepository {
    Exhibition create(Exhibition exhibition);
    Optional<Exhibition> findById(Long id);
    List<Exhibition> findAll();
    List<Exhibition> findByType(ExhibitionType type);
    void delete(Long id);
}