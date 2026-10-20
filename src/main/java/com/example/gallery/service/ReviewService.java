package com.example.gallery.service;

import com.example.gallery.models.Exhibition;
import com.example.gallery.models.Review;
import com.example.gallery.models.User;
import com.example.gallery.repository.ExhibitionRepository;
import com.example.gallery.repository.ReviewRepository;
import com.example.gallery.repository.UserRepository;
import java.util.List;
import java.util.Optional;

public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ExhibitionRepository exhibitionRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         UserRepository userRepository,
                         ExhibitionRepository exhibitionRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.exhibitionRepository = exhibitionRepository;
    }

    public Review createReview(Review review) {
        validateReview(review);
        if (reviewRepository.findById(review.getId()).isPresent())
            throw new IllegalStateException("Review with id " + review.getId() + " already exists");

        User visitor = userRepository.findById(review.getVisitor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Visitor not found"));
        Exhibition exhibition = exhibitionRepository.findById(review.getExhibition().getId())
                .orElseThrow(() -> new IllegalArgumentException("Exhibition not found"));

        Review created = reviewRepository.create(review);
        visitor.addReview(created);
        exhibition.addReview(created);
        return created;
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

    public void deleteReview(Long id) {
        validateId(id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review with id " + id + " not found"));
        if (review.getVisitor() != null) review.getVisitor().getReviews().remove(review);
        if (review.getExhibition() != null) review.getExhibition().getReviews().remove(review);
        reviewRepository.delete(id);
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
        if (id == null || id <= 0) throw new IllegalArgumentException("Id must be a positive number");
    }
}