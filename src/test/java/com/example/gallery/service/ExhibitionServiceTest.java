package com.example.gallery.service;

import com.example.gallery.models.Exhibition;
import com.example.gallery.models.ExhibitionType;
import com.example.gallery.repository.ExhibitRepository;
import com.example.gallery.repository.ExhibitionRepository;
import com.example.gallery.repository.InMemoryExhibitRepository;
import com.example.gallery.repository.InMemoryExhibitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты сервиса управления выставками (ExhibitionService).

 * Проверяются:
 *  - создание постоянной выставки (без дат проведения);
 *  - создание временной выставки (с датами начала и окончания);
 *  - работа с тегами (HashSet): добавление, удаление, проверка наличия;
 *  - фильтрация выставок по типу через Stream API.
 */
@DisplayName("Тесты сервиса выставок")
class ExhibitionServiceTest {

    private ExhibitionService exhibitionService;

    /**
     * ExhibitionService требует два репозитория: выставок и экспонатов
     * (второй нужен для добавления экспонатов на выставку).
     */
    @BeforeEach
    void setUp() {
        ExhibitionRepository exhibitionRepo = new InMemoryExhibitionRepository();
        ExhibitRepository exhibitRepo = new InMemoryExhibitRepository();
        exhibitionService = new ExhibitionService(exhibitionRepo, exhibitRepo);
    }

    /**
     * Создание постоянной выставки.
     * У постоянной выставки даты не задаются (null) — она идёт бессрочно.
     * Ожидание: тип выставки PERMANENT сохранён корректно.
     */
    @Test
    @DisplayName("Создание постоянной выставки")
    void testCreatePermanentExhibition() {
        Exhibition exhibition = new Exhibition(
                1L, "Permanent Art", ExhibitionType.PERMANENT, "Main Hall", null, null);

        Exhibition created = exhibitionService.createExhibition(exhibition);

        assertNotNull(created);
        assertEquals(ExhibitionType.PERMANENT, created.getType());
    }

    /**
     * Создание временной выставки с периодом проведения.
     * Ожидание: даты начала и окончания сохранены без изменений (проверяется работа с LocalDate).
     */
    @Test
    @DisplayName("Создание временной выставки с датами")
    void testCreateTemporaryExhibition() {
        Exhibition exhibition = new Exhibition(
                2L, "Temporary Show", ExhibitionType.TEMPORARY, "Hall 2",
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

        Exhibition created = exhibitionService.createExhibition(exhibition);

        assertNotNull(created);
        assertEquals(LocalDate.of(2024, 1, 1), created.getStartDate());
    }

    /**
     * Добавление и удаление тегов выставки.
     * Теги хранятся в HashSet и приводятся к нижнему регистру,
     * поэтому дубликаты невозможны, а поиск нечувствителен к регистру.
     * Ожидание: после добавления тег "modern" есть, после удаления — отсутствует.
     */
    @Test
    @DisplayName("Добавление и удаление тегов выставки")
    void testAddAndRemoveTag() {
        Exhibition exhibition = new Exhibition(
                1L, "Tagged Exhibition", ExhibitionType.PERMANENT, "Hall 1", null, null);
        exhibitionService.createExhibition(exhibition);

        // добавляем два тега
        exhibitionService.addTag(1L, "modern");
        exhibitionService.addTag(1L, "art");

        // тег присутствует (hasTag нормализует строку так же, как addTag)
        assertTrue(exhibitionService.getExhibition(1L).get().hasTag("modern"));

        // удаляем тег и проверяем, что он действительно исчез
        exhibitionService.removeTag(1L, "modern");
        assertFalse(exhibitionService.getExhibition(1L).get().hasTag("modern"));
    }

    /**
     * Фильтрация выставок по типу.
     * В хранилище 1 постоянная и 1 временная выставка.
     * Ожидание: findByType(PERMANENT) вернёт ровно одну запись —
     * проверяется корректность предиката в Stream API.
     */
    @Test
    @DisplayName("Фильтрация выставок по типу")
    void testFilterByType() {
        exhibitionService.createExhibition(new Exhibition(
                1L, "Perm 1", ExhibitionType.PERMANENT, "Hall", null, null));
        exhibitionService.createExhibition(new Exhibition(
                2L, "Temp 1", ExhibitionType.TEMPORARY, "Hall",
                LocalDate.now(), LocalDate.now().plusMonths(1)));

        List<Exhibition> permanent = exhibitionService.showExhibitionsByType(ExhibitionType.PERMANENT);

        assertEquals(1, permanent.size());
    }
}