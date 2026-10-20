package com.example.gallery.service;

import com.example.gallery.models.CustomerUser;
import com.example.gallery.models.Exhibition;
import com.example.gallery.models.ExhibitionType;
import com.example.gallery.models.Ticket;
import com.example.gallery.models.User;
import com.example.gallery.repository.ExhibitionRepository;
import com.example.gallery.repository.InMemoryExhibitionRepository;
import com.example.gallery.repository.InMemoryTicketRepository;
import com.example.gallery.repository.InMemoryUserRepository;
import com.example.gallery.repository.TicketRepository;
import com.example.gallery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;

class TicketServiceTest {
    private TicketService ticketService;
    private UserRepository userRepository;
    private ExhibitionRepository exhibitionRepository;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        exhibitionRepository = new InMemoryExhibitionRepository();
        TicketRepository ticketRepository = new InMemoryTicketRepository();

        ticketService = new TicketService(ticketRepository, userRepository, exhibitionRepository);

        User user = new CustomerUser(1L, "visitor", "v@mail.com", "pass", "Visitor Name");
        userRepository.create(user);

        Exhibition exhibition = new Exhibition(1L, "Art Show", ExhibitionType.TEMPORARY, "Hall 1", LocalDateTime.now().toLocalDate(), LocalDateTime.now().toLocalDate().plusDays(10));
        exhibitionRepository.create(exhibition);
    }

    @Test
    void testCreateTicket() {
        User user = userRepository.findById(1L).orElseThrow();
        Exhibition exhibition = exhibitionRepository.findById(1L).orElseThrow();
        LocalDateTime visitTime = LocalDateTime.of(2024, 10, 25, 14, 30);

        Ticket ticket = new Ticket(1L, user, exhibition, visitTime, 500.0);
        Ticket created = ticketService.createTicket(ticket);

        assertNotNull(created);
        assertEquals(500.0, created.getPrice());
        assertEquals(visitTime, created.getVisitDateTime());
    }

    @Test
    void testCreateTicketInvalidPrice() {
        User user = userRepository.findById(1L).orElseThrow();
        Exhibition exhibition = exhibitionRepository.findById(1L).orElseThrow();

        Ticket ticket = new Ticket(2L, user, exhibition, LocalDateTime.now(), -100.0);

        assertThrows(IllegalArgumentException.class, () -> ticketService.createTicket(ticket));
    }

    @Test
    void testShowVisitorTickets() {
        User user = userRepository.findById(1L).orElseThrow();
        Exhibition exhibition = exhibitionRepository.findById(1L).orElseThrow();

        ticketService.createTicket(new Ticket(1L, user, exhibition, LocalDateTime.now(), 500.0));
        ticketService.createTicket(new Ticket(2L, user, exhibition, LocalDateTime.now().plusDays(1), 600.0));

        List<Ticket> tickets = ticketService.showVisitorTickets(1L);
        assertEquals(2, tickets.size());
    }
}