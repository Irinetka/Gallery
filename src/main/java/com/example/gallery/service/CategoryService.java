package com.example.gallery.service;

import com.example.gallery.exception.DuplicateEntityException;
import com.example.gallery.exception.EntityNotFoundException;
import com.example.gallery.exception.ValidationException;
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
            throw new DuplicateEntityException("Category with id " + category.getId() + " already exists",
                    "Use another ID or update the existing category");
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
                .orElseThrow(() -> new EntityNotFoundException("Category not found",
                        "Check the category ID or create it first"));
        if (category.getName() != null) existing.setName(category.getName());
        return categoryRepository.save(existing);
    }

    @Transactional
    public void deleteCategory(Long id) {
        validateId(id);
        if (categoryRepository.findById(id).isEmpty())
            throw new EntityNotFoundException("Category not found",
                    "Nothing to delete: category with ID " + id + " does not exist");
        categoryRepository.deleteById(id);
    }

    private void validateCategory(Category category) {
        if (category == null) throw new ValidationException("Category cannot be null",
                "Provide a category object with id and name");
        validateId(category.getId());
        if (category.getName() == null || category.getName().isBlank())
            throw new ValidationException("Category name cannot be empty",
                    "Fill the 'name' field (non-blank string)");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new ValidationException("Id must be positive",
                "ID must be a positive number");
    }
}