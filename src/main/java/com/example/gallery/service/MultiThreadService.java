package com.example.gallery.service;

import com.example.gallery.exception.ValidationException;
import com.example.gallery.models.Exhibit;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class MultiThreadService {

    /**
     * Многопоточный поиск экспонатов по названию.
     * Использует пул из 4 потоков.
     */
    public List<Exhibit> searchExhibitsInParallel(List<Exhibit> exhibits, String query) {
        if (exhibits == null) {
            throw new ValidationException("Exhibits list cannot be null",
                    "Provide a non-null list of exhibits");
        }
        if (query == null || query.isBlank()) {
            throw new ValidationException("Search query cannot be empty",
                    "Enter a non-blank search string, e.g. 'wave'");
        }

        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            List<Callable<List<Exhibit>>> tasks = exhibits.stream()
                    .map(e -> (Callable<List<Exhibit>>) () -> {
                        if (e.getTitle().toLowerCase().contains(query.toLowerCase()))
                            return List.of(e);
                        return List.of();
                    }).collect(Collectors.toList());

            List<Future<List<Exhibit>>> futures = executor.invokeAll(tasks);
            return futures.stream()
                    .map(f -> {
                        try {
                            return f.get();
                        } catch (Exception ex) {
                            return List.<Exhibit>of();
                        }
                    })
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread was interrupted: " + e.getMessage()
                    + " Hint: the operation was cancelled, try again.");
            return List.of();
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Демонстрация многопоточной обработки экспонатов.
     * Потоки именуются GalleryWorker-1, GalleryWorker-2, ...
     * При пустом списке выводит "No exhibits to process".
     */
    public void processExhibitsInParallel(List<Exhibit> exhibits) {
        if (exhibits == null || exhibits.isEmpty()) {
            System.out.println("No exhibits to process");
            return;
        }

        ThreadFactory galleryFactory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("GalleryWorker-" + counter.getAndIncrement());
                return t;
            }
        };

        ExecutorService executor = Executors.newCachedThreadPool(galleryFactory);
        try {
            List<Callable<Void>> tasks = exhibits.stream()
                    .map(e -> (Callable<Void>) () -> {
                        System.out.println("Processing exhibit: " + e.getTitle()
                                + " in thread: " + Thread.currentThread().getName());
                        Thread.sleep(100);
                        return null;
                    }).collect(Collectors.toList());

            executor.invokeAll(tasks);
            System.out.println("All pool threads completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Каждое исключение обработано отдельно: прерывание потока
            System.out.println("Thread was interrupted: " + e.getMessage()
                    + " Hint: the operation was cancelled, try again.");
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Демонстрация многопоточной обработки с ограниченным пулом потоков.
     * Потоки именуются PoolWorker-1, PoolWorker-2, ...
     * @param exhibits список экспонатов
     * @param threadCount количество потоков в пуле
     */
    public void processExhibitsWithLimitedThreads(List<Exhibit> exhibits, int threadCount) {
        if (threadCount <= 0) {
            throw new ValidationException("Thread count must be positive",
                    "Pass threadCount >= 1, e.g. 2 or 4");
        }

        ThreadFactory poolFactory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("PoolWorker-" + counter.getAndIncrement());
                return t;
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(threadCount, poolFactory);
        try {
            List<Callable<Void>> tasks = exhibits.stream()
                    .map(e -> (Callable<Void>) () -> {
                        System.out.println("Processing exhibit: " + e.getTitle()
                                + " in thread: " + Thread.currentThread().getName());
                        Thread.sleep(100);
                        return null;
                    }).collect(Collectors.toList());

            executor.invokeAll(tasks);
            System.out.println("All pool threads completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Каждое исключение обработано отдельно: прерывание потока
            System.out.println("Thread was interrupted: " + e.getMessage()
                    + " Hint: the operation was cancelled, try again.");
        } finally {
            executor.shutdown();
        }
    }
}