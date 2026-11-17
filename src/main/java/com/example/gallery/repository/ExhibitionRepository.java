package com.example.gallery.repository;

import com.example.gallery.models.Exhibition;
import com.example.gallery.models.ExhibitionType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

    @EntityGraph(attributePaths = {"exhibits", "reviews", "tickets", "tags"})
    List<Exhibition> findByType(ExhibitionType type);

    @EntityGraph(attributePaths = {"exhibits", "reviews", "tickets", "tags"})
    List<Exhibition> findByTitleContainingIgnoreCase(String title);

    @EntityGraph(attributePaths = {"exhibits", "reviews", "tickets", "tags"})
    List<Exhibition> findByTitleContainingIgnoreCaseAndType(String title, ExhibitionType type);

    @EntityGraph(attributePaths = {"exhibits", "reviews", "tickets", "tags"})
    @Override
    List<Exhibition> findAll();

    @EntityGraph(attributePaths = {"exhibits", "reviews", "tickets", "tags"})
    @Override
    Optional<Exhibition> findById(Long id);
}