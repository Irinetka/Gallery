package com.example.gallery.service;

import com.example.gallery.models.Exhibition;
import com.example.gallery.models.ExhibitionType;
import com.example.gallery.repository.ExhibitRepository;
import com.example.gallery.repository.ExhibitionRepository;
import com.example.gallery.repository.InMemoryExhibitRepository;
import com.example.gallery.repository.InMemoryExhibitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;

class ExhibitionServiceTest {
    private ExhibitionService exhibitionService;

    @BeforeEach
    void setUp() {
        ExhibitionRepository exhibitionRepo = new InMemoryExhibitionRepository();
        ExhibitRepository exhibitRepo = new InMemoryExhibitRepository();
        exhibitionService = new ExhibitionService(exhibitionRepo, exhibitRepo);
    }

    @Test
    void testCreatePermanentExhibition() {
        Exhibition exhibition = new Exhibition(1L, "Permanent Art", ExhibitionType.PERMANENT, "Main Hall", null, null);
        Exhibition created = exhibitionService.createExhibition(exhibition);

        assertNotNull(created);
        assertEquals(ExhibitionType.PERMANENT, created.getType());
    }

    @Test
    void testCreateTemporaryExhibition() {
        Exhibition exhibition = new Exhibition(2L, "Temporary Show", ExhibitionType.TEMPORARY, "Hall 2",
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        Exhibition created = exhibitionService.createExhibition(exhibition);

        assertNotNull(created);
        assertEquals(LocalDate.of(2024, 1, 1), created.getStartDate());
    }

    @Test
    void testAddAndRemoveTag() {
        Exhibition exhibition = new Exhibition(1L, "Tagged Exhibition", ExhibitionType.PERMANENT, "Hall 1", null, null);
        exhibitionService.createExhibition(exhibition);

        exhibitionService.addTag(1L, "modern");
        exhibitionService.addTag(1L, "art");

        assertTrue(exhibitionService.getExhibition(1L).get().hasTag("modern"));

        exhibitionService.removeTag(1L, "modern");
        assertFalse(exhibitionService.getExhibition(1L).get().hasTag("modern"));
    }

    @Test
    void testFilterByType() {
        exhibitionService.createExhibition(new Exhibition(1L, "Perm 1", ExhibitionType.PERMANENT, "Hall", null, null));
        exhibitionService.createExhibition(new Exhibition(2L, "Temp 1", ExhibitionType.TEMPORARY, "Hall", LocalDate.now(), LocalDate.now().plusMonths(1)));

        List<Exhibition> permanent = exhibitionService.showExhibitionsByType(ExhibitionType.PERMANENT);
        assertEquals(1, permanent.size());
    }
}