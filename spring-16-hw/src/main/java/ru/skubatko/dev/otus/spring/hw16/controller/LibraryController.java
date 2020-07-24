package ru.skubatko.dev.otus.spring.hw16.controller;

import ru.skubatko.dev.otus.spring.hw16.dto.BookDto;
import ru.skubatko.dev.otus.spring.hw16.service.LibraryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService service;

    @GetMapping("/library/books")
    public String listBooks(Model model) {
        model.addAttribute("books", service.findAllBooks());
        return "index";
    }

    @GetMapping("/library/books/add")
    public String addBook(Model model) {
        model.addAttribute("book", new BookDto());
        return "add-book";
    }

    @PostMapping("/library/books/add")
    public String addBook(@Valid BookDto book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-book";
        }

        service.addBook(book);
        model.addAttribute("books", service.findAllBooks());
        return "redirect:/library/books";
    }

    @GetMapping("/library/books/edit/{name}")
    public String showUpdateForm(@PathVariable("name") String name, Model model) {
        BookDto book = service.findBookByName(name);
        model.addAttribute("book", book);
        return "update-book";
    }

    @PostMapping("/library/books/update/{name}")
    public String updateBook(@PathVariable("name") String name, @Valid BookDto book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            book.setName(name);
            return "update-book";
        }

        service.updateBook(name, book.getName());
        model.addAttribute("books", service.findAllBooks());
        return "redirect:/library/books";
    }

    @GetMapping("/library/books/delete/{name}")
    public String deleteBook(@PathVariable("name") String name, Model model) {
        service.deleteBook(name);
        model.addAttribute("books", service.findAllBooks());
        return "index";
    }
}
