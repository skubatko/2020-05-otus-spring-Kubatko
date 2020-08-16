package ru.skubatko.dev.otus.spring.hw20.rest;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import ru.skubatko.dev.otus.spring.hw20.domain.Book;
import ru.skubatko.dev.otus.spring.hw20.domain.Comment;
import ru.skubatko.dev.otus.spring.hw20.dto.BookDto;
import ru.skubatko.dev.otus.spring.hw20.repository.BookRepository;
import ru.skubatko.dev.otus.spring.hw20.repository.CommentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@DisplayName("Контроллер работы с книгами")
@WebFluxTest(BooksController.class)
class BooksControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private CommentRepository commentRepository;

    private final String baseUrl = "/api/books";
    private final String bookName = "testName";

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        book = new Book(bookName, "testAuthor", "testGenre", Collections.singletonList(new Comment("testComments", bookName)));
        bookDto = BookDto.toDto(book);
    }

    @DisplayName("должен возвращать статус OK и все ожидаемые книги когда выполняется запрос GET по пути /api/books")
    @Test
    void shouldReturnStatusOkAndAllExpectedBooksWhenPerformsGetOnApiBooks() {
        List<BookDto> books = List.of(bookDto);
        given(bookRepository.findAll()).willReturn(Flux.just(book));

        webTestClient.get().uri(baseUrl)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(1)
                .isEqualTo(books);
    }

    @DisplayName("должен возвращать статус CREATED и ожидаемую книгу когда выполняется запрос POST по пути /api/books")
    @Test
    void shouldReturnStatusCreatedAndExpectedBookWhenPerformsPostOnApiBooks() {
        List<Comment> comments = book.getComments();

        when(commentRepository.saveAll(comments)).thenReturn(Flux.fromIterable(comments));
        when(bookRepository.insert(book)).thenReturn(Mono.just(book));

        webTestClient.post().uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDto.class)
                .isEqualTo(bookDto);
    }

    @DisplayName("должен возвращать статус OK и ожидаемую книгу когда выполняется запрос PUT по пути /api/books/{oldBookName}")
    @Test
    void shouldReturnStatusOkAndExpectedBookWhenPerformsPutOnApiBooksWithOldBookName() {
        String oldBookName = "oldBookName";
        String url = baseUrl + "/%s";

        when(bookRepository.findByName(oldBookName)).thenReturn(Mono.just(book));
        when(bookRepository.save(book)).thenReturn(Mono.just(book));

        webTestClient.put().uri(String.format(url, oldBookName))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(bookDto);
    }

    @DisplayName("должен возвращать статус NO_CONTENT и когда выполняется запрос DELETE по пути /api/books/{bookName}")
    @Test
    void shouldReturnStatusNoContentWhenPerformsDeleteOnApiBooks() {
        String url = baseUrl + "/%s";
        String bookName = bookDto.getName();

        when(bookRepository.deleteByName(bookName)).thenReturn(Mono.empty());
        when(commentRepository.deleteAllByBookName(bookName)).thenReturn(Mono.empty());

        webTestClient.delete().uri(String.format(url, bookName))
                .exchange()
                .expectStatus().isNoContent();
    }
}
