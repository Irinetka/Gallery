package com.example.gallery.service;

import com.example.gallery.models.Author;
import com.example.gallery.models.Category;
import com.example.gallery.models.Exhibit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Демонстрационный тест многопоточной обработки коллекции экспонатов.
 *
 * Задача теста — подтвердить выполнение пункта задания:
 * «показать в логе, что действия действительно происходят в разных потоках,
 *  дать потокам имена».
 *
 * В тесте создаются 5 экспонатов и запускается MultiThreadService:
 *  - processExhibitsInParallel()      — по одному именованному потоку на экспонат (GalleryWorker-N);
 *  - processExhibitsWithLimitedThreads() — фиксированный пул потоков (PoolWorker-N).
 *
 * Сообщения в консоли выводятся перемешанно, что параллельность выполнения;
 * синхронизация с основным потоком выполняется методом join().
 */
@DisplayName("Демонстрация многопоточной обработки")
class MultiThreadTest {

    /**
     * Вспомогательный метод: создаёт список из count экспонатов
     * с общим автором и категорией (для теста важны id и название).
     */
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

    /**
     * Основной демонстрационный тест: 5 экспонатов -> 5 потоков GalleryWorker-1..5.
     *
     * В логе видно, что операции "Loading metadata", "Analyzing author",
     * "Checking category" от разных потоков чередуются, а суммарное время
     * обработки меньше суммы задержек — признак параллельного выполнения.
     */
    @Test
    @DisplayName("Параллельная обработка 5 экспонатов в потоках GalleryWorker-N")
    void demonstrateMultithreading() {
        MultiThreadService service = new MultiThreadService();
        List<Exhibit> exhibits = createExhibits(5); // создаём 5 экспонатов

        System.out.println("\n=== ЗАПУСК МНОГОПОТОЧНОЙ ОБРАБОТКИ ===\n");

        // Запускаем обработку — в консоли появятся перемешанные сообщения разных потоков.
        // assertDoesNotThrow подтверждает, что обработка завершилась без ошибок,
        // а основной поток дождался всех рабочих потоков через join().
        assertDoesNotThrow(() -> service.processExhibitsInParallel(exhibits));

        System.out.println("\n=== ОБРАБОТКА ЗАВЕРШЕНА ===\n");
    }

    /**
     * Проверка имён потоков по перехваченному логу.
     *
     * System.out временно подменяется на ByteArrayOutputStream, чтобы
     * программно убедиться: в логе присутствуют имена всех пяти потоков
     * GalleryWorker-1 ... GalleryWorker-5, то есть работа действительно
     * распределена между разными потоками, а не выполнена одним.
     */
    @Test
    @DisplayName("Проверка имён потоков GalleryWorker-1..5 в логе")
    void testThreadNamesInLog() {
        MultiThreadService service = new MultiThreadService();
        List<Exhibit> exhibits = createExhibits(5);

        PrintStream originalOut = System.out;                 // сохраняем исходный поток вывода
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(buffer));           // перехватываем лог
            service.processExhibitsInParallel(exhibits);
        } finally {
            System.setOut(originalOut);                       // обязательно возвращаем обратно
        }

        String log = buffer.toString();

        // Каждый экспонат обработан своим именованным потоком
        for (int i = 1; i <= 5; i++) {
            assertTrue(log.contains("GalleryWorker-" + i),
                    "В логе должно быть имя потока GalleryWorker-" + i);
        }
        // Итоговое сообщение печатается только после join() всех потоков
        assertTrue(log.contains("All threads completed"));
    }

    /**
     * Обработка с ограниченным пулом потоков.
     *
     * 5 экспонатов распределяются между 2 потоками (PoolWorker-1 и PoolWorker-2)
     * по схеме j += threadCount: первый поток берёт элементы 0, 2, 4,
     * второй — 1, 3. Проверяем, что создано ровно 2 потока и третий не появлялся.
     */
    @Test
    @DisplayName("Обработка пулом из 2 потоков PoolWorker-N")
    void testLimitedThreadPool() {
        MultiThreadService service = new MultiThreadService();
        List<Exhibit> exhibits = createExhibits(5);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(buffer));
            service.processExhibitsWithLimitedThreads(exhibits, 2);
        } finally {
            System.setOut(originalOut);
        }

        String log = buffer.toString();

        assertTrue(log.contains("PoolWorker-1"));
        assertTrue(log.contains("PoolWorker-2"));
        assertFalse(log.contains("PoolWorker-3"), "Третий поток создаваться не должен");
        assertTrue(log.contains("All pool threads completed"));
    }

    /**
     * Некорректный размер пула (0 потоков).
     * Ожидание: IllegalArgumentException("Thread count must be positive").
     */
    @Test
    @DisplayName("Пул с неположительным числом потоков")
    void testInvalidThreadCount() {
        MultiThreadService service = new MultiThreadService();
        List<Exhibit> exhibits = createExhibits(3);

        assertThrows(IllegalArgumentException.class,
                () -> service.processExhibitsWithLimitedThreads(exhibits, 0));
    }

    /**
     * Обработка пустой коллекции.
     * Ожидание: потоки не создаются, метод корректно завершается
     * и выводит сообщение "No exhibits to process".
     */
    @Test
    @DisplayName("Обработка пустой коллекции экспонатов")
    void testEmptyCollection() {
        MultiThreadService service = new MultiThreadService();

        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(buffer));
            service.processExhibitsInParallel(new ArrayList<>());
        } finally {
            System.setOut(originalOut);
        }

        assertTrue(buffer.toString().contains("No exhibits to process"));
    }
}