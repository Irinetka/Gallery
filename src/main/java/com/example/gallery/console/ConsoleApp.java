package com.example.gallery.console;

import com.example.gallery.models.*;
import com.example.gallery.repository.AuthorRepository;
import com.example.gallery.repository.CategoryRepository;
import com.example.gallery.repository.ExhibitRepository;
import com.example.gallery.repository.ExhibitionRepository;
import com.example.gallery.repository.InMemoryAuthorRepository;
import com.example.gallery.repository.InMemoryCategoryRepository;
import com.example.gallery.repository.InMemoryExhibitRepository;
import com.example.gallery.repository.InMemoryExhibitionRepository;
import com.example.gallery.repository.InMemoryReviewRepository;
import com.example.gallery.repository.InMemoryTicketRepository;
import com.example.gallery.repository.InMemoryUserRepository;
import com.example.gallery.repository.ReviewRepository;
import com.example.gallery.repository.TicketRepository;
import com.example.gallery.repository.UserRepository;
import com.example.gallery.service.AuthService;
import com.example.gallery.service.AuthorService;
import com.example.gallery.service.CategoryService;
import com.example.gallery.service.ExhibitService;
import com.example.gallery.service.ExhibitionService;
import com.example.gallery.service.MultiThreadService;
import com.example.gallery.service.ReviewService;
import com.example.gallery.service.TicketService;
import com.example.gallery.service.UserService;
import java.util.Scanner;

public class ConsoleApp {
    public void start() {
        Scanner scanner = new Scanner(System.in);
        ConsoleUtils console = new ConsoleUtils(scanner);

        UserRepository userRepository = new InMemoryUserRepository();
        AuthorRepository authorRepository = new InMemoryAuthorRepository();
        CategoryRepository categoryRepository = new InMemoryCategoryRepository();
        ExhibitRepository exhibitRepository = new InMemoryExhibitRepository();
        ExhibitionRepository exhibitionRepository = new InMemoryExhibitionRepository();
        ReviewRepository reviewRepository = new InMemoryReviewRepository();
        TicketRepository ticketRepository = new InMemoryTicketRepository();

        UserService userService = new UserService(userRepository);
        AuthService authService = new AuthService(userService, userRepository);
        AuthorService authorService = new AuthorService(authorRepository);
        CategoryService categoryService = new CategoryService(categoryRepository);
        MultiThreadService multiThreadService = new MultiThreadService();

        ExhibitService exhibitService = new ExhibitService(
                exhibitRepository, authorRepository, categoryRepository, multiThreadService);

        ExhibitionService exhibitionService = new ExhibitionService(exhibitionRepository, exhibitRepository);
        ReviewService reviewService = new ReviewService(reviewRepository, userRepository, exhibitionRepository);
        TicketService ticketService = new TicketService(ticketRepository, userRepository, exhibitionRepository);

        initializeData(authorService, categoryService, exhibitionService, exhibitService);

        Session session = new Session();
        AuthMenu authMenu = new AuthMenu(authService, session, console);
        MainMenu mainMenu = new MainMenu(session, console, //userService,
                authorService,
                categoryService, exhibitService, exhibitionService, reviewService,
                ticketService);

        while (true) {
            authMenu.show();
            if (!session.isAuthenticated()) {
                break;
            }
            mainMenu.show();
        }
        scanner.close();
    }

    private void initializeData(AuthorService authorService, CategoryService categoryService,
                                ExhibitionService exhibitionService, ExhibitService exhibitService) {
        categoryService.createCategory(new Category(1L, "Painting"));
        categoryService.createCategory(new Category(2L, "Original graphics"));
        categoryService.createCategory(new Category(3L, "Sculpture"));
        categoryService.createCategory(new Category(4L, "Decorative art"));
        categoryService.createCategory(new Category(5L, "Icon painting"));

        authorService.createAuthor(new Author(1L, "Aivazovsky Ivan"));
        authorService.createAuthor(new Author(2L, "Vasnetsov Viktor"));
        authorService.createAuthor(new Author(3L, "Ge Nikolay"));
        authorService.createAuthor(new Author(4L, "Repin Ilya"));
        authorService.createAuthor(new Author(5L, "Shishkin Ivan"));

        exhibitionService.createExhibition(new Exhibition(
                1L, "Lavrushinsky lane: masterpieces of Russian art",
                ExhibitionType.PERMANENT, "Tretyakov Gallery", null, null));

        exhibitionService.createExhibition(new Exhibition(
                2L, "New Tretyakov: 20th century art",
                ExhibitionType.PERMANENT, "New Tretyakov", null, null));

        exhibitionService.createExhibition(new Exhibition(
                3L, "Ivan the Terrible and his son Ivan. Return",
                ExhibitionType.TEMPORARY, "Tretyakov Gallery",
                java.time.LocalDate.of(2024, 12, 17),
                java.time.LocalDate.of(2026, 2, 15)));

        exhibitionService.createExhibition(new Exhibition(
                4L, "Karl Bryullov. Rome - Moscow - Petersburg",
                ExhibitionType.TEMPORARY, "New Tretyakov",
                java.time.LocalDate.of(2025, 6, 10),
                java.time.LocalDate.of(2026, 1, 18)));

        Author aivazovsky = authorService.getAuthor(1L).orElseThrow();
        Category painting = categoryService.getCategory(1L).orElseThrow();

        exhibitService.createExhibit(new Exhibit(1L, "The Ninth Wave", "INV-001", 1850,
                "221x332", "Oil on canvas", "Painting", "Hall 1", aivazovsky, painting));
        exhibitService.createExhibit(new Exhibit(2L, "Black Sea", "INV-002", 1881,
                "150x200", "Oil on canvas", "Painting", "Hall 2", aivazovsky, painting));
        exhibitService.createExhibit(new Exhibit(3L, "Bogatyrs", "INV-003", 1898,
                "295x446", "Oil on canvas", "Painting", "Hall 3",
                authorService.getAuthor(2L).orElseThrow(), painting));
    }
}