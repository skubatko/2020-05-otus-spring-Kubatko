package ru.skubatko.dev.otus.spring.hw20.rest;

import ru.skubatko.dev.otus.spring.hw20.dto.BookDto;
import ru.skubatko.dev.otus.spring.hw20.repository.BookRepository;
import ru.skubatko.dev.otus.spring.hw20.repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BooksController {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/api/books")
    @ResponseStatus(HttpStatus.OK)
    public Flux<BookDto> getBooks() {
        return bookRepository.findAll().map(BookDto::toDto);
    }

    @PostMapping("/api/books")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDto> create(@RequestBody BookDto bookDto) {
        bookRepository.(bookDto);
        return bookDto;
    }

    @PutMapping("/api/books/{oldBookName}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BookDto> update(
            @PathVariable("oldBookName") String oldBookName,
            @RequestBody final BookDto bookDto) {
        bookRepository.updateBook(oldBookName, bookDto.getName());
        return bookDto;
    }

    @DeleteMapping("/api/books/{bookName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("bookName") String bookName) {
        bookRepository.deleteBook(bookName);
    }
}
