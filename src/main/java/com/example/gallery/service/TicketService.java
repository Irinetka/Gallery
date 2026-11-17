package com.example.gallery.service;

import com.example.gallery.models.Exhibition;
import com.example.gallery.models.Ticket;
import com.example.gallery.models.User;
import com.example.gallery.repository.ExhibitionRepository;
import com.example.gallery.repository.TicketRepository;
import com.example.gallery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ExhibitionRepository exhibitionRepository;

    @Transactional
    public Ticket createTicket(Ticket ticket) {
        validateTicket(ticket);
        if (ticketRepository.findById(ticket.getId()).isPresent())
            throw new IllegalStateException("Ticket with id " + ticket.getId() + " already exists");
        User visitor = userRepository.findById(ticket.getVisitor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Visitor not found"));
        Exhibition exhibition = exhibitionRepository.findById(ticket.getExhibition().getId())
                .orElseThrow(() -> new IllegalArgumentException("Exhibition not found"));
        ticket.setVisitor(visitor);
        ticket.setExhibition(exhibition);
        return ticketRepository.save(ticket);
    }

    public Optional<Ticket> getTicket(Long id) {
        validateId(id);
        return ticketRepository.findById(id);
    }

    public List<Ticket> showVisitorTickets(Long visitorId) {
        validateId(visitorId);
        if (userRepository.findById(visitorId).isEmpty())
            throw new IllegalArgumentException("Visitor not found");
        return ticketRepository.findByVisitorId(visitorId);
    }

    public List<Ticket> showExhibitionTickets(Long exhibitionId) {
        validateId(exhibitionId);
        if (exhibitionRepository.findById(exhibitionId).isEmpty())
            throw new IllegalArgumentException("Exhibition not found");
        return ticketRepository.findByExhibitionId(exhibitionId);
    }

    public List<Ticket> search(Long visitorId, Long exhibitionId) {
        if (visitorId != null && exhibitionId != null)
            return ticketRepository.findAll().stream()
                    .filter(t -> t.getVisitor().getId().equals(visitorId))
                    .filter(t -> t.getExhibition().getId().equals(exhibitionId))
                    .toList();
        if (visitorId != null) return ticketRepository.findByVisitorId(visitorId);
        if (exhibitionId != null) return ticketRepository.findByExhibitionId(exhibitionId);
        return ticketRepository.findAll();
    }

    @Transactional
    public Ticket update(Long id, Ticket ticket) {
        validateId(id);
        Ticket existing = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        if (ticket.getVisitDateTime() != null) existing.setVisitDateTime(ticket.getVisitDateTime());
        if (ticket.getPrice() != null) existing.setPrice(ticket.getPrice());
        return ticketRepository.save(existing);
    }

    @Transactional
    public void deleteTicket(Long id) {
        validateId(id);
        if (ticketRepository.findById(id).isEmpty())
            throw new IllegalArgumentException("Ticket not found");
        ticketRepository.deleteById(id);
    }

    private void validateTicket(Ticket ticket) {
        if (ticket == null) throw new IllegalArgumentException("Ticket cannot be null");
        validateId(ticket.getId());
        if (ticket.getVisitor() == null || ticket.getVisitor().getId() == null)
            throw new IllegalArgumentException("Ticket visitor cannot be null");
        if (ticket.getExhibition() == null || ticket.getExhibition().getId() == null)
            throw new IllegalArgumentException("Ticket exhibition cannot be null");
        if (ticket.getVisitDateTime() == null)
            throw new IllegalArgumentException("Visit date and time cannot be null");
        if (ticket.getPrice() == null || ticket.getPrice() <= 0)
            throw new IllegalArgumentException("Ticket price must be positive");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Id must be positive");
    }
}