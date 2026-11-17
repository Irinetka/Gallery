package com.example.gallery.repository;

import com.example.gallery.models.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"visitor", "exhibition"})
    List<Review> findByExhibitionId(Long exhibitionId);

    @EntityGraph(attributePaths = {"visitor", "exhibition"})
    List<Review> findByVisitorId(Long visitorId);

    @EntityGraph(attributePaths = {"visitor", "exhibition"})
    @Override
    List<Review> findAll();

    @EntityGraph(attributePaths = {"visitor", "exhibition"})
    @Override
    java.util.Optional<Review> findById(Long id);
}