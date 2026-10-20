package com.example.gallery.repository;

import com.example.gallery.models.Exhibit;
import java.util.List;
import java.util.Optional;

public interface ExhibitRepository {
    Exhibit create(Exhibit exhibit);
    Optional<Exhibit> findById(Long id);
    List<Exhibit> findAll();
    List<Exhibit> findByAuthorId(Long authorId);
    List<Exhibit> findByCategoryId(Long categoryId);
    List<Exhibit> findByYearRange(Integer startYear, Integer endYear);
    void delete(Long id);
}