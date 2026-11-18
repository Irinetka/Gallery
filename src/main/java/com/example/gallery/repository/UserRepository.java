package com.example.gallery.repository;

import com.example.gallery.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"tickets", "reviews"})
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"tickets", "reviews"})
    @Override
    Optional<User> findById(Long id);
}