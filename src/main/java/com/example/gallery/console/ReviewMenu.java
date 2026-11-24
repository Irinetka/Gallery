package com.example.gallery.console;

import com.example.gallery.models.Exhibition;
import com.example.gallery.models.Review;
import com.example.gallery.service.ExhibitionService;
import com.example.gallery.service.ReviewService;
import com.example.gallery.exception.*;

import java.util.List;

public class ReviewMenu {
    private final Session session;
    private final ConsoleUtils console;
    private final ReviewService reviewService;
    private final ExhibitionService exhibitionService;

    public ReviewMenu(Session session, ConsoleUtils console,
                      ReviewService reviewService, ExhibitionService exhibitionService) {
        this.session = session;
        this.console = console;
        this.reviewService = reviewService;
        this.exhibitionService = exhibitionService;
    }

    public void show() {
        while (session.isAuthenticated()) {
            System.out.println();
            System.out.println("REVIEWS");
            System.out.println("1. Show my reviews");
            System.out.println("2. Show exhibition reviews");
            System.out.println("3. Create review");
            System.out.println("4. Delete review");
            System.out.println("0. Back");

            String choice = console.readString("Select action: ");

            try {
                switch (choice) {
                    case "1" -> myReviews();
                    case "2" -> exhibitionReviews();
                    case "3" -> create();
                    case "4" -> delete();
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

    private void myReviews() {
        List<Review> reviews = reviewService.showVisitorReviews(session.getCurrentUser().getId());
        printReviews(reviews);
        console.pause();
    }

    private void exhibitionReviews() {
        Long exhibitionId = console.readLong("Exhibition ID: ");
        List<Review> reviews = reviewService.showExhibitionReviews(exhibitionId);
        printReviews(reviews);
        console.pause();
    }

    private void create() {
        Long exhibitionId = console.readLong("Exhibition ID: ");
        Exhibition exhibition = exhibitionService.getExhibition(exhibitionId)
                .orElseThrow(() -> new IllegalArgumentException("Exhibition not found"));

        Long id = console.readLong("Review ID: ");
        String content = console.readNonBlankString("Content: ");
        Integer rating = console.readIntegerInRange("Rating (1-5): ", 1, 5);

        Review review = new Review(id, session.getCurrentUser(), exhibition, content, rating);
        reviewService.createReview(review);
        System.out.println("Review added.");
        console.pause();
    }

    // ПРОВЕРКА: пользователь может удалять только свои отзывы
    private void delete() {
        Long id = console.readLong("Review ID: ");
        Review review = reviewService.getReview(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (!review.getVisitor().getId().equals(session.getCurrentUser().getId())) {
            throw new IllegalStateException("You can only delete your own reviews.");
        }

        reviewService.deleteReview(id);
        System.out.println("Review deleted.");
        console.pause();
    }

    private void printReviews(List<Review> reviews) {
        System.out.println();
        System.out.println("Reviews");
        if (reviews.isEmpty()) {
            System.out.println("No reviews.");
            return;
        }
        for (Review review : reviews) {
            printReview(review);
        }
    }

    private void printReview(Review review) {
        System.out.println();
        System.out.println("ID: " + review.getId());
        System.out.println("Author: " + review.getVisitor().getUsername());
        System.out.println("Exhibition: " + review.getExhibition().getTitle());
        System.out.println("Rating: " + review.getRating() + "/5");
        System.out.println("Content: " + review.getContent());
        System.out.println("Date: " + review.getCreatedAt());
    }
}