package ru.skubatko.dev.otus.spring.hw17.api;

import ru.skubatko.dev.otus.spring.hw17.dto.BookDto;
import ru.skubatko.dev.otus.spring.hw17.service.LibraryService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BooksResource {

    private final LibraryService service;

    @GetMapping("/api/books")
    public List<BookDto> getBooks() {
        return service.findAllBooks();
    }

    @PostMapping("/api/books")
    public BookDto create(@RequestBody BookDto bookDto) {
        service.addBook(bookDto);
        return bookDto;
    }

    @PutMapping("/api/books/{oldBookName}")
    public BookDto update(
            @PathVariable("oldBookName") String oldBookName,
            @RequestBody final BookDto bookDto) {
        service.updateBook(oldBookName, bookDto.getName());
        return bookDto;
    }

    @DeleteMapping("/api/books/{bookName}")
    public void delete(@PathVariable("bookName") String bookName) {
        service.deleteBook(bookName);
    }
}
