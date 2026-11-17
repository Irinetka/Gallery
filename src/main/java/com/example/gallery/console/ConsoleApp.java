package com.example.gallery.console;

import com.example.gallery.GalleryApplication;
import com.example.gallery.models.*;
import com.example.gallery.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

public class ConsoleApp {

    private final UserService userService;
    private final AuthService authService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final ExhibitService exhibitService;
    private final ExhibitionService exhibitionService;
    private final ReviewService reviewService;
    private final TicketService ticketService;
    private final MultiThreadService multiThreadService;

    public ConsoleApp(UserService userService, AuthService authService,
                      AuthorService authorService, CategoryService categoryService,
                      ExhibitService exhibitService, ExhibitionService exhibitionService,
                      ReviewService reviewService, TicketService ticketService,
                      MultiThreadService multiThreadService) {
        this.userService = userService;
        this.authService = authService;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.exhibitService = exhibitService;
        this.exhibitionService = exhibitionService;
        this.reviewService = reviewService;
        this.ticketService = ticketService;
        this.multiThreadService = multiThreadService;
    }

    public void start() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        ConsoleUtils console = new ConsoleUtils(scanner);

        initializeData();

        Session session = new Session();
        AuthMenu authMenu = new AuthMenu(authService, session, console);
        MainMenu mainMenu = new MainMenu(session, console,
                authorService, categoryService,
                exhibitService, exhibitionService, reviewService, ticketService);

        while (true) {
            authMenu.show();
            if (!session.isAuthenticated()) {
                break;
            }
            mainMenu.show();
        }
        scanner.close();
    }

    private void initializeData() {
        // Категории
        if (categoryService.showCategories().isEmpty()) {
            categoryService.createCategory(new Category(1L, "Painting"));
            categoryService.createCategory(new Category(2L, "Original graphics"));
            categoryService.createCategory(new Category(3L, "Sculpture"));
            categoryService.createCategory(new Category(4L, "Decorative art"));
            categoryService.createCategory(new Category(5L, "Icon painting"));
        }

        // Авторы
        if (authorService.showAuthors().isEmpty()) {
            authorService.createAuthor(new Author(1L, "Aivazovsky Ivan"));
            authorService.createAuthor(new Author(2L, "Vasnetsov Viktor"));
            authorService.createAuthor(new Author(3L, "Ge Nikolay"));
            authorService.createAuthor(new Author(4L, "Repin Ilya"));
            authorService.createAuthor(new Author(5L, "Shishkin Ivan"));
        }

        // Выставки — используем конструктор БЕЗ ID
        if (exhibitionService.showAllExhibitions().isEmpty()) {
            exhibitionService.createExhibition(new Exhibition(
                    "Lavrushinsky lane: masterpieces of Russian art",
                    ExhibitionType.PERMANENT,
                    "Tretyakov Gallery",
                    null, null));

            exhibitionService.createExhibition(new Exhibition(
                    "New Tretyakov: 20th century art",
                    ExhibitionType.PERMANENT,
                    "New Tretyakov",
                    null, null));

            exhibitionService.createExhibition(new Exhibition(
                    "Ivan the Terrible and his son Ivan. Return",
                    ExhibitionType.TEMPORARY,
                    "Tretyakov Gallery",
                    LocalDate.of(2024, 12, 17),
                    LocalDate.of(2026, 2, 15)));

            exhibitionService.createExhibition(new Exhibition(
                    "Karl Bryullov. Rome - Moscow - Petersburg",
                    ExhibitionType.TEMPORARY,
                    "New Tretyakov",
                    LocalDate.of(2025, 6, 10),
                    LocalDate.of(2026, 1, 18)));
        }

        // Экспонаты
        if (exhibitService.showAllExhibits().isEmpty()) {
            Author aivazovsky = authorService.getAuthor(1L).orElseThrow();
            Category painting = categoryService.getCategory(1L).orElseThrow();

            exhibitService.createExhibit(new Exhibit(1L, "The Ninth Wave", "INV-001", 1850,
                    "221x332", "Oil on canvas", "Painting", "Hall 1", aivazovsky, painting));

            exhibitService.createExhibit(new Exhibit(2L, "Black Sea", "INV-002", 1881,
                    "150x200", "Oil on canvas", "Painting", "Hall 2", aivazovsky, painting));

            Author vasnetsov = authorService.getAuthor(2L).orElseThrow();
            exhibitService.createExhibit(new Exhibit(3L, "Bogatyrs", "INV-003", 1898,
                    "295x446", "Oil on canvas", "Painting", "Hall 3", vasnetsov, painting));
        }

        System.out.println("Test data initialized successfully.");
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(GalleryApplication.class, args);

        ConsoleApp app = new ConsoleApp(
                context.getBean(UserService.class),
                context.getBean(AuthService.class),
                context.getBean(AuthorService.class),
                context.getBean(CategoryService.class),
                context.getBean(ExhibitService.class),
                context.getBean(ExhibitionService.class),
                context.getBean(ReviewService.class),
                context.getBean(TicketService.class),
                context.getBean(MultiThreadService.class)
        );
        app.start();
    }
}