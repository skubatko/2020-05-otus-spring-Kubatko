package ru.skubatko.dev.otus.spring.hw09.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw09.domain.BookComment;
import ru.skubatko.dev.otus.spring.hw09.repository.jpa.BookCommentRepositoryJpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@DisplayName("Репозиторий для работы с комментариями книг должен")
@DataJpaTest
@Import(BookCommentRepositoryJpa.class)
class BookCommentRepositoryJpaTest {

    @Autowired
    private BookCommentRepositoryJpa repository;

    @DisplayName("находить ожидаемый комментарий по его id")
    @Test
    void shouldFindExpectedBookCommentById() {
        BookComment expected = new BookComment(2, "testBookComment2", 2);
        BookComment actual = repository.findById(2).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить ожидаемый комментарий по его имени")
    @Test
    void shouldFindExpectedBookCommentByName() {
        String content = "testBookComment2";
        BookComment expected = new BookComment(2, content, 2);
        BookComment actual = repository.findByContent(content).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить все комментарии")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldFindAllBookComments() {
        List<BookComment> bookComments = repository.findAll();
        assertThat(bookComments)
                .hasSize(6)
                .extracting("content").containsOnlyOnce(
                "testBookComment1",
                "testBookComment2",
                "testBookComment3",
                "testBookComment4",
                "testBookComment5",
                "testBookComment6");
    }

    @DisplayName("добавлять комментарий в базу данных")
    @Test
    void shouldAddBookComment() {
        BookComment expected = new BookComment(4, "testBookComment4", 4);
        repository.save(expected);

        BookComment actual = repository.findById(4).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("обновлять комментарий в базе данных")
    @Test
    void shouldUpdateBookComment() {
        BookComment bookComment = repository.findById(3).orElse(null);
        String updatedContent = "testBookComment3Updated";
        bookComment.setContent(updatedContent);
        repository.update(bookComment);

        BookComment actual = repository.findById(3).orElse(null);
        assertThat(actual).hasFieldOrPropertyWithValue("content", updatedContent);
    }

    @DisplayName("удалять комментарий по заданному id из базы данных")
    @Test
    void shouldDeleteBookCommentById() {
        repository.deleteById(1);

        List<BookComment> bookComments = repository.findAll();
        assertThat(bookComments).extracting("id").doesNotContain(1);
    }

    @DisplayName("возвращать ожидаемое количество комментариев в базе данных")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnExpectedBookCommentsCount() {
        long actual = repository.count();
        assertThat(actual).isEqualTo(6L);
    }
}
