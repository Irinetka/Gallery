package com.example.gallery.service;

import com.example.gallery.models.Category;
import com.example.gallery.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category createCategory(Category category) {
        validateCategory(category);
        if (categoryRepository.findById(category.getId()).isPresent())
            throw new IllegalStateException("Category with id " + category.getId() + " already exists");
        return categoryRepository.save(category);
    }

    public Optional<Category> getCategory(Long id) {
        validateId(id);
        return categoryRepository.findById(id);
    }

    public List<Category> showCategories() { return categoryRepository.findAll(); }

    @Transactional
    public Category updateCategory(Long id, Category category) {
        validateId(id);
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        if (category.getName() != null) existing.setName(category.getName());
        return categoryRepository.save(existing);
    }

    @Transactional
    public void deleteCategory(Long id) {
        validateId(id);
        if (categoryRepository.findById(id).isEmpty())
            throw new IllegalArgumentException("Category not found");
        categoryRepository.deleteById(id);
    }

    private void validateCategory(Category category) {
        if (category == null) throw new IllegalArgumentException("Category cannot be null");
        validateId(category.getId());
        if (category.getName() == null || category.getName().isBlank())
            throw new IllegalArgumentException("Category name cannot be empty");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Id must be positive");
    }
}