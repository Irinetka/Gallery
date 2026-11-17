package com.example.gallery.service;

import com.example.gallery.models.Category;
import com.example.gallery.repository.CategoryRepository;
import com.example.gallery.repository.InMemoryCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты сервиса управления категориями экспонатов (CategoryService).
 *
 * Логика категорий аналогична логике авторов, поэтому набор проверок симметричен:
 * валидация id и названия, запрет дубликатов, CRUD-операции.
 */
@DisplayName("Тесты сервиса категорий")
class CategoryServiceTest {

    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    /** Новое пустое хранилище категорий перед каждым тестом. */
    @BeforeEach
    void setUp() {
        categoryRepository = new InMemoryCategoryRepository();
        categoryService = new CategoryService(categoryRepository);
    }

    /**
     * Успешное создание категории.
     * Ожидание: объект создан, id и название соответствуют переданным.
     */
    @Test
    @DisplayName("Создание категории с корректными данными")
    void testCreateCategorySuccess() {
        Category category = new Category(1L, "Painting");

        Category created = categoryService.createCategory(category);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("Painting", created.getName());
    }

    /**
     * Создание категории с уже существующим id.
     * Ожидание: IllegalStateException — дубликаты первичного ключа запрещены.
     */
    @Test
    @DisplayName("Создание категории с дублирующимся id")
    void testCreateCategoryDuplicateId() {
        categoryService.createCategory(new Category(1L, "Painting"));

        Category duplicate = new Category(1L, "Graphics");

        assertThrows(IllegalStateException.class, () -> categoryService.createCategory(duplicate));
    }

    /**
     * Передача null вместо категории.
     * Ожидание: IllegalArgumentException.
     */
    @Test
    @DisplayName("Создание категории из null-объекта")
    void testCreateCategoryNull() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(null));
    }

    /**
     * Отрицательный id категории.
     * Ожидание: IllegalArgumentException — id должен быть положительным.
     */
    @Test
    @DisplayName("Создание категории с невалидным id")
    void testCreateCategoryInvalidId() {
        Category category = new Category(-1L, "Painting");

        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(category));
    }

    /**
     * Пустое название категории ("").
     * Ожидание: IllegalArgumentException.
     */
    @Test
    @DisplayName("Создание категории с пустым названием")
    void testCreateCategoryEmptyName() {
        Category category = new Category(1L, "");

        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(category));
    }

    /**
     * Название из пробелов ("").
     * Ожидание: IllegalArgumentException — граничный случай проверки isBlank().
     */
    @Test
    @DisplayName("Создание категории с названием из пробелов")
    void testCreateCategoryBlankName() {
        Category category = new Category(1L, "   ");

        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(category));
    }

    /**
     * Успешное получение категории по id.
     * Ожидание: Optional содержит категорию "Painting".
     */
    @Test
    @DisplayName("Получение существующей категории")
    void testGetCategorySuccess() {
        categoryService.createCategory(new Category(1L, "Painting"));

        Category found = categoryService.getCategory(1L).orElse(null);

        assertNotNull(found);
        assertEquals("Painting", found.getName());
    }

    /**
     * Получение несуществующей категории.
     * Ожидание: пустой Optional (без исключения).
     */
    @Test
    @DisplayName("Получение несуществующей категории")
    void testGetCategoryNotFound() {
        Category found = categoryService.getCategory(999L).orElse(null);

        assertNull(found);
    }

    /**
     * Получение категории с невалидным id.
     * Ожидание: IllegalArgumentException.
     */
    @Test
    @DisplayName("Получение категории с невалидным id")
    void testGetCategoryInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategory(-1L));
    }

    /**
     * Получение списка всех категорий.
     * Ожидание: 3 элемента — по числу добавленных категорий.
     */
    @Test
    @DisplayName("Получение списка всех категорий")
    void testShowCategories() {
        categoryService.createCategory(new Category(1L, "Painting"));
        categoryService.createCategory(new Category(2L, "Graphics"));
        categoryService.createCategory(new Category(3L, "Sculpture"));

        List<Category> categories = categoryService.showCategories();

        assertEquals(3, categories.size());
    }

    /**
     * Список категорий пустого хранилища.
     * Ожидание: пустой список, а не null.
     */
    @Test
    @DisplayName("Получение пустого списка категорий")
    void testShowCategoriesEmpty() {
        List<Category> categories = categoryService.showCategories();

        assertTrue(categories.isEmpty());
    }

    /**
     * Успешное удаление категории.
     * Ожидание: после удаления категория не находится.
     */
    @Test
    @DisplayName("Удаление существующей категории")
    void testDeleteCategorySuccess() {
        categoryService.createCategory(new Category(1L, "Painting"));

        categoryService.deleteCategory(1L);

        assertTrue(categoryService.getCategory(1L).isEmpty());
    }

    /**
     * Удаление несуществующей категории.
     * Ожидание: IllegalArgumentException.
     */
    @Test
    @DisplayName("Удаление несуществующей категории")
    void testDeleteCategoryNotFound() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(999L));
    }

    /**
     * Удаление категории с невалидным id.
     * Ожидание: IllegalArgumentException.
     */
    @Test
    @DisplayName("Удаление категории с невалидным id")
    void testDeleteCategoryInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(-1L));
    }
}