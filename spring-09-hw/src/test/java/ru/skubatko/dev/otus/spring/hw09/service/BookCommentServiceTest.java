package ru.skubatko.dev.otus.spring.hw09.service;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw09.domain.Book;
import ru.skubatko.dev.otus.spring.hw09.domain.BookComment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DisplayName("Сервис для работы с комментариями книг должен")
@Transactional
@SpringBootTest
class BookCommentServiceTest {

    @Autowired
    private BookCommentService bookCommentService;

    @Autowired
    private BookService bookService;

    @DisplayName("находить ожидаемый комментарий по его id")
    @Test
    void shouldFindExpectedBookCommentById() {
        BookComment actual = bookCommentService.findById(2);
        assertThat(actual).hasFieldOrPropertyWithValue("content", "testBookComment2");
    }

    @DisplayName("находить ожидаемый комментарий по его содержанию")
    @Test
    void shouldFindExpectedBookCommentByContent() {
        String content = "testBookComment2";
        BookComment actual = bookCommentService.findByName(content);
        assertThat(actual).hasFieldOrPropertyWithValue("content", content);
    }

    @DisplayName("находить все комментарии")
    @Test
    void shouldFindAllBookComments() {
        List<BookComment> bookComments = bookCommentService.findAll();
        assertThat(bookComments)
                .hasSize(6)
                .extracting("content")
                .containsOnlyOnce(
                        "testBookComment1",
                        "testBookComment2",
                        "testBookComment3",
                        "testBookComment4",
                        "testBookComment5",
                        "testBookComment6");
    }

    @DisplayName("находить все комментарии по заданному id книги")
    @Test
    void shouldFindAllBookCommentsByBookId() {
        List<BookComment> bookComments = bookCommentService.findAllByBookId(1L);
        assertThat(bookComments)
                .hasSize(1)
                .extracting("content")
                .containsOnly("testBookComment1");
    }

    @DisplayName("добавлять комментарий")
    @Test
    void shouldAddBookComment() {
        String bookName = "testBook2";
        Book book = bookService.findByName(bookName);
        BookComment bookComment = new BookComment();
        String content = "testBookCommentNew";
        bookComment.setContent(content);
        bookComment.setBook(book);
        bookCommentService.save(bookComment);

        BookComment actual = bookCommentService.findByName(content);
        assertThat(actual).isEqualTo(bookComment);
    }

    @DisplayName("обновлять комментарий")
    @Test
    void shouldUpdateBookComment() {
        BookComment bookComment = bookCommentService.findById(3);
        String updatedContent = "testBookComment3Updated";
        bookComment.setContent(updatedContent);
        bookCommentService.update(bookComment);

        BookComment actual = bookCommentService.findById(3);
        assertThat(actual).hasFieldOrPropertyWithValue("content", updatedContent);
    }

    @DisplayName("удалять комментарий по заданному id")
    @Test
    void shouldDeleteBookCommentById() {
        bookCommentService.deleteById(1);

        List<BookComment> bookComments = bookCommentService.findAll();
        assertThat(bookComments).extracting("id").doesNotContain(1);
    }

    @DisplayName("возвращать ожидаемое количество комментариев")
    @Test
    void shouldReturnExpectedBookCommentsCount() {
        long actual = bookCommentService.count();
        assertThat(actual).isEqualTo(6L);
    }
}
