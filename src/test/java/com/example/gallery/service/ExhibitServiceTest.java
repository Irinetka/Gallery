package com.example.gallery.service;

import com.example.gallery.models.Author;
import com.example.gallery.models.Category;
import com.example.gallery.models.Exhibit;
import com.example.gallery.repository.AuthorRepository;
import com.example.gallery.repository.CategoryRepository;
import com.example.gallery.repository.ExhibitRepository;
import com.example.gallery.repository.InMemoryAuthorRepository;
import com.example.gallery.repository.InMemoryCategoryRepository;
import com.example.gallery.repository.InMemoryExhibitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты сервиса управления экспонатами (ExhibitService).

 * Проверяются:
 *  - создание экспоната с привязкой к существующим автору и категории;
 *  - многопоточный поиск по названию (успех, пустой результат, пустой запрос);
 *  - фильтрация по автору и по диапазону лет;
 *  - удаление экспоната с очисткой связей у автора и категории.
 */
@DisplayName("Тесты сервиса экспонатов")
class ExhibitServiceTest {

    private ExhibitService exhibitService;
    private AuthorService authorService;
    private CategoryService categoryService;

    /**
     * Подготовка окружения: три репозитория + сервис многопоточности.
     * Дополнительно сразу создаются автор (id=1) и категория (id=1),
     * так как ExhibitService.createExhibit() требует их наличия в хранилище.
     */
    @BeforeEach
    void setUp() {
        AuthorRepository authorRepo = new InMemoryAuthorRepository();
        CategoryRepository categoryRepo = new InMemoryCategoryRepository();
        ExhibitRepository exhibitRepo = new InMemoryExhibitRepository();
        MultiThreadService multiThreadService = new MultiThreadService();

        authorService = new AuthorService(authorRepo);
        categoryService = new CategoryService(categoryRepo);
        exhibitService = new ExhibitService(exhibitRepo, authorRepo, categoryRepo, multiThreadService);

        authorService.createAuthor(new Author(1L, "Aivazovsky Ivan"));
        categoryService.createCategory(new Category(1L, "Painting"));
    }

    /**
     * Успешное создание экспоната.
     * Ожидание: объект сохранён, название соответствует переданному;
     * внутри сервиса экспонат также добавляется в списки автора и категории.
     */
    @Test
    @DisplayName("Создание экспоната")
    void testCreateExhibit() {
        Author author = authorService.getAuthor(1L).orElseThrow();
        Category category = categoryService.getCategory(1L).orElseThrow();

        Exhibit exhibit = new Exhibit(1L, "The Ninth Wave", "INV-001", 1850,
                "221x332", "Oil on canvas", "Painting", "Hall 1", author, category);

        Exhibit created = exhibitService.createExhibit(exhibit);

        assertNotNull(created);
        assertEquals("The Ninth Wave", created.getTitle());
    }

    /**
     * Многопоточный поиск по названию — успешный сценарий.
     * Из трёх экспонатов подстрока "Ninth" встречается только в одном названии.
     * Ожидание: найден ровно 1 экспонат "The Ninth Wave".

     * Побочный эффект (проверяется визуально по логу): для найденных экспонатов
     * запускается обработка в именованных потоках GalleryWorker-N.
     * Сам поиск регистронезависимый (query и title приводятся к нижнему регистру).
     */
    @Test
    @DisplayName("Многопоточный поиск экспонатов по названию")
    void testSearchExhibitsWithMultithreading() {
        Author author = authorService.getAuthor(1L).orElseThrow();
        Category category = categoryService.getCategory(1L).orElseThrow();

        exhibitService.createExhibit(new Exhibit(1L, "The Ninth Wave", "INV-001", 1850,
                "221x332", "Oil", "Painting", "Hall 1", author, category));
        exhibitService.createExhibit(new Exhibit(2L, "Black Sea", "INV-002", 1881,
                "150x200", "Oil", "Painting", "Hall 2", author, category));
        exhibitService.createExhibit(new Exhibit(3L, "Bogatyrs", "INV-003", 1898,
                "295x446", "Oil", "Painting", "Hall 3", author, category));

        List<Exhibit> result = exhibitService.searchExhibitsWithMultithreading("Ninth");

        assertEquals(1, result.size());
        assertEquals("The Ninth Wave", result.get(0).getTitle());
    }

