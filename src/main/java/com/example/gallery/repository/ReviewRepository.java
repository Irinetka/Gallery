package com.example.gallery.repository;

import com.example.gallery.models.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    Review create(Review review);
    Optional<Review> findById(Long id);
    List<Review> findByExhibitionId(Long exhibitionId);
    List<Review> findByVisitorId(Long visitorId);
    void delete(Long id);
}