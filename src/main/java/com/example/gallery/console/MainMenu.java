package com.example.gallery.console;

import com.example.gallery.models.User;
import com.example.gallery.models.Exhibit;
import com.example.gallery.service.AuthorService;
import com.example.gallery.service.CategoryService;
import com.example.gallery.service.ExhibitService;
import com.example.gallery.service.ExhibitionService;
import com.example.gallery.service.MultiThreadService;
import com.example.gallery.service.ReviewService;
import com.example.gallery.service.TicketService;
import com.example.gallery.service.UserService;
import java.util.List;

public class MainMenu {
    private final Session session;
    private final ConsoleUtils console;
    private final UserService userService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final ExhibitService exhibitService;
    private final ExhibitionService exhibitionService;
    private final ReviewService reviewService;
    private final TicketService ticketService;
    private final MultiThreadService multiThreadService;

    public MainMenu(Session session, ConsoleUtils console,
                    UserService userService, AuthorService authorService,
                    CategoryService categoryService, ExhibitService exhibitService,
                    ExhibitionService exhibitionService, ReviewService reviewService,
                    TicketService ticketService, MultiThreadService multiThreadService) {
        this.session = session;
        this.console = console;
        this.userService = userService;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.exhibitService = exhibitService;
        this.exhibitionService = exhibitionService;
        this.reviewService = reviewService;
        this.ticketService = ticketService;
        this.multiThreadService = multiThreadService;
    }

    public void show() {
        ExhibitMenu exhibitMenu = new ExhibitMenu(session, console, exhibitService, authorService, categoryService);
        ExhibitionMenu exhibitionMenu = new ExhibitionMenu(session, console, exhibitionService);
        ReviewMenu reviewMenu = new ReviewMenu(session, console, reviewService, exhibitionService);
        TicketMenu ticketMenu = new TicketMenu(session, console, ticketService, exhibitionService);

        while (session.isAuthenticated()) {
            User user = session.getCurrentUser();
            System.out.println();
            System.out.println("GALLERY");
            System.out.println("User: " + user.getUsername());
            System.out.println("Role: " + user.getRole());
            System.out.println();
            System.out.println("1. My profile");
            System.out.println("2. Exhibits");
            System.out.println("3. Exhibitions");
            System.out.println("4. Reviews");
            System.out.println("5. Tickets");
            System.out.println("6. Multithread processing");
            System.out.println("0. Logout");
            System.out.println();

            String choice = console.readString("Select action: ");

            try {
                switch (choice) {
                    case "1" -> showProfile();
                    case "2" -> exhibitMenu.show();
                    case "3" -> exhibitionMenu.show();
                    case "4" -> reviewMenu.show();
                    case "5" -> ticketMenu.show();
                    case "6" -> multiThreadProcessing();
                    case "0" -> logout();
                    default -> System.out.println("Unknown command.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
                console.pause();
            }
        }
    }

    private void showProfile() {
        User user = session.getCurrentUser();
        System.out.println();
        System.out.println("My profile");
        System.out.println("ID: " + user.getId());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role: " + user.getRole());
        System.out.println("Tickets: " + user.getTickets().size());
        System.out.println("Reviews: " + user.getReviews().size());
        console.pause();
    }

    private void multiThreadProcessing() {
        System.out.println();
        System.out.println("MULTITHREAD PROCESSING");
        System.out.println("1. Process all exhibits (1 thread per exhibit)");
        System.out.println("2. Process with pool of 3 threads");
        System.out.println("0. Back");

        String choice = console.readString("Select mode: ");

        try {
            switch (choice) {
                case "1" -> {
                    List<Exhibit> exhibits = exhibitService.showAllExhibits();
                    multiThreadService.processExhibitsInParallel(exhibits);
                    console.pause();
                }
                case "2" -> {
                    List<Exhibit> exhibits = exhibitService.showAllExhibits();
                    multiThreadService.processExhibitsWithLimitedThreads(exhibits, 3);
                    console.pause();
                }
                case "0" -> { return; }
                default -> System.out.println("Unknown command.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
            console.pause();
        }
    }

    private void logout() {
        String answer = console.readString("Logout? (y/n): ");
        if (answer.equalsIgnoreCase("y")) {
            session.logout();
            System.out.println("You have logged out.");
        }
    }
}