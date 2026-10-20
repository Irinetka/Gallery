package com.example.gallery.repository;

import com.example.gallery.models.Ticket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryTicketRepository implements TicketRepository {
    private final Map<Long, Ticket> storage = new HashMap<>();

    @Override
    public Ticket create(Ticket ticket) {
        storage.put(ticket.getId(), ticket);
        return ticket;
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Ticket> findByVisitorId(Long visitorId) {
        return storage.values().stream()
                .filter(t -> t.getVisitor() != null && t.getVisitor().getId().equals(visitorId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> findByExhibitionId(Long exhibitionId) {
        return storage.values().stream()
                .filter(t -> t.getExhibition() != null && t.getExhibition().getId().equals(exhibitionId))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}