package ru.skubatko.dev.otus.spring.hw09.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw09.domain.Genre;
import ru.skubatko.dev.otus.spring.hw09.repository.jpa.GenreRepositoryJpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DisplayName("Jdbc dao для работы с жанрами должно")
@JdbcTest
@Import(GenreRepositoryJpa.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class GenreDaoJdbcTest {

    @Autowired
    private GenreRepositoryJpa dao;

    @DisplayName("находить ожидаемый жанр по его id")
    @Test
    void shouldFindExpectedGenreById() {
        Genre expected = new Genre(2, "testGenre2");
        Genre actual = dao.findById(2);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить все жанры")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldFindAllGenres() {
        List<Genre> genres = dao.findAll();
        assertThat(genres)
                .hasSize(4)
                .extracting("name").containsOnlyOnce("testGenre1", "testGenre2", "testGenre3", "testGenre4");
    }

    @DisplayName("добавлять жанр в базу данных")
    @Test
    void shouldAddGenre() {
        Genre expected = new Genre(5, "testGenre5");
        int result = dao.save(expected);
        assertThat(result).isEqualTo(1);

        Genre actual = dao.findById(5);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("обновлять жанр в базе данных")
    @Test
    void shouldUpdateGenre() {
        Genre genre = dao.findById(3);
        String updatedName = "testGenre3Updated";
        genre.setName(updatedName);
        int result = dao.update(genre);
        assertThat(result).isEqualTo(1);

        Genre actual = dao.findById(3);
        assertThat(actual).hasFieldOrPropertyWithValue("name", updatedName);
    }

    @DisplayName("удалять жанр по заданному id из базы данных")
    @Test
    void shouldDeleteGenreById() {
        int result = dao.deleteById(1);
        assertThat(result).isEqualTo(1);

        List<Genre> genres = dao.findAll();
        assertThat(genres).extracting("id").doesNotContain(1);
    }

    @DisplayName("возвращать ожидаемое количество жанров в базе данных")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnExpectedGenresCount() {
        long actual = dao.count();
        assertThat(actual).isEqualTo(4L);
    }
}
