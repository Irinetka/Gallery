package com.example.gallery.service;

import com.example.gallery.models.Exhibit;
import java.util.ArrayList;
import java.util.List;

public class MultiThreadService {

    public void processExhibitsInParallel(List<Exhibit> exhibits) {
        if (exhibits == null || exhibits.isEmpty()) {
            System.out.println("No exhibits to process");
            return;
        }

        System.out.println();
        System.out.println("MULTITHREAD EXHIBIT PROCESSING");
        System.out.println("Total exhibits: " + exhibits.size());
        System.out.println();

        List<Thread> threads = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < exhibits.size(); i++) {
            final Exhibit exhibit = exhibits.get(i);
            final int index = i;
            String threadName = "GalleryWorker-" + (i + 1);

            Runnable processor = () -> {
                Thread.currentThread().setName(threadName);
                System.out.println("[" + Thread.currentThread().getName() + "] " +
                        "Start processing #" + exhibit.getId() + ": " + exhibit.getTitle());
                try {
                    simulate("Loading metadata");
                    simulate("Analyzing author: " + (exhibit.getAuthor() != null ? exhibit.getAuthor().getFullName() : "unknown"));
                    simulate("Checking category: " + (exhibit.getCategory() != null ? exhibit.getCategory().getName() : "not specified"));
                    System.out.println("[" + Thread.currentThread().getName() + "] " +
                            "Exhibit #" + exhibit.getId() + " processed");
                } catch (InterruptedException e) {
                    System.out.println("[" + Thread.currentThread().getName() + "] Interrupted");
                    Thread.currentThread().interrupt();
                }
            };

            Thread thread = new Thread(processor);
            thread.setName(threadName);
            threads.add(thread);
            thread.start();
            System.out.println("Started thread: " + threadName);
        }

        System.out.println();
        System.out.println("Waiting for all threads to complete...");
        System.out.println();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Waiting interrupted");
                Thread.currentThread().interrupt();
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        System.out.println();
        System.out.println("All threads completed. Duration: " + duration + " ms");
        System.out.println();
    }

    public void processExhibitsWithLimitedThreads(List<Exhibit> exhibits, int threadCount) {
        if (exhibits == null || exhibits.isEmpty()) {
            System.out.println("No exhibits to process");
            return;
        }
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Thread count must be positive");
        }

        System.out.println();
        System.out.println("MULTITHREAD PROCESSING WITH POOL OF " + threadCount + " THREADS");
        System.out.println("Exhibits: " + exhibits.size() + ", Threads: " + threadCount);
        System.out.println();

        long startTime = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            String threadName = "PoolWorker-" + (i + 1);

            Runnable batchProcessor = () -> {
                Thread.currentThread().setName(threadName);
                System.out.println("[" + Thread.currentThread().getName() + "] Thread started");
                for (int j = threadIndex; j < exhibits.size(); j += threadCount) {
                    Exhibit exhibit = exhibits.get(j);
                    try {
                        System.out.println("[" + Thread.currentThread().getName() + "] " +
                                "Processing #" + exhibit.getId() + ": " + exhibit.getTitle());
                        Thread.sleep(100 + (long) (Math.random() * 300));
                        System.out.println("[" + Thread.currentThread().getName() + "] " +
                                "Processed #" + exhibit.getId());
                    } catch (InterruptedException e) {
                        System.out.println("[" + Thread.currentThread().getName() + "] Interrupted");
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                System.out.println("[" + Thread.currentThread().getName() + "] Finished");
            };

            Thread thread = new Thread(batchProcessor);
            thread.setName(threadName);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        System.out.println();
        System.out.println("All pool threads completed. Duration: " + duration + " ms");
        System.out.println();
    }

    private void simulate(String operation) throws InterruptedException {
        System.out.println("[" + Thread.currentThread().getName() + "] " + operation);
        Thread.sleep(100 + (long) (Math.random() * 400));
    }
}