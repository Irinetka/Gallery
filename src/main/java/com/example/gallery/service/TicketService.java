package com.example.gallery.service;

import com.example.gallery.models.Exhibition;
import com.example.gallery.models.Ticket;
import com.example.gallery.models.User;
import com.example.gallery.repository.ExhibitionRepository;
import com.example.gallery.repository.TicketRepository;
import com.example.gallery.repository.UserRepository;
import java.util.List;
import java.util.Optional;

public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ExhibitionRepository exhibitionRepository;

    public TicketService(TicketRepository ticketRepository,
                         UserRepository userRepository,
                         ExhibitionRepository exhibitionRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.exhibitionRepository = exhibitionRepository;
    }

    public Ticket createTicket(Ticket ticket) {
        validateTicket(ticket);
        if (ticketRepository.findById(ticket.getId()).isPresent())
            throw new IllegalStateException("Ticket with id " + ticket.getId() + " already exists");

        User visitor = userRepository.findById(ticket.getVisitor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Visitor not found"));
        Exhibition exhibition = exhibitionRepository.findById(ticket.getExhibition().getId())
                .orElseThrow(() -> new IllegalArgumentException("Exhibition not found"));

        Ticket created = ticketRepository.create(ticket);
        visitor.addTicket(created);
        exhibition.getTickets().add(created);
        return created;
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

    public void deleteTicket(Long id) {
        validateId(id);
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket with id " + id + " not found"));
        if (ticket.getVisitor() != null) ticket.getVisitor().getTickets().remove(ticket);
        if (ticket.getExhibition() != null) ticket.getExhibition().getTickets().remove(ticket);
        ticketRepository.delete(id);
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
        if (id == null || id <= 0) throw new IllegalArgumentException("Id must be a positive number");
    }
}