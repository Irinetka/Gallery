package com.example.gallery.service;

import com.example.gallery.models.Author;
import com.example.gallery.models.Category;
import com.example.gallery.models.Exhibit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class MultiThreadTest {

    @Test
    void demonstrateMultithreading() {
        MultiThreadService service = new MultiThreadService();

        List<Exhibit> exhibits = new ArrayList<>();
        Author author = new Author(1L, "Test Author");
        Category category = new Category(1L, "Test Category");

        // Создаём 5 экспонатов
        for (int i = 1; i <= 5; i++) {
            exhibits.add(new Exhibit((long) i, "Exhibit " + i, "INV-" + i,
                    2020, "100x100", "Oil", "Painting", "Hall " + i,
                    author, category));
        }

        System.out.println("\n    ЗАПУСК МНОГОПОТОЧНОЙ ОБРАБОТКИ    \n");

        // Запускаем обработку — в консоли увидите перемешанные сообщения
        service.processExhibitsInParallel(exhibits);

        System.out.println("\n    ОБРАБОТКА ЗАВЕРШЕНА    \n");
    }
}