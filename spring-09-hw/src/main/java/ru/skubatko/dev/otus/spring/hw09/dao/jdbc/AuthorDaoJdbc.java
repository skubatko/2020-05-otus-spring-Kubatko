package ru.skubatko.dev.otus.spring.hw09.dao.jdbc;

import ru.skubatko.dev.otus.spring.hw09.dao.AuthorDao;
import ru.skubatko.dev.otus.spring.hw09.domain.Author;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Author findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return jdbc.queryForObject("select id, name from authors where id = :id", params, this::authorRowMapper);
    }

    @Override
    public List<Author> findAll() {
        return jdbc.query("select id, name from authors", this::authorRowMapper);
    }

    @Override
    public int insert(Author author) {
        return jdbc.update("insert into authors (id, name) values (:id, :name)",
                new BeanPropertySqlParameterSource(author));
    }

    @Override
    public int update(Author author) {
        return jdbc.update("update authors set name = :name where id = :id",
                new BeanPropertySqlParameterSource(author));
    }

    @Override
    public int deleteById(long id) {
        return jdbc.update("delete from authors where id = :id", new MapSqlParameterSource("id", id));
    }

    @Override
    public long count() {
        return jdbc.queryForObject("select count(id) from authors", Collections.emptyMap(), Long.class);
    }

    private Author authorRowMapper(ResultSet resultSet, int i) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        return new Author(id, name);
    }
}