    /**
     * Многопоточный поиск без совпадений.
     * Ожидание: пустой список; многопоточная обработка НЕ запускается
     * (в логе выводится "No exhibits found for query: ...").
     */
    @Test
    @DisplayName("Многопоточный поиск: пустой результат")
    void testSearchExhibitsEmptyResult() {
        Author author = authorService.getAuthor(1L).orElseThrow();
        Category category = categoryService.getCategory(1L).orElseThrow();

        exhibitService.createExhibit(new Exhibit(1L, "The Ninth Wave", "INV-001", 1850,
                "221x332", "Oil", "Painting", "Hall 1", author, category));

        List<Exhibit> result = exhibitService.searchExhibitsWithMultithreading("NonExistent");

        assertTrue(result.isEmpty());
    }

    /**
     * Поиск с пустым запросом.
     * Ожидание: IllegalArgumentException("Search query cannot be empty") —
     * пустой запрос не должен приводить к обработке всей коллекции.
     */
    @Test
    @DisplayName("Многопоточный поиск: пустой запрос")
    void testSearchExhibitsEmptyQuery() {
        assertThrows(IllegalArgumentException.class, () ->
                exhibitService.searchExhibitsWithMultithreading(""));
    }

    /**
     * Фильтрация экспонатов по автору.
     * Оба экспоната принадлежат автору с id=1.
     * Ожидание: результат содержит 2 записи.
     */
    @Test
    @DisplayName("Фильтрация экспонатов по автору")
    void testFilterByAuthor() {
        Author author = authorService.getAuthor(1L).orElseThrow();
        Category category = categoryService.getCategory(1L).orElseThrow();

        exhibitService.createExhibit(new Exhibit(1L, "Painting 1", "INV-001", 1850,
                "100x100", "Oil", "Painting", "Hall 1", author, category));
        exhibitService.createExhibit(new Exhibit(2L, "Painting 2", "INV-002", 1860,
                "100x100", "Oil", "Painting", "Hall 2", author, category));

        List<Exhibit> result = exhibitService.showExhibitsByAuthor(1L);

        assertEquals(2, result.size());
    }

    /**
     * Фильтрация по диапазону лет создания.
     * Экспонаты: 1850 и 1900 год; диапазон запроса — 1840..1860.
     * Ожидание: в выборку попадёт только экспонат 1850 года (границы включительные).
     */
    @Test
    @DisplayName("Фильтрация экспонатов по диапазону лет")
    void testFilterByYearRange() {
        Author author = authorService.getAuthor(1L).orElseThrow();
        Category category = categoryService.getCategory(1L).orElseThrow();

        exhibitService.createExhibit(new Exhibit(1L, "Painting 1850", "INV-001", 1850,
                "100x100", "Oil", "Painting", "Hall 1", author, category));
        exhibitService.createExhibit(new Exhibit(2L, "Painting 1900", "INV-002", 1900,
                "100x100", "Oil", "Painting", "Hall 2", author, category));

        List<Exhibit> result = exhibitService.showExhibitsByYearRange(1840, 1860);

        assertEquals(1, result.size());
    }

    /**
     * Удаление экспоната.
     * Ожидание: экспонат больше не находится по id.
     * Дополнительно сервис удаляет ссылку на экспонат из списков
     * автора и категории, чтобы не оставалось «висячих» связей.
     */
    @Test
    @DisplayName("Удаление экспоната")
    void testDeleteExhibit() {
        Author author = authorService.getAuthor(1L).orElseThrow();
        Category category = categoryService.getCategory(1L).orElseThrow();

        exhibitService.createExhibit(new Exhibit(1L, "Test", "INV-001", 1850,
                "100x100", "Oil", "Painting", "Hall 1", author, category));

        exhibitService.deleteExhibit(1L);

        assertTrue(exhibitService.getExhibit(1L).isEmpty());
    }
}