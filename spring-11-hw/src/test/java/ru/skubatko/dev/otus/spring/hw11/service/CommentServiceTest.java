package ru.skubatko.dev.otus.spring.hw11.service;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw11.domain.Book;
import ru.skubatko.dev.otus.spring.hw11.domain.Comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DisplayName("Сервис для работы с комментариями книг должен")
@Transactional
@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BookService bookService;

    @DisplayName("находить ожидаемый комментарий по его id")
    @Test
    void shouldFindExpectedBookCommentById() {
        Comment actual = commentService.findById(2);
        assertThat(actual).hasFieldOrPropertyWithValue("content", "testBookComment2");
    }

    @DisplayName("находить ожидаемый комментарий по его содержанию")
    @Test
    void shouldFindExpectedBookCommentByContent() {
        String content = "testBookComment2";
        Comment actual = commentService.findByName(content);
        assertThat(actual).hasFieldOrPropertyWithValue("content", content);
    }

    @DisplayName("находить все комментарии")
    @Test
    void shouldFindAllBookComments() {
        List<Comment> comments = commentService.findAll();
        assertThat(comments)
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
        String bookName = "testBook2";
        Book book = bookService.findByName(bookName);
        Comment comment = new Comment();
        String content = "testBookCommentNew";
        comment.setContent(content);
        comment.setBook(book);
        commentService.save(comment);

        Comment actual = commentService.findByName(content);
        assertThat(actual).isEqualTo(comment);
    }

    @DisplayName("обновлять комментарий")
    @Test
    void shouldUpdateBookComment() {
        Comment comment = commentService.findById(3);
        String updatedContent = "testBookComment3Updated";
        comment.setContent(updatedContent);
        commentService.update(comment);

        Comment actual = commentService.findById(3);
        assertThat(actual).hasFieldOrPropertyWithValue("content", updatedContent);
    }

    @DisplayName("удалять комментарий по заданному id")
    @Test
    void shouldDeleteBookCommentById() {
        commentService.deleteById(1);

        List<Comment> comments = commentService.findAll();
        assertThat(comments).extracting("id").doesNotContain(1);
    }
}
