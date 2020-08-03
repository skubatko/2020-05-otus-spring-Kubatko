package ru.skubatko.dev.otus.spring.hw17.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LibraryController {

    @GetMapping({"/", "/index"})
    public String root() {
        return "index";
    }

    @GetMapping("/books")
    public String books() {
        return "books";
    }

    @GetMapping("/authors")
    public String authors() {
        return "authors";
    }

    @GetMapping("/genres")
    public String genres() {
        return "genres";
    }
}
