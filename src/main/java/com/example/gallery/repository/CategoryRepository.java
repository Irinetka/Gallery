package com.example.gallery.repository;

import com.example.gallery.models.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category create(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAll();
    void delete(Long id);
}