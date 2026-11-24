package com.example.gallery.service;

import com.example.gallery.exception.GalleryException;
import com.example.gallery.models.Category;
import com.example.gallery.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тесты для CategoryService.
 * Использует @SpringBootTest + Mockito (без подключения к БД).
 */
@SpringBootTest
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testCreateCategorySuccess() {
        Category category = new Category(1L, "Painting");
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        when(categoryRepository.save(category)).thenReturn(category);

        Category created = categoryService.createCategory(category);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("Painting", created.getName());
    }

    @Test
    void testCreateCategoryDuplicateId() {
        categoryService.createCategory(new Category(1L, "Painting"));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "Painting")));

        Category duplicate = new Category(1L, "Graphics");

        assertThrows(GalleryException.class, () -> categoryService.createCategory(duplicate));
    }

    @Test
    void testCreateCategoryNull() {
        assertThrows(GalleryException.class, () -> categoryService.createCategory(null));
    }

    @Test
    void testCreateCategoryInvalidId() {
        Category category = new Category(-1L, "Painting");

        assertThrows(GalleryException.class, () -> categoryService.createCategory(category));
    }

    @Test
    void testCreateCategoryEmptyName() {
        Category category = new Category(1L, "");

        assertThrows(GalleryException.class, () -> categoryService.createCategory(category));
    }

    @Test
    void testCreateCategoryBlankName() {
        Category category = new Category(1L, "   ");

        assertThrows(GalleryException.class, () -> categoryService.createCategory(category));
    }

    @Test
    void testGetCategorySuccess() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "Painting")));

        Category found = categoryService.getCategory(1L).orElse(null);

        assertNotNull(found);
        assertEquals("Painting", found.getName());
    }

    @Test
    void testGetCategoryNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        Category found = categoryService.getCategory(999L).orElse(null);

        assertNull(found);
    }

    @Test
    void testGetCategoryInvalidId() {
        assertThrows(GalleryException.class, () -> categoryService.getCategory(-1L));
    }

    @Test
    void testShowCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(
                new Category(1L, "Painting"),
                new Category(2L, "Graphics"),
                new Category(3L, "Sculpture")
        ));

        List<Category> categories = categoryService.showCategories();

        assertEquals(3, categories.size());
    }

    @Test
    void testShowCategoriesEmpty() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        List<Category> categories = categoryService.showCategories();

        assertTrue(categories.isEmpty());
    }

    @Test
    void testDeleteCategorySuccess() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "Painting")));

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategoryNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(GalleryException.class, () -> categoryService.deleteCategory(999L));
    }

    @Test
    void testDeleteCategoryInvalidId() {
        assertThrows(GalleryException.class, () -> categoryService.deleteCategory(-1L));
    }
}