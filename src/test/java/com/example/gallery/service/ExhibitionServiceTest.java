package com.example.gallery.service;

import com.example.gallery.models.Exhibition;
import com.example.gallery.models.ExhibitionType;
import com.example.gallery.repository.ExhibitRepository;
import com.example.gallery.repository.ExhibitionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тесты для ExhibitionService.
 * Использует @SpringBootTest + Mockito (без подключения к БД).
 */
@SpringBootTest
class ExhibitionServiceTest {

    @Mock
    private ExhibitionRepository exhibitionRepository;

    @Mock
    private ExhibitRepository exhibitRepository;

    @InjectMocks
    private ExhibitionService exhibitionService;

    @Test
    void testCreatePermanentExhibition() {
        Exhibition exhibition = new Exhibition(1L, "Permanent Art", ExhibitionType.PERMANENT,
                "Main Hall", null, null);
        when(exhibitionRepository.findById(1L)).thenReturn(Optional.empty());
        when(exhibitionRepository.save(exhibition)).thenReturn(exhibition);

        Exhibition created = exhibitionService.createExhibition(exhibition);

        assertNotNull(created);
        assertEquals(ExhibitionType.PERMANENT, created.getType());
    }

    @Test
    void testCreateTemporaryExhibition() {
        Exhibition exhibition = new Exhibition(2L, "Temporary Show", ExhibitionType.TEMPORARY,
                "Hall 2", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        when(exhibitionRepository.findById(2L)).thenReturn(Optional.empty());
        when(exhibitionRepository.save(exhibition)).thenReturn(exhibition);

        Exhibition created = exhibitionService.createExhibition(exhibition);

        assertNotNull(created);
        assertEquals(LocalDate.of(2024, 1, 1), created.getStartDate());
    }

    @Test
    void testFilterByType() {
        when(exhibitionRepository.findByType(ExhibitionType.PERMANENT))
                .thenReturn(List.of(new Exhibition(1L, "Perm 1", ExhibitionType.PERMANENT,
                        "Hall", null, null)));
        when(exhibitionRepository.findByType(ExhibitionType.TEMPORARY))
                .thenReturn(List.of(new Exhibition(2L, "Temp 1", ExhibitionType.TEMPORARY,
                        "Hall", LocalDate.now(), LocalDate.now().plusMonths(1))));

        List<Exhibition> permanent = exhibitionService.showExhibitionsByType(ExhibitionType.PERMANENT);

        assertEquals(1, permanent.size());
    }
}