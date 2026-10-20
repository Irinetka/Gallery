package com.example.gallery.console;

import com.example.gallery.models.Author;
import com.example.gallery.models.Category;
import com.example.gallery.models.Exhibit;
import com.example.gallery.service.AuthorService;
import com.example.gallery.service.CategoryService;
import com.example.gallery.service.ExhibitService;
import java.util.List;

public class ExhibitMenu {
    private final Session session;
    private final ConsoleUtils console;
    private final ExhibitService exhibitService;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public ExhibitMenu(Session session, ConsoleUtils console,
                       ExhibitService exhibitService, AuthorService authorService,
                       CategoryService categoryService) {
        this.session = session;
        this.console = console;
        this.exhibitService = exhibitService;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    public void show() {
        while (session.isAuthenticated()) {
            System.out.println();
            System.out.println("EXHIBITS");
            System.out.println("1. Show all exhibits");
            System.out.println("2. Search by title");
            System.out.println("3. Filter by author");
            System.out.println("4. Filter by category");
            System.out.println("5. Filter by year range");
            System.out.println("6. Create exhibit");
            System.out.println("7. Open exhibit");
            System.out.println("8. Delete exhibit");
            System.out.println("0. Back");

            String choice = console.readString("Select action: ");

            try {
                switch (choice) {
                    case "1" -> listExhibits();
                    case "2" -> search();
                    case "3" -> filterByAuthor();
                    case "4" -> filterByCategory();
                    case "5" -> filterByYear();
                    case "6" -> create();
                    case "7" -> showExhibit();
                    case "8" -> delete();
                    case "0" -> { return; }
                    default -> System.out.println("Unknown command.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
                console.pause();
            }
        }
    }

    private void listExhibits() {
        List<Exhibit> exhibits = exhibitService.showAllExhibits();
        System.out.println();
        System.out.println("All exhibits");
        if (exhibits.isEmpty()) {
            System.out.println("No exhibits yet.");
        }
        for (Exhibit exhibit : exhibits) {
            printExhibit(exhibit);
        }
        console.pause();
    }

    private void search() {
        String query = console.readString("Search query: ");
        List<Exhibit> exhibits = exhibitService.searchExhibits(query);
        System.out.println();
        System.out.println("Search results");
        if (exhibits.isEmpty()) {
            System.out.println("Nothing found.");
        }
        for (Exhibit exhibit : exhibits) {
            printExhibit(exhibit);
        }
        console.pause();
    }

    private void filterByAuthor() {
        List<Author> authors = authorService.showAuthors();
        System.out.println();
        System.out.println("Authors");
        for (Author author : authors) {
            System.out.println("ID: " + author.getId() + " - " + author.getFullName());
        }
        Long authorId = console.readLong("Author ID: ");
        List<Exhibit> exhibits = exhibitService.showExhibitsByAuthor(authorId);
        System.out.println();
        System.out.println("Exhibits by author");
        if (exhibits.isEmpty()) {
            System.out.println("No exhibits found.");
        }
        for (Exhibit exhibit : exhibits) {
            printExhibit(exhibit);
        }
        console.pause();
    }

    private void filterByCategory() {
        List<Category> categories = categoryService.showCategories();
        System.out.println();
        System.out.println("Categories");
        for (Category category : categories) {
            System.out.println("ID: " + category.getId() + " - " + category.getName());
        }
        Long categoryId = console.readLong("Category ID: ");
        List<Exhibit> exhibits = exhibitService.showExhibitsByCategory(categoryId);
        System.out.println();
        System.out.println("Exhibits by category");
        if (exhibits.isEmpty()) {
            System.out.println("No exhibits found.");
        }
        for (Exhibit exhibit : exhibits) {
            printExhibit(exhibit);
        }
        console.pause();
    }

    private void filterByYear() {
        Integer startYear = console.readInteger("Start year: ");
        Integer endYear = console.readInteger("End year: ");
        List<Exhibit> exhibits = exhibitService.showExhibitsByYearRange(startYear, endYear);
        System.out.println();
        System.out.println("Exhibits from " + startYear + " to " + endYear);
        if (exhibits.isEmpty()) {
            System.out.println("No exhibits found.");
        }
        for (Exhibit exhibit : exhibits) {
            printExhibit(exhibit);
        }
        console.pause();
    }

    private void create() {
        Long id = console.readLong("New exhibit ID: ");
        String title = console.readNonBlankString("Title: ");
        String inventoryNumber = console.readString("Inventory number: ");
        Integer year = console.readInteger("Year: ");
        String size = console.readString("Size: ");
        String material = console.readString("Material: ");
        String technique = console.readString("Technique: ");
        String location = console.readString("Location: ");

        List<Author> authors = authorService.showAuthors();
        System.out.println();
        System.out.println("Authors");
        for (Author author : authors) {
            System.out.println("ID: " + author.getId() + " - " + author.getFullName());
        }
        Long authorId = console.readLong("Author ID: ");
        Author author = authorService.getAuthor(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        List<Category> categories = categoryService.showCategories();
        System.out.println();
        System.out.println("Categories");
        for (Category category : categories) {
            System.out.println("ID: " + category.getId() + " - " + category.getName());
        }
        Long categoryId = console.readLong("Category ID: ");
        Category category = categoryService.getCategory(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Exhibit exhibit = new Exhibit(id, title, inventoryNumber, year, size,
                material, technique, location, author, category);
        exhibitService.createExhibit(exhibit);
        System.out.println("Exhibit created.");
        console.pause();
    }

    private void showExhibit() {
        Long id = console.readLong("Exhibit ID: ");
        Exhibit exhibit = exhibitService.getExhibit(id)
                .orElseThrow(() -> new IllegalArgumentException("Exhibit not found"));
        System.out.println();
        System.out.println("Exhibit");
        printExhibit(exhibit);
        System.out.println("Reviews: " + exhibit.getReviews().size());
        console.pause();
    }

    private void delete() {
        Long id = console.readLong("Exhibit ID: ");
        exhibitService.deleteExhibit(id);
        System.out.println("Exhibit deleted.");
        console.pause();
    }

    private void printExhibit(Exhibit exhibit) {
        System.out.println();
        System.out.println("ID: " + exhibit.getId());
        System.out.println("Title: " + exhibit.getTitle());
        System.out.println("Inventory number: " + exhibit.getInventoryNumber());
        System.out.println("Year: " + exhibit.getYear());
        System.out.println("Size: " + exhibit.getSize());
        System.out.println("Material: " + exhibit.getMaterial());
        System.out.println("Technique: " + exhibit.getTechnique());
        System.out.println("Location: " + exhibit.getLocation());
        System.out.println("Author: " + (exhibit.getAuthor() != null ? exhibit.getAuthor().getFullName() : "Unknown"));
        System.out.println("Category: " + (exhibit.getCategory() != null ? exhibit.getCategory().getName() : "Not specified"));
    }
}