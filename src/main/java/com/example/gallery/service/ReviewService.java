package com.example.gallery.service;

import com.example.gallery.exception.BusinessRuleException;
import com.example.gallery.exception.DuplicateEntityException;
import com.example.gallery.exception.EntityNotFoundException;
import com.example.gallery.exception.ValidationException;
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
            throw new DuplicateEntityException("Review with id " + review.getId() + " already exists",
                    "Choose a free review ID");
        User visitor = userRepository.findById(review.getVisitor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Visitor not found",
                        "The user with this ID does not exist; register first"));
        Exhibition exhibition = exhibitionRepository.findById(review.getExhibition().getId())
                .orElseThrow(() -> new EntityNotFoundException("Exhibition not found",
                        "Review can be left only for an existing exhibition"));
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
            throw new EntityNotFoundException("Exhibition not found",
                    "Check the exhibition ID");
        return reviewRepository.findByExhibitionId(exhibitionId);
    }

    public List<Review> showVisitorReviews(Long visitorId) {
        validateId(visitorId);
        if (userRepository.findById(visitorId).isEmpty())
            throw new EntityNotFoundException("Visitor not found",
                    "Check the user ID");
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
                .orElseThrow(() -> new EntityNotFoundException("Review not found",
                        "Check the review ID; it may have been deleted"));
        if (review.getContent() != null) existing.setContent(review.getContent());
        if (review.getRating() != null) existing.setRating(review.getRating());
        return reviewRepository.save(existing);
    }

    @Transactional
    public void deleteReview(Long id) {
        validateId(id);
        if (reviewRepository.findById(id).isEmpty())
            throw new EntityNotFoundException("Review not found",
                    "Nothing to delete: review with ID " + id + " does not exist");
        reviewRepository.deleteById(id);
    }

    private void validateReview(Review review) {
        if (review == null) throw new ValidationException("Review cannot be null",
                "Provide a complete review object");
        validateId(review.getId());
        if (review.getContent() == null || review.getContent().isBlank())
            throw new ValidationException("Review content cannot be empty", "Fill the 'content' field");
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5)
            throw new BusinessRuleException("Rating must be between 1 and 5",
                    "Set 'rating' as an integer from 1 (worst) to 5 (best)");
        if (review.getVisitor() == null || review.getVisitor().getId() == null)
            throw new ValidationException("Review visitor cannot be null",
                    "Set 'visitor.id' to an existing user");
        if (review.getExhibition() == null || review.getExhibition().getId() == null)
            throw new ValidationException("Review exhibition cannot be null",
                    "Set 'exhibition.id' to an existing exhibition");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new ValidationException("Id must be positive",
                "ID must be a positive number");
    }
}