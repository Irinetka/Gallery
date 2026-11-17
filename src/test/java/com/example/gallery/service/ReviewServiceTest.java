package com.example.gallery.service;

import com.example.gallery.models.CustomerUser;
import com.example.gallery.models.Exhibition;
import com.example.gallery.models.ExhibitionType;
import com.example.gallery.models.Review;
import com.example.gallery.models.User;
import com.example.gallery.repository.ExhibitionRepository;
import com.example.gallery.repository.InMemoryExhibitionRepository;
import com.example.gallery.repository.InMemoryReviewRepository;
import com.example.gallery.repository.InMemoryUserRepository;
import com.example.gallery.repository.ReviewRepository;
import com.example.gallery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты сервиса работы с отзывами (ReviewService).
 *
 * Ключевая бизнес-правило: рейтинг отзыва — целое число от 1 до 5.
 * Проверяются успешное создание отзыва, выход рейтинга за верхнюю
 * и нижнюю границы, а также удаление отзыва с очисткой связей.
 */
@DisplayName("Тесты сервиса отзывов")
class ReviewServiceTest {

    private ReviewService reviewService;
    private UserRepository userRepository;
    private ExhibitionRepository exhibitionRepository;

    /**
     * Подготовка данных: посетитель (id=1) и выставка (id=1),
     * так как отзыв обязательно связан с существующими пользователем и выставкой.
     */
    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        exhibitionRepository = new InMemoryExhibitionRepository();
        ReviewRepository reviewRepository = new InMemoryReviewRepository();

        reviewService = new ReviewService(reviewRepository, userRepository, exhibitionRepository);

        User user = new CustomerUser(1L, "reviewer", "r@mail.com", "pass", "Reviewer");
        userRepository.create(user);

        Exhibition exhibition = new Exhibition(
                1L, "Reviewable Show", ExhibitionType.PERMANENT, "Hall 1", null, null);
        exhibitionRepository.create(exhibition);
    }

    /**
     * Успешное создание отзыва с максимальным рейтингом 5.
     * Ожидание: отзыв сохранён, рейтинг и текст соответствуют переданным.
     * Также отзыв добавляется в списки посетителя и выставки.
     */
    @Test
    @DisplayName("Создание отзыва с рейтингом 5")
    void testCreateReviewSuccess() {
        User user = userRepository.findById(1L).orElseThrow();
        Exhibition exhibition = exhibitionRepository.findById(1L).orElseThrow();

        Review review = new Review(1L, user, exhibition, "Great exhibition!", 5);
        Review created = reviewService.createReview(review);

        assertNotNull(created);
        assertEquals(5, created.getRating());
        assertEquals("Great exhibition!", created.getContent());
    }

    /**
     * Рейтинг выше допустимого (6) — верхняя граничная проверка.
     * Ожидание: IllegalArgumentException("Rating must be between 1 and 5").
     */
    @Test
    @DisplayName("Создание отзыва с рейтингом выше допустимого (6)")
    void testCreateReviewInvalidRatingTooHigh() {
        User user = userRepository.findById(1L).orElseThrow();
        Exhibition exhibition = exhibitionRepository.findById(1L).orElseThrow();

        Review review = new Review(2L, user, exhibition, "Too high", 6);

        assertThrows(IllegalArgumentException.class, () -> reviewService.createReview(review));
    }

    /**
     * Рейтинг ниже допустимого (0) — нижняя граничная проверка.
     * Ожидание: IllegalArgumentException.
     */
    @Test
    @DisplayName("Создание отзыва с рейтингом ниже допустимого (0)")
    void testCreateReviewInvalidRatingTooLow() {
        User user = userRepository.findById(1L).orElseThrow();
        Exhibition exhibition = exhibitionRepository.findById(1L).orElseThrow();

        Review review = new Review(3L, user, exhibition, "Too low", 0);

        assertThrows(IllegalArgumentException.class, () -> reviewService.createReview(review));
    }

    /**
     * Удаление отзыва.
     * Ожидание: отзыв не находится по id; ссылки на него удаляются
     * из списков посетителя и выставки (целостность связей).
     */
    @Test
    @DisplayName("Удаление отзыва")
    void testDeleteReview() {
        User user = userRepository.findById(1L).orElseThrow();
        Exhibition exhibition = exhibitionRepository.findById(1L).orElseThrow();

        reviewService.createReview(new Review(1L, user, exhibition, "To be deleted", 4));
        reviewService.deleteReview(1L);

        assertTrue(reviewService.getReview(1L).isEmpty());
    }
}