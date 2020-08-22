package ru.skubatko.dev.otus.spring.hw22.controller;

import ru.skubatko.dev.otus.spring.hw22.dto.BookDto;
import ru.skubatko.dev.otus.spring.hw22.service.LibraryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService service;

    @GetMapping({"/library/books", "/"})
    public String listBooks(Model model) {
        model.addAttribute("books", service.findAllBooks());
        return "index";
    }

    @GetMapping("/library/books/add")
    public String showAddBookForm(HttpServletRequest request, Model model) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (Objects.nonNull(inputFlashMap)) {
            model.addAttribute("error", inputFlashMap.get("error"));
        }

        model.addAttribute("book", new BookDto());
        return "add-book";
    }

    @PostMapping("/library/books/add")
    public RedirectView addBook(@ModelAttribute("book") @Valid BookDto book,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return new RedirectView("/library/books/add", true);
        }

        service.addBook(book);
        redirectAttributes.addFlashAttribute("book", book);
        return new RedirectView("/library/books/add/success", true);
    }

    @GetMapping("/library/books/add/success")
    public String showAddBookSuccessForm(HttpServletRequest request, Model model) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (Objects.nonNull(inputFlashMap)) {
            BookDto book = (BookDto) inputFlashMap.get("book");
            model.addAttribute("book", book);
            return "add-book-success";
        } else {
            return "redirect:/library/books";
        }
    }

    @GetMapping("/library/books/edit/{name}")
    public String showUpdateBookForm(@PathVariable("name") String name,
                                     HttpServletRequest request,
                                     Model model) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (Objects.nonNull(inputFlashMap)) {
            model.addAttribute("error", inputFlashMap.get("error"));
        }

        BookDto book = service.findBookByName(name);
        model.addAttribute("book", book);
        return "update-book";
    }

    @PostMapping("/library/books/update/{name}")
    public RedirectView updateBook(@PathVariable("name") String name,
                                   @ModelAttribute("book") BookDto book,
                                   RedirectAttributes redirectAttributes) {
        if (book.getName().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Book's name is mandatory");
            return new RedirectView(String.format("/library/books/edit/%s", name), true);
        }

        service.updateBook(name, book.getName());
        return new RedirectView("/library/books", true);
    }

    @GetMapping("/library/books/delete/{name}")
    public String deleteBook(@PathVariable("name") String name, Model model) {
        service.deleteBook(name);
        return "redirect:/library/books";
    }
}
