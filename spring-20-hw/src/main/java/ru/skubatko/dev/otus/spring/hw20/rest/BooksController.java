package ru.skubatko.dev.otus.spring.hw20.rest;

import ru.skubatko.dev.otus.spring.hw20.domain.Book;
import ru.skubatko.dev.otus.spring.hw20.domain.Comment;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BooksController {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    private static final String COMMA = ",";

    @GetMapping("/api/books")
    @ResponseStatus(HttpStatus.OK)
    public Flux<BookDto> getBooks() {
        return bookRepository.findAll().map(BookDto::toDto);
    }

    @PostMapping("/api/books")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDto> create(@RequestBody BookDto dto) {
        return commentRepository.saveAll(getComments(dto))
                       .collectList()
                       .flatMap(comments -> bookRepository.insert(new Book(dto.getName(), dto.getAuthor(), dto.getGenre(), comments)))
                       .map(BookDto::toDto);
    }

    private List<Comment> getComments(BookDto dto) {
        return Arrays.stream(dto.getComments().split(COMMA))
                       .map(content -> new Comment(content.trim(), dto.getName()))
                       .collect(Collectors.toList());
    }

    @PutMapping("/api/books/{oldBookName}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BookDto> update(
            @PathVariable("oldBookName") String oldBookName,
            @RequestBody final BookDto dto) {

        return Mono.just(dto)
                       .flatMap(bookDto -> bookRepository.findByName(oldBookName))
                       .flatMap(book -> bookRepository.save(book.setName(dto.getName())))
                       .map(BookDto::toDto);
    }

    @DeleteMapping("/api/books/{bookName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("bookName") String bookName) {
        bookRepository.deleteByName(bookName).then(
                commentRepository.deleteAllByBookName(bookName)).subscribe();
    }
}
