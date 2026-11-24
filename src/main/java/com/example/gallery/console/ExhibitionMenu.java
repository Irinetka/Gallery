package com.example.gallery.console;

import com.example.gallery.models.Exhibition;
import com.example.gallery.models.ExhibitionType;
import com.example.gallery.service.ExhibitionService;
import com.example.gallery.exception.*;

import java.time.LocalDate;
import java.util.List;

public class ExhibitionMenu {
    private final Session session;
    private final ConsoleUtils console;
    private final ExhibitionService exhibitionService;

    public ExhibitionMenu(Session session, ConsoleUtils console,
                          ExhibitionService exhibitionService) {
        this.session = session;
        this.console = console;
        this.exhibitionService = exhibitionService;
    }

    public void show() {
        while (session.isAuthenticated()) {
            System.out.println();
            System.out.println("EXHIBITIONS");
            System.out.println("1. Show all exhibitions");
            System.out.println("2. Permanent exhibitions");
            System.out.println("3. Temporary exhibitions");
            System.out.println("4. Create exhibition");
            System.out.println("5. Open exhibition");
            System.out.println("6. Add tag");
            System.out.println("7. Remove tag");
            System.out.println("8. Delete exhibition");
            System.out.println("0. Back");

            String choice = console.readString("Select action: ");

            try {
                switch (choice) {
                    case "1" -> listExhibitions();
                    case "2" -> permanentExhibitions();
                    case "3" -> temporaryExhibitions();
                    case "4" -> create();
                    case "5" -> showExhibition();
                    case "6" -> addTag();
                    case "7" -> removeTag();
                    case "8" -> delete();
                    case "0" -> { return; }
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

    private void listExhibitions() {
        List<Exhibition> exhibitions = exhibitionService.showAllExhibitions();
        System.out.println();
        System.out.println("All exhibitions");
        if (exhibitions.isEmpty()) {
            System.out.println("No exhibitions yet.");
        }
        for (Exhibition exhibition : exhibitions) {
            printExhibition(exhibition);
        }
        console.pause();
    }

    private void permanentExhibitions() {
        List<Exhibition> exhibitions = exhibitionService.showExhibitionsByType(ExhibitionType.PERMANENT);
        System.out.println();
        System.out.println("Permanent exhibitions");
        if (exhibitions.isEmpty()) {
            System.out.println("No permanent exhibitions.");
        }
        for (Exhibition exhibition : exhibitions) {
            printExhibition(exhibition);
        }
        console.pause();
    }

    private void temporaryExhibitions() {
        List<Exhibition> exhibitions = exhibitionService.showExhibitionsByType(ExhibitionType.TEMPORARY);
        System.out.println();
        System.out.println("Temporary exhibitions");
        if (exhibitions.isEmpty()) {
            System.out.println("No temporary exhibitions.");
        }
        for (Exhibition exhibition : exhibitions) {
            printExhibition(exhibition);
        }
        console.pause();
    }

    // ПРОВЕРКА ПРАВ: только Admin и Manager могут создавать выставки
    private void create() {
        if (!session.getCurrentUser().canManageExhibitions()) {
            System.out.println("Access denied. Only Admin and Manager can create exhibitions.");
            console.pause();
            return;
        }

        Long id = console.readLong("New exhibition ID: ");
        String title = console.readNonBlankString("Title: ");

        System.out.println("Exhibition type:");
        System.out.println("1. Permanent");
        System.out.println("2. Temporary");
        String typeChoice = console.readString("Select type: ");
        ExhibitionType type = typeChoice.equals("1") ? ExhibitionType.PERMANENT : ExhibitionType.TEMPORARY;

        String location = console.readString("Location: ");

        LocalDate startDate = null;
        LocalDate endDate = null;

        if (type == ExhibitionType.TEMPORARY) {
            String startDateStr = console.readString("Start date (YYYY-MM-DD): ");
            startDate = LocalDate.parse(startDateStr);
            String endDateStr = console.readString("End date (YYYY-MM-DD): ");
            endDate = LocalDate.parse(endDateStr);
        }

        Exhibition exhibition = new Exhibition(id, title, type, location, startDate, endDate);
        exhibitionService.createExhibition(exhibition);
        System.out.println("Exhibition created.");
        console.pause();
    }

    private void showExhibition() {
        Long id = console.readLong("Exhibition ID: ");
        Exhibition exhibition = exhibitionService.getExhibition(id)
                .orElseThrow(() -> new IllegalArgumentException("Exhibition not found"));
        System.out.println();
        System.out.println("Exhibition");
        printExhibition(exhibition);
        System.out.println("Exhibits: " + exhibition.getExhibits().size());
        System.out.println("Reviews: " + exhibition.getReviews().size());
        console.pause();
    }

    // ПРОВЕРКА ПРАВ: только Admin и Manager могут добавлять теги
    private void addTag() {
        if (!session.getCurrentUser().canManageExhibitions()) {
            System.out.println("Access denied. Only Admin and Manager can add tags.");
            console.pause();
            return;
        }

        Long id = console.readLong("Exhibition ID: ");
        String tag = console.readNonBlankString("Tag: ");
        exhibitionService.addTag(id, tag);
        System.out.println("Tag added.");
        console.pause();
    }

    // ПРОВЕРКА ПРАВ: только Admin и Manager могут удалять теги
    private void removeTag() {
        if (!session.getCurrentUser().canManageExhibitions()) {
            System.out.println("Access denied. Only Admin and Manager can remove tags.");
            console.pause();
            return;
        }

        Long id = console.readLong("Exhibition ID: ");
        String tag = console.readNonBlankString("Tag: ");
        exhibitionService.removeTag(id, tag);
        System.out.println("Tag removed.");
        console.pause();
    }

    // ПРОВЕРКА ПРАВ:только Admin и Manager могут удалять выставки
    private void delete() {
        if (!session.getCurrentUser().canManageExhibitions()) {
            System.out.println("Access denied. Only Admin and Manager can delete exhibitions.");
            console.pause();
            return;
        }

        Long id = console.readLong("Exhibition ID: ");
        exhibitionService.deleteExhibition(id);
        System.out.println("Exhibition deleted.");
        console.pause();
    }

    private void printExhibition(Exhibition exhibition) {
        System.out.println();
        System.out.println("ID: " + exhibition.getId());
        System.out.println("Title: " + exhibition.getTitle());
        System.out.println("Type: " + (exhibition.getType() == ExhibitionType.PERMANENT
                ? "Permanent" : "Temporary"));
        System.out.println("Location: " + exhibition.getLocation());
        if (exhibition.getStartDate() != null && exhibition.getEndDate() != null) {
            System.out.println("Period: " + exhibition.getStartDate() + " - " + exhibition.getEndDate());
        }
        System.out.println("Tags: " + exhibition.getTags());
    }
}