package com.example.gallery.service;

import com.example.gallery.models.Author;
import com.example.gallery.repository.AuthorRepository;
import com.example.gallery.repository.InMemoryAuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты сервиса управления авторами произведений (AuthorService).
 *
 * Проверяется бизнес-логика слоя service:
 *  - валидация полей (id должен быть положительным, имя не пустое);
 *  - запрет дубликатов по идентификатору;
 *  - корректность CRUD-операций (создание, чтение, список, удаление).
 *
 * Хранилище — InMemoryAuthorRepository (HashMap в оперативной памяти),
 * поэтому тесты не требуют базы данных и выполняются изолированно.
 */
@DisplayName("Тесты сервиса авторов")
class AuthorServiceTest {

    private AuthorRepository authorRepository;
    private AuthorService authorService;

    /**
     * Перед КАЖДЫМ тестом создаётся новое пустое хранилище и новый сервис.
     * Это гарантирует независимость тестов друг от друга:
     * данные, добавленные в одном тесте, не влияют на другой.
     */
    @BeforeEach
    void setUp() {
        authorRepository = new InMemoryAuthorRepository();
        authorService = new AuthorService(authorRepository);
    }

    /**
     * Успешное создание автора.
     * Ожидание: сервис вернёт непустой объект с теми же id и ФИО, что были переданы.
     */
    @Test
    @DisplayName("Создание автора с корректными данными")
    void testCreateAuthorSuccess() {
        Author author = new Author(1L, "Aivazovsky Ivan");

        Author created = authorService.createAuthor(author);

        assertNotNull(created);                             // объект действительно создан
        assertEquals(1L, created.getId());                   // id сохранён без изменений
        assertEquals("Aivazovsky Ivan", created.getFullName()); // ФИО сохранено без изменений
    }

    /**
     * Попытка создать автора с уже существующим id.
     * Ожидание: IllegalStateException — сервис запрещает перезапись существующей записи.
     */
    @Test
    @DisplayName("Создание автора с дублирующимся id")
    void testCreateAuthorDuplicateId() {
        authorService.createAuthor(new Author(1L, "Aivazovsky Ivan"));

        Author duplicate = new Author(1L, "Vasnetsov Viktor"); // тот же id = 1

        assertThrows(IllegalStateException.class, () -> authorService.createAuthor(duplicate));
    }

    /**
     * Передача null вместо объекта автора.
     * Ожидание: IllegalArgumentException (сработала проверка validateAuthor).
     */
    @Test
    @DisplayName("Создание автора из null-объекта")
    void testCreateAuthorNull() {
        assertThrows(IllegalArgumentException.class, () -> authorService.createAuthor(null));
    }

    /**
     * Отрицательный идентификатор.
     * Ожидание: IllegalArgumentException — id должен быть положительным числом.
     */
    @Test
    @DisplayName("Создание автора с невалидным (отрицательным) id")
    void testCreateAuthorInvalidId() {
        Author author = new Author(-1L, "Aivazovsky Ivan");

        assertThrows(IllegalArgumentException.class, () -> authorService.createAuthor(author));
    }

    /**
     * Пустая строка в качестве ФИО ("").
     * Ожидание: IllegalArgumentException — имя автора обязательно.
     */
    @Test
    @DisplayName("Создание автора с пустым именем")
    void testCreateAuthorEmptyFullName() {
        Author author = new Author(1L, "");

        assertThrows(IllegalArgumentException.class, () -> authorService.createAuthor(author));
    }

    /**
     * Имя состоит только из пробелов ("   ").
     * Ожидание: IllegalArgumentException — проверка isBlank() отсекает такие значения.
     * Это граничный случай, который не ловится обычной проверкой на isEmpty().
     */
    @Test
    @DisplayName("Создание автора с именем из пробелов")
    void testCreateAuthorBlankFullName() {
        Author author = new Author(1L, "   ");

        assertThrows(IllegalArgumentException.class, () -> authorService.createAuthor(author));
    }

    /**
     * Успешное получение ранее созданного автора по id.
     * Ожидание: Optional содержит объект с нужным ФИО.
     */
    @Test
    @DisplayName("Получение существующего автора по id")
    void testGetAuthorSuccess() {
        authorService.createAuthor(new Author(1L, "Aivazovsky Ivan"));

        Author found = authorService.getAuthor(1L).orElse(null);

        assertNotNull(found);
        assertEquals("Aivazovsky Ivan", found.getFullName());
    }

    /**
     * Поиск автора, которого нет в хранилище (id = 999).
     * Ожидание: пустой Optional, а НЕ исключение — отсутствие записи является
     * нормальным результатом поиска (безопасная работа через Optional).
     */
    @Test
    @DisplayName("Получение несуществующего автора")
    void testGetAuthorNotFound() {
        Author found = authorService.getAuthor(999L).orElse(null);

        assertNull(found);
    }

    /**
     * Получение автора с невалидным id.
     * Ожидание: IllegalArgumentException — валидация выполняется ДО обращения к хранилищу.
     */
    @Test
    @DisplayName("Получение автора с невалидным id")
    void testGetAuthorInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> authorService.getAuthor(-1L));
    }

    /**
     * Получение списка всех авторов.
     * Ожидание: размер списка равен количеству добавленных авторов (3).
     */
    @Test
    @DisplayName("Получение списка всех авторов")
    void testShowAuthors() {
        authorService.createAuthor(new Author(1L, "Aivazovsky Ivan"));
        authorService.createAuthor(new Author(2L, "Vasnetsov Viktor"));
        authorService.createAuthor(new Author(3L, "Repin Ilya"));

        List<Author> authors = authorService.showAuthors();

        assertEquals(3, authors.size());
    }

    /**
     * Получение списка из пустого хранилища.
     * Ожидание: пустой список (не null) — это важно для корректной работы циклов в меню.
     */
    @Test
    @DisplayName("Получение пустого списка авторов")
    void testShowAuthorsEmpty() {
        List<Author> authors = authorService.showAuthors();

        assertTrue(authors.isEmpty());
    }

    /**
     * Успешное удаление автора.
     * Ожидание: после удаления поиск по тому же id возвращает пустой Optional.
     */
    @Test
    @DisplayName("Удаление существующего автора")
    void testDeleteAuthorSuccess() {
        authorService.createAuthor(new Author(1L, "Aivazovsky Ivan"));

        authorService.deleteAuthor(1L);

        assertTrue(authorService.getAuthor(1L).isEmpty());
    }

    /**
     * Удаление несуществующего автора.
     * Ожидание: IllegalArgumentException с сообщением "not found".
     */
    @Test
    @DisplayName("Удаление несуществующего автора")
    void testDeleteAuthorNotFound() {
        assertThrows(IllegalArgumentException.class, () -> authorService.deleteAuthor(999L));
    }

    /**
     * Удаление автора с невалидным id.
     * Ожидание: IllegalArgumentException (срабатывает validateId).
     */
    @Test
    @DisplayName("Удаление автора с невалидным id")
    void testDeleteAuthorInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> authorService.deleteAuthor(-1L));
    }
}