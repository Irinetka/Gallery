package com.example.gallery.service;

import com.example.gallery.models.Category;
import com.example.gallery.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        validateCategory(category);
        if (categoryRepository.findById(category.getId()).isPresent())
            throw new IllegalStateException("Category with id " + category.getId() + " already exists");
        return categoryRepository.create(category);
    }

    public Optional<Category> getCategory(Long id) {
        validateId(id);
        return categoryRepository.findById(id);
    }

    public List<Category> showCategories() { return categoryRepository.findAll(); }

    public void deleteCategory(Long id) {
        validateId(id);
        if (categoryRepository.findById(id).isEmpty())
            throw new IllegalArgumentException("Category with id " + id + " not found");
        categoryRepository.delete(id);
    }

    private void validateCategory(Category category) {
        if (category == null) throw new IllegalArgumentException("Category cannot be null");
        validateId(category.getId());
        if (category.getName() == null || category.getName().isBlank())
            throw new IllegalArgumentException("Category name cannot be empty");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Id must be a positive number");
    }
}