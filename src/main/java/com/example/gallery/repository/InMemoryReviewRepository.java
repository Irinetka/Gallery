package com.example.gallery.repository;

import com.example.gallery.models.Review;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryReviewRepository implements ReviewRepository {
    private final Map<Long, Review> storage = new HashMap<>();

    @Override
    public Review create(Review review) {
        storage.put(review.getId(), review);
        return review;
    }

    @Override
    public Optional<Review> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Review> findByExhibitionId(Long exhibitionId) {
        return storage.values().stream()
                .filter(r -> r.getExhibition() != null && r.getExhibition().getId().equals(exhibitionId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findByVisitorId(Long visitorId) {
        return storage.values().stream()
                .filter(r -> r.getVisitor() != null && r.getVisitor().getId().equals(visitorId))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}