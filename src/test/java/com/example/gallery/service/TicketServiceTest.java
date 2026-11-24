package com.example.gallery.service;

import com.example.gallery.exception.GalleryException;
import com.example.gallery.models.CustomerUser;
import com.example.gallery.models.Exhibition;
import com.example.gallery.models.ExhibitionType;
import com.example.gallery.models.Ticket;
import com.example.gallery.models.User;
import com.example.gallery.repository.ExhibitionRepository;
import com.example.gallery.repository.TicketRepository;
import com.example.gallery.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тесты для TicketService.
 * Использует @SpringBootTest + Mockito (без подключения к БД).
 */
@SpringBootTest
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExhibitionRepository exhibitionRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void testCreateTicket() {
        User user = new CustomerUser(1L, "visitor", "v@mail.com", "pass", "Visitor Name");
        Exhibition exhibition = new Exhibition(1L, "Art Show", ExhibitionType.TEMPORARY,
                "Hall 1", LocalDateTime.now().toLocalDate(),
                LocalDateTime.now().toLocalDate().plusDays(10));
        LocalDateTime visitTime = LocalDateTime.of(2024, 10, 25, 14, 30);

        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(exhibitionRepository.findById(1L)).thenReturn(Optional.of(exhibition));
        when(ticketRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Ticket ticket = new Ticket(1L, user, exhibition, visitTime, 500.0);
        Ticket created = ticketService.createTicket(ticket);

        assertNotNull(created);
        assertEquals(500.0, created.getPrice());
        assertEquals(visitTime, created.getVisitDateTime());
    }

    @Test
    void testCreateTicketInvalidPrice() {
        User user = new CustomerUser(1L, "visitor", "v@mail.com", "pass", "Visitor Name");
        Exhibition exhibition = new Exhibition(1L, "Art Show", ExhibitionType.TEMPORARY,
                "Hall 1", LocalDateTime.now().toLocalDate(),
                LocalDateTime.now().toLocalDate().plusDays(10));

        Ticket ticket = new Ticket(2L, user, exhibition, LocalDateTime.now(), -100.0);

        assertThrows(GalleryException.class, () -> ticketService.createTicket(ticket));
    }

    @Test
    void testShowVisitorTickets() {
        User user = new CustomerUser(1L, "visitor", "v@mail.com", "pass", "Visitor Name");
        Exhibition exhibition = new Exhibition(1L, "Art Show", ExhibitionType.TEMPORARY,
                "Hall 1", LocalDateTime.now().toLocalDate(),
                LocalDateTime.now().toLocalDate().plusDays(10));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(ticketRepository.findByVisitorId(1L)).thenReturn(List.of(
                new Ticket(1L, user, exhibition, LocalDateTime.now(), 500.0),
                new Ticket(2L, user, exhibition, LocalDateTime.now().plusDays(1), 600.0)
        ));

        List<Ticket> tickets = ticketService.showVisitorTickets(1L);

        assertEquals(2, tickets.size());
    }
}