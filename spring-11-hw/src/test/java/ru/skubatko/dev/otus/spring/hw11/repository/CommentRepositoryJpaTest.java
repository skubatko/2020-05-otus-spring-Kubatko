package ru.skubatko.dev.otus.spring.hw11.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw11.domain.Book;
import ru.skubatko.dev.otus.spring.hw11.domain.Comment;
import ru.skubatko.dev.otus.spring.hw11.repository.jpa.CommentRepositoryJpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@DisplayName("Репозиторий для работы с комментариями книг должен")
@DataJpaTest
@Import(CommentRepositoryJpa.class)
class CommentRepositoryJpaTest {

    @Autowired
    private CommentRepositoryJpa repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("находить ожидаемый комментарий по его id")
    @Test
    void shouldFindExpectedBookCommentById() {
        Book book = em.find(Book.class, 2L);
        Comment expected = new Comment(2, "testBookComment2", book);

        Comment actual = repository.findById(2).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить ожидаемый комментарий по его имени")
    @Test
    void shouldFindExpectedBookCommentByName() {
        Book book = em.find(Book.class, 2L);
        String content = "testBookComment2";
        Comment expected = new Comment(2, content, book);

        Comment actual = repository.findByContent(content);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить все комментарии")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldFindAllBookComments() {
        List<Comment> comments = repository.findAll();
        assertThat(comments)
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
        Book book = em.find(Book.class, 4L);
        Comment expected = new Comment();
        String content = "testBookComment4";
        expected.setContent(content);
        expected.setBook(book);
        repository.save(expected);

        Comment actual = repository.findById(4).orElse(null);
        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).isEqualTo(content);
        assertThat(actual.getBook()).isEqualTo(book);
    }

    @DisplayName("обновлять комментарий в базе данных")
    @Test
    void shouldUpdateBookComment() {
        Comment comment = repository.findByContent("testBookComment3");
        String updatedContent = "testBookComment3Updated";
        comment.setContent(updatedContent);
        repository.save(comment);

        Comment actual = repository.findById(3).orElse(null);
        assertThat(actual).hasFieldOrPropertyWithValue("content", updatedContent);
    }

    @DisplayName("удалять комментарий по заданному id из базы данных")
    @Test
    void shouldDeleteBookCommentById() {
        repository.deleteById(1);

        List<Comment> comments = repository.findAll();
        assertThat(comments).extracting("id").doesNotContain(1);
    }
}
