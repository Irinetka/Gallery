package com.example.gallery.service;

import com.example.gallery.models.Exhibit;

import java.util.ArrayList;
import java.util.List;

// Класс MultiThreadService — сервис многопоточной обработки коллекции экспонатов
public class MultiThreadService {

    // Метод processExhibitsInParallel — создаёт отдельный именованный поток GalleryWorker-N для каждого экспоната
    public void processExhibitsInParallel(List<Exhibit> exhibits) {
        // Проверяется пустая или null-коллекция — выводится сообщение и выполняется выход из метода
        if (exhibits == null || exhibits.isEmpty()) {
            System.out.println("No exhibits to process");
            return;
        }

        // Выводится заголовок раздела многопоточной обработки
        System.out.println();
        System.out.println("MULTITHREAD EXHIBIT PROCESSING");
        System.out.println("Total exhibits: " + exhibits.size());
        System.out.println();

        // Создаётся список потоков threads для хранения всех запущенных потоков
        List<Thread> threads = new ArrayList<>();
        // Запоминается время начала обработки в переменной startTime
        long startTime = System.currentTimeMillis();

        // В цикле для каждого экспоната создаётся отдельный поток
        for (int i = 0; i < exhibits.size(); i++) {
            // Получается текущий экспонат из списка
            final Exhibit exhibit = exhibits.get(i);
            // Запоминается индекс экспоната
            final int index = i;
            // Формируется имя потока вида "GalleryWorker-N"
            String threadName = "GalleryWorker-" + (i + 1);

            // Создаётся Runnable-задача processor — тело потока
            Runnable processor = () -> {
                // Устанавливается имя текущего потока
                Thread.currentThread().setName(threadName);
                // Выводится сообщение о начале обработки экспоната
                System.out.println("[" + Thread.currentThread().getName() + "] " +
                        "Start processing #" + exhibit.getId() + ": " + exhibit.getTitle());
                try {
                    // Вызывается метод simulate — имитация загрузки метаданных
                    simulate("Loading metadata");
                    // Вызывается метод simulate — имитация анализа автора
                    simulate("Analyzing author: " + (exhibit.getAuthor() != null
                            ? exhibit.getAuthor().getFullName() : "unknown"));
                    // Вызывается метод simulate — имитация проверки категории
                    simulate("Checking category: " + (exhibit.getCategory() != null
                            ? exhibit.getCategory().getName() : "not specified"));
                    // Выводится сообщение об успешной обработке экспоната
                    System.out.println("[" + Thread.currentThread().getName() + "] " +
                            "Exhibit #" + exhibit.getId() + " processed");
                } catch (InterruptedException e) {
                    // Обрабатывается прерывание потока — выводится сообщение
                    System.out.println("[" + Thread.currentThread().getName() + "] Interrupted");
                    // Восстанавливается флаг прерывания текущего потока
                    Thread.currentThread().interrupt();
                }
            };

            // Создаётся новый поток thread с задачей processor
            Thread thread = new Thread(processor);
            // Устанавливается имя потока
            thread.setName(threadName);
            // Поток добавляется в список threads
            threads.add(thread);
            // Запускается поток
            thread.start();
            // Выводится сообщение о запуске потока
            System.out.println("Started thread: " + threadName);
        }

        // Выводится сообщение об ожидании завершения всех потоков
        System.out.println();
        System.out.println("Waiting for all threads to complete...");
        System.out.println();

        // В цикле выполняется ожидание завершения каждого потока через join()
        for (Thread thread : threads) {
            try {
                // Вызывается метод join — основной поток ждёт завершения текущего рабочего потока
                thread.join();
            } catch (InterruptedException e) {
                // Обрабатывается прерывание ожидания — выводится сообщение
                System.out.println("Waiting interrupted");
                // Восстанавливается флаг прерывания
                Thread.currentThread().interrupt();
            }
        }

        // Вычисляется длительность обработки в миллисекундах
        long duration = System.currentTimeMillis() - startTime;
        // Выводится итоговое сообщение с длительностью
        System.out.println();
        System.out.println("All threads completed. Duration: " + duration + " ms");
        System.out.println();
    }

