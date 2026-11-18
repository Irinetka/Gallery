package com.example.gallery.repository;

import com.example.gallery.models.Ticket;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @EntityGraph(attributePaths = {"visitor", "exhibition"})
    List<Ticket> findByVisitorId(Long visitorId);

    @EntityGraph(attributePaths = {"visitor", "exhibition"})
    List<Ticket> findByExhibitionId(Long exhibitionId);

    @EntityGraph(attributePaths = {"visitor", "exhibition"})
    @Override
    List<Ticket> findAll();

    @EntityGraph(attributePaths = {"visitor", "exhibition"})
    @Override
    java.util.Optional<Ticket> findById(Long id);
}