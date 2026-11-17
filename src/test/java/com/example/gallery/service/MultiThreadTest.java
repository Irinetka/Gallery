package com.example.gallery.service;

import com.example.gallery.models.Author;
import com.example.gallery.models.Category;
import com.example.gallery.models.Exhibit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MultiThreadTest {

    @Autowired
    private MultiThreadService multiThreadService;

    private List<Exhibit> createExhibits(int count) {
        List<Exhibit> exhibits = new ArrayList<>();
        Author author = new Author(1L, "Test Author");
        Category category = new Category(1L, "Test Category");

        for (int i = 1; i <= count; i++) {
            exhibits.add(new Exhibit((long) i, "Exhibit " + i, "INV-" + i,
                    2020, "100x100", "Oil", "Painting", "Hall " + i,
                    author, category));
        }
        return exhibits;
    }

    @Test
    @DisplayName("Проверка имён потоков GalleryWorker-1..5 в логе")
    void testThreadNamesInLog() {
        List<Exhibit> exhibits = createExhibits(5);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(buffer));
            multiThreadService.processExhibitsInParallel(exhibits);
        } finally {
            System.setOut(originalOut);
        }

        String log = buffer.toString();

        assertTrue(log.contains("GalleryWorker-1"));
        assertTrue(log.contains("GalleryWorker-2"));
        assertTrue(log.contains("GalleryWorker-3"));
        assertTrue(log.contains("GalleryWorker-4"));
        assertTrue(log.contains("GalleryWorker-5"));
    }

    @Test
    @DisplayName("Обработка пустой коллекции экспонатов")
    void testEmptyCollection() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(buffer));
            multiThreadService.processExhibitsInParallel(new ArrayList<>());
        } finally {
            System.setOut(originalOut);
        }

        assertTrue(buffer.toString().contains("No exhibits to process"));
    }

    @Test
    @DisplayName("Пул с неположительным числом потоков")
    void testInvalidThreadCount() {
        assertThrows(IllegalArgumentException.class, () ->
                multiThreadService.processExhibitsWithLimitedThreads(createExhibits(1), 0));
        assertThrows(IllegalArgumentException.class, () ->
                multiThreadService.processExhibitsWithLimitedThreads(createExhibits(1), -1));
    }

    @Test
    @DisplayName("Параллельная обработка 5 экспонатов в потоках")
    void testParallelProcessing() {
        List<Exhibit> exhibits = createExhibits(5);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(buffer));
            multiThreadService.processExhibitsInParallel(exhibits);
        } finally {
            System.setOut(originalOut);
        }

        String log = buffer.toString();
        assertTrue(log.contains("All pool threads completed"));
        assertEquals(5, log.split("Processing exhibit").length - 1);
    }

    @Test
    @DisplayName("Обработка пулом из 2 потоков PoolWorker-N")
    void testLimitedThreadPool() {
        List<Exhibit> exhibits = createExhibits(5);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(buffer));
            multiThreadService.processExhibitsWithLimitedThreads(exhibits, 2);
        } finally {
            System.setOut(originalOut);
        }

        String log = buffer.toString();
        assertTrue(log.contains("PoolWorker-1"));
        assertTrue(log.contains("PoolWorker-2"));
        assertFalse(log.contains("PoolWorker-3"), "Третий поток создаваться не должен");
        assertTrue(log.contains("All pool threads completed"));
    }
}