package com.example.gallery.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exhibitions")
@Getter
@Setter
@NoArgsConstructor
public class Exhibition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ExhibitionType type;

    @Column(name = "place", nullable = false, length = 150)
    private String location;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToMany
    @JoinTable(
            name = "exhibition_exhibits",
            joinColumns = @JoinColumn(name = "exhibition_id"),
            inverseJoinColumns = @JoinColumn(name = "exhibit_id")
    )
    private Set<Exhibit> exhibits = new HashSet<>();

    @OneToMany(mappedBy = "exhibition", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "exhibition", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ticket> tickets = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "exhibition_tags", joinColumns = @JoinColumn(name = "exhibition_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    public Exhibition(Long id, String title, ExhibitionType type, String location,
                      LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Exhibition(String title, ExhibitionType type, String location,
                      LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.type = type;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addTag(String tag) { tags.add(tag.trim().toLowerCase()); }
    public void removeTag(String tag) { tags.remove(tag.trim().toLowerCase()); }
    public boolean hasTag(String tag) { return tags.contains(tag.trim().toLowerCase()); }
}