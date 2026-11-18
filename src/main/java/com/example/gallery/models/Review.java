package com.example.gallery.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User visitor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id", nullable = false)
    private Exhibition exhibition;

    @Column(name = "review_text", nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Review(Long id, User visitor, Exhibition exhibition, String content, Integer rating) {
        this.id = id;
        this.visitor = visitor;
        this.exhibition = exhibition;
        this.content = content;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
    }
}