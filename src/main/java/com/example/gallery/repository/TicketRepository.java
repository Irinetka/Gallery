package com.example.gallery.repository;

import com.example.gallery.models.Ticket;
import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Ticket create(Ticket ticket);
    Optional<Ticket> findById(Long id);
    List<Ticket> findByVisitorId(Long visitorId);
    List<Ticket> findByExhibitionId(Long exhibitionId);
    void delete(Long id);
}