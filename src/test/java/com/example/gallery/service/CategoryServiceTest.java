package com.example.gallery.service;

import com.example.gallery.models.Category;
import com.example.gallery.repository.CategoryRepository;
import com.example.gallery.repository.InMemoryCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = new InMemoryCategoryRepository();
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void testCreateCategorySuccess() {
        Category category = new Category(1L, "Painting");
        Category created = categoryService.createCategory(category);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("Painting", created.getName());
    }

    @Test
    void testCreateCategoryDuplicateId() {
        categoryService.createCategory(new Category(1L, "Painting"));

        Category duplicate = new Category(1L, "Graphics");

        assertThrows(IllegalStateException.class, () -> categoryService.createCategory(duplicate));
    }

    @Test
    void testCreateCategoryNull() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(null));
    }

    @Test
    void testCreateCategoryInvalidId() {
        Category category = new Category(-1L, "Painting");

        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(category));
    }

    @Test
    void testCreateCategoryEmptyName() {
        Category category = new Category(1L, "");

        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(category));
    }

    @Test
    void testCreateCategoryBlankName() {
        Category category = new Category(1L, "   ");

        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(category));
    }

    @Test
    void testGetCategorySuccess() {
        categoryService.createCategory(new Category(1L, "Painting"));

        Category found = categoryService.getCategory(1L).orElse(null);

        assertNotNull(found);
        assertEquals("Painting", found.getName());
    }

    @Test
    void testGetCategoryNotFound() {
        Category found = categoryService.getCategory(999L).orElse(null);

        assertNull(found);
    }

    @Test
    void testGetCategoryInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategory(-1L));
    }

    @Test
    void testShowCategories() {
        categoryService.createCategory(new Category(1L, "Painting"));
        categoryService.createCategory(new Category(2L, "Graphics"));
        categoryService.createCategory(new Category(3L, "Sculpture"));

        List<Category> categories = categoryService.showCategories();

        assertEquals(3, categories.size());
    }

    @Test
    void testShowCategoriesEmpty() {
        List<Category> categories = categoryService.showCategories();

        assertTrue(categories.isEmpty());
    }

    @Test
    void testDeleteCategorySuccess() {
        categoryService.createCategory(new Category(1L, "Painting"));

        categoryService.deleteCategory(1L);

        assertTrue(categoryService.getCategory(1L).isEmpty());
    }

    @Test
    void testDeleteCategoryNotFound() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(999L));
    }

    @Test
    void testDeleteCategoryInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(-1L));
    }
}