    // Метод processExhibitsWithLimitedThreads — обрабатывает экспонаты фиксированным пулом потоков PoolWorker-N
    public void processExhibitsWithLimitedThreads(List<Exhibit> exhibits, int threadCount) {
        // Проверяется пустая или null-коллекция — выводится сообщение и выполняется выход из метода
        if (exhibits == null || exhibits.isEmpty()) {
            System.out.println("No exhibits to process");
            return;
        }
        // Проверяется положительность размера пула — выбрасывается исключение при неположительном значении
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Thread count must be positive");
        }

        // Выводится заголовок раздела обработки пулом потоков
        System.out.println();
        System.out.println("MULTITHREAD PROCESSING WITH POOL OF " + threadCount + " THREADS");
        System.out.println("Exhibits: " + exhibits.size() + ", Threads: " + threadCount);
        System.out.println();

        // Запоминается время начала обработки
        long startTime = System.currentTimeMillis();
        // Создаётся список потоков threads
        List<Thread> threads = new ArrayList<>();

        // В цикле создаётся threadCount потоков
        for (int i = 0; i < threadCount; i++) {
            // Запоминается индекс текущего потока
            final int threadIndex = i;
            // Формируется имя потока вида "PoolWorker-N"
            String threadName = "PoolWorker-" + (i + 1);

            // Создаётся Runnable-задача batchProcessor — тело потока пула
            Runnable batchProcessor = () -> {
                // Устанавливается имя текущего потока
                Thread.currentThread().setName(threadName);
                // Выводится сообщение о запуске потока
                System.out.println("[" + Thread.currentThread().getName() + "] Thread started");
                // В цикле поток обрабатывает экспонаты с шагом threadCount (по схеме j += threadCount)
                for (int j = threadIndex; j < exhibits.size(); j += threadCount) {
                    // Получается текущий экспонат
                    Exhibit exhibit = exhibits.get(j);
                    try {
                        // Выводится сообщение о начале обработки экспоната
                        System.out.println("[" + Thread.currentThread().getName() + "] " +
                                "Processing #" + exhibit.getId() + ": " + exhibit.getTitle());
                        // Вызывается метод sleep — имитация работы со случайной задержкой 100..400 мс
                        Thread.sleep(100 + (long) (Math.random() * 300));
                        // Выводится сообщение об успешной обработке
                        System.out.println("[" + Thread.currentThread().getName() + "] " +
                                "Processed #" + exhibit.getId());
                    } catch (InterruptedException e) {
                        // Обрабатывается прерывание потока
                        System.out.println("[" + Thread.currentThread().getName() + "] Interrupted");
                        // Восстанавливается флаг прерывания
                        Thread.currentThread().interrupt();
                        // Прерывается цикл обработки
                        break;
                    }
                }
                // Выводится сообщение о завершении работы потока
                System.out.println("[" + Thread.currentThread().getName() + "] Finished");
            };

            // Создаётся новый поток thread с задачей batchProcessor
            Thread thread = new Thread(batchProcessor);
            // Устанавливается имя потока
            thread.setName(threadName);
            // Поток добавляется в список threads
            threads.add(thread);
            // Запускается поток
            thread.start();
        }

        // В цикле выполняется ожидание завершения каждого потока через join()
        for (Thread thread : threads) {
            try {
                // Вызывается метод join — основной поток ждёт завершения текущего рабочего потока
                thread.join();
            } catch (InterruptedException e) {
                // Восстанавливается флаг прерывания
                Thread.currentThread().interrupt();
            }
        }

        // Вычисляется длительность обработки в миллисекундах
        long duration = System.currentTimeMillis() - startTime;
        // Выводится итоговое сообщение с длительностью
        System.out.println();
        System.out.println("All pool threads completed. Duration: " + duration + " ms");
        System.out.println();
    }

    // Метод simulate — имитирует выполнение операции с задержкой 100..500 мс
    private void simulate(String operation) throws InterruptedException {
        // Выводится сообщение о выполняемой операции с указанием имени потока
        System.out.println("[" + Thread.currentThread().getName() + "] " + operation);
        // Вызывается метод sleep — поток засыпает на случайное время от 100 до 500 мс
        Thread.sleep(100 + (long) (Math.random() * 400));
    }
}