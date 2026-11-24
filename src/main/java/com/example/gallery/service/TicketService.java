package com.example.gallery.service;

import com.example.gallery.exception.BusinessRuleException;
import com.example.gallery.exception.DuplicateEntityException;
import com.example.gallery.exception.EntityNotFoundException;
import com.example.gallery.exception.ValidationException;
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
            throw new DuplicateEntityException("Ticket with id " + ticket.getId() + " already exists",
                    "Choose a free ticket ID");
        User visitor = userRepository.findById(ticket.getVisitor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Visitor not found",
                        "The user with this ID does not exist"));
        Exhibition exhibition = exhibitionRepository.findById(ticket.getExhibition().getId())
                .orElseThrow(() -> new EntityNotFoundException("Exhibition not found",
                        "Tickets can be sold only for existing exhibitions"));
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
            throw new EntityNotFoundException("Visitor not found", "Check the user ID");
        return ticketRepository.findByVisitorId(visitorId);
    }

    public List<Ticket> showExhibitionTickets(Long exhibitionId) {
        validateId(exhibitionId);
        if (exhibitionRepository.findById(exhibitionId).isEmpty())
            throw new EntityNotFoundException("Exhibition not found", "Check the exhibition ID");
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
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found",
                        "Check the ticket ID; it may have been deleted"));
        if (ticket.getVisitDateTime() != null) existing.setVisitDateTime(ticket.getVisitDateTime());
        if (ticket.getPrice() != null) existing.setPrice(ticket.getPrice());
        return ticketRepository.save(existing);
    }

    @Transactional
    public void deleteTicket(Long id) {
        validateId(id);
        if (ticketRepository.findById(id).isEmpty())
            throw new EntityNotFoundException("Ticket not found",
                    "Nothing to delete: ticket with ID " + id + " does not exist");
        ticketRepository.deleteById(id);
    }

    private void validateTicket(Ticket ticket) {
        if (ticket == null) throw new ValidationException("Ticket cannot be null",
                "Provide a complete ticket object");
        validateId(ticket.getId());
        if (ticket.getVisitor() == null || ticket.getVisitor().getId() == null)
            throw new ValidationException("Ticket visitor cannot be null", "Set 'visitor.id'");
        if (ticket.getExhibition() == null || ticket.getExhibition().getId() == null)
            throw new ValidationException("Ticket exhibition cannot be null", "Set 'exhibition.id'");
        if (ticket.getVisitDateTime() == null)
            throw new ValidationException("Visit date and time cannot be null",
                    "Set 'visitDateTime' in ISO format, e.g. 2026-12-01T14:30");
        if (ticket.getPrice() == null || ticket.getPrice() <= 0)
            throw new BusinessRuleException("Ticket price must be positive",
                    "Set 'price' as a number greater than 0");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new ValidationException("Id must be positive",
                "ID must be a positive number");
    }
}