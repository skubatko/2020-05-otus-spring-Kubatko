package ru.skubatko.dev.otus.spring.hw09.service;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw09.domain.Author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DisplayName("Сервис для работы с авторами должен")
@Transactional
@SpringBootTest
class AuthorServiceTest {

    @Autowired
    private AuthorService service;

    @DisplayName("находить ожидаемого автора по его id")
    @Test
    void shouldFindExpectedAuthorById() {
        Author expected = new Author(2, "testAuthor2");
        Author actual = service.findById(2);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить всех авторов")
    @Test
    void shouldFindAllAuthors() {
        List<Author> authors = service.findAll();
        assertThat(authors)
                .hasSize(3)
                .extracting("name").containsOnlyOnce("testAuthor1", "testAuthor2", "testAuthor3");
    }

    @DisplayName("добавлять автора в базу данных")
    @Test
    void shouldAddAuthor() {
        Author expected = new Author(4, "testAuthor4");
        int result = service.save(expected);
        assertThat(result).isEqualTo(1);

        Author actual = service.findById(4);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("обновлять автора в базе данных")
    @Test
    void shouldUpdateAuthor() {
        Author author = service.findById(3);
        String updatedName = "testAuthor3Updated";
        author.setName(updatedName);
        int result = service.update(author);
        assertThat(result).isEqualTo(1);

        Author actual = service.findById(3);
        assertThat(actual).hasFieldOrPropertyWithValue("name", updatedName);
    }

    @DisplayName("удалять автора по заданному id из базы данных")
    @Test
    void shouldDeleteAuthorById() {
        int result = service.deleteById(1);
        assertThat(result).isEqualTo(1);

        List<Author> authors = service.findAll();
        assertThat(authors).extracting("id").doesNotContain(1);
    }

    @DisplayName("возвращать ожидаемое количество авторов в базе данных")
    @Test
    void shouldReturnExpectedAuthorsCount() {
        long actual = service.count();
        assertThat(actual).isEqualTo(3L);
    }
}
