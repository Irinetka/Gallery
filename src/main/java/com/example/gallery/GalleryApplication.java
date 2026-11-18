package com.example.gallery;

import com.example.gallery.console.ConsoleApp;
import com.example.gallery.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class GalleryApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(GalleryApplication.class, args);

        ConsoleApp app = new ConsoleApp(
                context.getBean(UserService.class),
                context.getBean(AuthService.class),
                context.getBean(AuthorService.class),
                context.getBean(CategoryService.class),
                context.getBean(ExhibitService.class),
                context.getBean(ExhibitionService.class),
                context.getBean(ReviewService.class),
                context.getBean(TicketService.class),
                context.getBean(MultiThreadService.class)
        );
        app.start();
    }
}