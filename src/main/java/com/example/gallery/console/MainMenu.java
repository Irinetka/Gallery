package com.example.gallery.console;

import com.example.gallery.models.User;
import com.example.gallery.service.AuthorService;
import com.example.gallery.service.CategoryService;
import com.example.gallery.service.ExhibitService;
import com.example.gallery.service.ExhibitionService;
import com.example.gallery.service.ReviewService;
import com.example.gallery.service.TicketService;
import com.example.gallery.service.UserService;
import com.example.gallery.exception.*;

public class MainMenu {
    private final Session session;
    private final ConsoleUtils console;
    //private final UserService userService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final ExhibitService exhibitService;
    private final ExhibitionService exhibitionService;
    private final ReviewService reviewService;
    private final TicketService ticketService;

    public MainMenu(Session session, ConsoleUtils console,
                    //UserService userService,
                    AuthorService authorService,
                    CategoryService categoryService, ExhibitService exhibitService,
                    ExhibitionService exhibitionService, ReviewService reviewService,
                    TicketService ticketService) {
        this.session = session;
        this.console = console;
        //this.userService = userService;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.exhibitService = exhibitService;
        this.exhibitionService = exhibitionService;
        this.reviewService = reviewService;
        this.ticketService = ticketService;
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
                    case "0" -> logout();
                    default -> System.out.println("Unknown command.");
                }
            } catch (ValidationException e) {
                System.out.println("DATA ERROR: " + e.getMessage());
                System.out.println("How to fix: " + e.getHint());
                console.pause();
            } catch (EntityNotFoundException e) {
                System.out.println("NOT FOUND: " + e.getMessage());
                System.out.println("How to fix: " + e.getHint());
                console.pause();
            } catch (DuplicateEntityException e) {
                System.out.println("DUPLICATE: " + e.getMessage());
                System.out.println("How to fix: " + e.getHint());
                console.pause();
            } catch (BusinessRuleException e) {
                System.out.println("RULE VIOLATION: " + e.getMessage());
                System.out.println("How to fix: " + e.getHint());
                console.pause();
            } catch (InvalidCredentialsException e) {
                System.out.println("LOGIN FAILED: " + e.getMessage());
                System.out.println("How to fix: " + e.getHint());
                console.pause();
            } catch (AccessDeniedException e) {
                System.out.println("ACCESS DENIED: " + e.getMessage());
                System.out.println("How to fix: " + e.getHint());
                console.pause();
            } catch (IllegalArgumentException | IllegalStateException e) {
                // Фолбэк для не-собственных исключений (например, DateTimeParseException при вводе даты)
                System.out.println("Unexpected error: " + e.getMessage());
                System.out.println("How to fix: check the input format and try again.");
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

    private void logout() {
        String answer = console.readString("Logout? (y/n): ");
        if (answer.equalsIgnoreCase("y")) {
            session.logout();
            System.out.println("You have logged out.");
        }
    }
}