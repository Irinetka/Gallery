package com.example.gallery.service;

import com.example.gallery.models.CustomerUser;
import com.example.gallery.models.Exhibition;
import com.example.gallery.models.ExhibitionType;
import com.example.gallery.models.Review;
import com.example.gallery.models.User;
import com.example.gallery.repository.ExhibitionRepository;
import com.example.gallery.repository.ReviewRepository;
import com.example.gallery.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тесты для ReviewService.
 * Использует @SpringBootTest + Mockito (без подключения к БД).
 */
@SpringBootTest
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExhibitionRepository exhibitionRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void testCreateReviewSuccess() {
        User user = new CustomerUser(1L, "reviewer", "r@mail.com", "pass", "Reviewer");
        Exhibition exhibition = new Exhibition(1L, "Reviewable Show", ExhibitionType.PERMANENT,
                "Hall 1", null, null);

        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(exhibitionRepository.findById(1L)).thenReturn(Optional.of(exhibition));
        when(reviewRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Review review = new Review(1L, user, exhibition, "Great exhibition!", 5);
        Review created = reviewService.createReview(review);

        assertNotNull(created);
        assertEquals(5, created.getRating());
        assertEquals("Great exhibition!", created.getContent());
    }

    @Test
    void testCreateReviewInvalidRatingTooHigh() {
        User user = new CustomerUser(1L, "reviewer", "r@mail.com", "pass", "Reviewer");
        Exhibition exhibition = new Exhibition(1L, "Reviewable Show", ExhibitionType.PERMANENT,
                "Hall 1", null, null);

        Review review = new Review(2L, user, exhibition, "Too high", 6);

        assertThrows(IllegalArgumentException.class, () -> reviewService.createReview(review));
    }

    @Test
    void testCreateReviewInvalidRatingTooLow() {
        User user = new CustomerUser(1L, "reviewer", "r@mail.com", "pass", "Reviewer");
        Exhibition exhibition = new Exhibition(1L, "Reviewable Show", ExhibitionType.PERMANENT,
                "Hall 1", null, null);

        Review review = new Review(3L, user, exhibition, "Too low", 0);

        assertThrows(IllegalArgumentException.class, () -> reviewService.createReview(review));
    }

    @Test
    void testDeleteReview() {
        User user = new CustomerUser(1L, "reviewer", "r@mail.com", "pass", "Reviewer");
        Exhibition exhibition = new Exhibition(1L, "Reviewable Show", ExhibitionType.PERMANENT,
                "Hall 1", null, null);

        Review review = new Review(1L, user, exhibition, "To be deleted", 4);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(1L);

        verify(reviewRepository, times(1)).deleteById(1L);  // Проверяем вызов метода
    }
}