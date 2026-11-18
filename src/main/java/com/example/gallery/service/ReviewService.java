package com.example.gallery.service;

import com.example.gallery.models.Exhibition;
import com.example.gallery.models.Review;
import com.example.gallery.models.User;
import com.example.gallery.repository.ExhibitionRepository;
import com.example.gallery.repository.ReviewRepository;
import com.example.gallery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ExhibitionRepository exhibitionRepository;

    @Transactional
    public Review createReview(Review review) {
        validateReview(review);
        if (reviewRepository.findById(review.getId()).isPresent())
            throw new IllegalStateException("Review with id " + review.getId() + " already exists");
        User visitor = userRepository.findById(review.getVisitor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Visitor not found"));
        Exhibition exhibition = exhibitionRepository.findById(review.getExhibition().getId())
                .orElseThrow(() -> new IllegalArgumentException("Exhibition not found"));
        review.setVisitor(visitor);
        review.setExhibition(exhibition);
        return reviewRepository.save(review);
    }

    public Optional<Review> getReview(Long id) {
        validateId(id);
        return reviewRepository.findById(id);
    }

    public List<Review> showExhibitionReviews(Long exhibitionId) {
        validateId(exhibitionId);
        if (exhibitionRepository.findById(exhibitionId).isEmpty())
            throw new IllegalArgumentException("Exhibition not found");
        return reviewRepository.findByExhibitionId(exhibitionId);
    }

    public List<Review> showVisitorReviews(Long visitorId) {
        validateId(visitorId);
        if (userRepository.findById(visitorId).isEmpty())
            throw new IllegalArgumentException("Visitor not found");
        return reviewRepository.findByVisitorId(visitorId);
    }

    public List<Review> search(Long exhibitionId, Long visitorId) {
        if (exhibitionId != null && visitorId != null)
            return reviewRepository.findAll().stream()
                    .filter(r -> r.getExhibition().getId().equals(exhibitionId))
                    .filter(r -> r.getVisitor().getId().equals(visitorId))
                    .toList();
        if (exhibitionId != null) return reviewRepository.findByExhibitionId(exhibitionId);
        if (visitorId != null) return reviewRepository.findByVisitorId(visitorId);
        return reviewRepository.findAll();
    }

    @Transactional
    public Review update(Long id, Review review) {
        validateId(id);
        Review existing = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        if (review.getContent() != null) existing.setContent(review.getContent());
        if (review.getRating() != null) existing.setRating(review.getRating());
        return reviewRepository.save(existing);
    }

    @Transactional
    public void deleteReview(Long id) {
        validateId(id);
        if (reviewRepository.findById(id).isEmpty())
            throw new IllegalArgumentException("Review not found");
        reviewRepository.deleteById(id);
    }

    private void validateReview(Review review) {
        if (review == null) throw new IllegalArgumentException("Review cannot be null");
        validateId(review.getId());
        if (review.getContent() == null || review.getContent().isBlank())
            throw new IllegalArgumentException("Review content cannot be empty");
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5)
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        if (review.getVisitor() == null || review.getVisitor().getId() == null)
            throw new IllegalArgumentException("Review visitor cannot be null");
        if (review.getExhibition() == null || review.getExhibition().getId() == null)
            throw new IllegalArgumentException("Review exhibition cannot be null");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Id must be positive");
    }
}