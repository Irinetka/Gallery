package com.example.gallery.repository;

import com.example.gallery.models.Exhibit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExhibitRepository extends JpaRepository<Exhibit, Long> {

    @EntityGraph(attributePaths = {"author", "category"})
    List<Exhibit> findByAuthorId(Long authorId);

    @EntityGraph(attributePaths = {"author", "category"})
    List<Exhibit> findByCategoryId(Long categoryId);

    @EntityGraph(attributePaths = {"author", "category"})
    List<Exhibit> findByYearBetween(Integer startYear, Integer endYear);

    @EntityGraph(attributePaths = {"author", "category"})
    List<Exhibit> findByTitleContainingIgnoreCase(String title);

    @EntityGraph(attributePaths = {"author", "category"})
    @Override
    List<Exhibit> findAll();

    @EntityGraph(attributePaths = {"author", "category"})
    @Override
    java.util.Optional<Exhibit> findById(Long id);
}