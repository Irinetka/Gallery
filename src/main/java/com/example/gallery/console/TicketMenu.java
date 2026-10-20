package com.example.gallery.console;

import com.example.gallery.models.Exhibition;
import com.example.gallery.models.Ticket;
import com.example.gallery.service.ExhibitionService;
import com.example.gallery.service.TicketService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TicketMenu {
    private final Session session;
    private final ConsoleUtils console;
    private final TicketService ticketService;
    private final ExhibitionService exhibitionService;

    public TicketMenu(Session session, ConsoleUtils console,
                      TicketService ticketService, ExhibitionService exhibitionService) {
        this.session = session;
        this.console = console;
        this.ticketService = ticketService;
        this.exhibitionService = exhibitionService;
    }

    public void show() {
        while (session.isAuthenticated()) {
            System.out.println();
            System.out.println("TICKETS");
            System.out.println("1. My tickets");
            System.out.println("2. Buy ticket");
            System.out.println("3. Delete ticket");
            System.out.println("0. Back");

            String choice = console.readString("Select action: ");

            try {
                switch (choice) {
                    case "1" -> myTickets();
                    case "2" -> buy();
                    case "3" -> delete();
                    case "0" -> { return; }
                    default -> System.out.println("Unknown command.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
                console.pause();
            }
        }
    }

    private void myTickets() {
        List<Ticket> tickets = ticketService.showVisitorTickets(session.getCurrentUser().getId());
        printTickets(tickets);
        console.pause();
    }

    private void buy() {
        System.out.println();
        System.out.println("Available exhibitions");
        List<Exhibition> exhibitions = exhibitionService.showAllExhibitions();
        for (Exhibition exhibition : exhibitions) {
            System.out.println("ID: " + exhibition.getId() + " - " + exhibition.getTitle());
        }

        Long exhibitionId = console.readLong("Exhibition ID: ");
        Exhibition exhibition = exhibitionService.getExhibition(exhibitionId)
                .orElseThrow(() -> new IllegalArgumentException("Exhibition not found"));

        Long id = console.readLong("Ticket ID: ");
        String dateTimeStr = console.readString("Visit date and time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime visitDateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Double price = console.readDouble("Price: ");

        Ticket ticket = new Ticket(id, session.getCurrentUser(), exhibition, visitDateTime, price);
        ticketService.createTicket(ticket);
        System.out.println("Ticket purchased.");
        console.pause();
    }

    private void delete() {
        Long id = console.readLong("Ticket ID: ");
        Ticket ticket = ticketService.getTicket(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        if (!ticket.getVisitor().getId().equals(session.getCurrentUser().getId())) {
            throw new IllegalStateException("You can only delete your own tickets.");
        }

        ticketService.deleteTicket(id);
        System.out.println("Ticket deleted.");
        console.pause();
    }

    private void printTickets(List<Ticket> tickets) {
        System.out.println();
        System.out.println("Tickets");
        if (tickets.isEmpty()) {
            System.out.println("No tickets.");
            return;
        }
        for (Ticket ticket : tickets) {
            printTicket(ticket);
        }
    }

    private void printTicket(Ticket ticket) {
        System.out.println();
        System.out.println("ID: " + ticket.getId());
        System.out.println("Exhibition: " + ticket.getExhibition().getTitle());
        System.out.println("Visit: " + ticket.getVisitDateTime());
        System.out.println("Price: " + ticket.getPrice() + " rub.");
    }
}