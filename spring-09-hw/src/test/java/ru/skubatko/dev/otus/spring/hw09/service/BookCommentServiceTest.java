package ru.skubatko.dev.otus.spring.hw09.service;

import static org.assertj.core.api.Assertions.assertThat;

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
    private BookCommentService service;

    @DisplayName("находить ожидаемый комментарий по его id")
    @Test
    void shouldFindExpectedBookCommentById() {
        BookComment expected = new BookComment(2, "testBookComment2", 2);
        BookComment actual = service.findById(2);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить ожидаемый комментарий по его содержанию")
    @Test
    void shouldFindExpectedBookCommentByName() {
        String name = "testBookComment2";
        BookComment expected = new BookComment(2, name, 2);
        BookComment actual = service.findByName(name);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить все комментарии")
    @Test
    void shouldFindAllBookComments() {
        List<BookComment> bookComments = service.findAll();
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

    @DisplayName("добавлять комментарий")
    @Test
    void shouldAddBookComment() {
        BookComment expected = new BookComment();
        String content = "testBookCommentNew";
        expected.setContent(content);
        expected.setBookId(2L);
        service.save(expected);

        BookComment actual = service.findByName(content);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("обновлять комментарий")
    @Test
    void shouldUpdateBookComment() {
        BookComment bookComment = service.findById(3);
        String updatedContent = "testBookComment3Updated";
        bookComment.setContent(updatedContent);
        service.update(bookComment);

        BookComment actual = service.findById(3);
        assertThat(actual).hasFieldOrPropertyWithValue("content", updatedContent);
    }

    @DisplayName("удалять комментарий по заданному id")
    @Test
    void shouldDeleteBookCommentById() {
        service.deleteById(1);

        List<BookComment> bookComments = service.findAll();
        assertThat(bookComments).extracting("id").doesNotContain(1);
    }

    @DisplayName("возвращать ожидаемое количество комментариев")
    @Test
    void shouldReturnExpectedBookCommentsCount() {
        long actual = service.count();
        assertThat(actual).isEqualTo(6L);
    }
}
