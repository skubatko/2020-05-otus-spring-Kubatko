package ru.skubatko.dev.otus.spring.hw09.dao.jdbc;

import ru.skubatko.dev.otus.spring.hw09.dao.GenreDao;
import ru.skubatko.dev.otus.spring.hw09.domain.Genre;

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
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Genre findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return jdbc.queryForObject("select id, name from genres where id = :id", params, this::genreRowMapper);
    }

    @Override
    public List<Genre> findAll() {
        return jdbc.query("select id, name from genres", this::genreRowMapper);
    }

    @Override
    public int insert(Genre genre) {
        return jdbc.update("insert into genres (id, name) values (:id, :name)",
                new BeanPropertySqlParameterSource(genre));
    }

    @Override
    public int update(Genre genre) {
        return jdbc.update("update genres set name = :name where id = :id",
                new BeanPropertySqlParameterSource(genre));
    }

    @Override
    public int deleteById(long id) {
        return jdbc.update("delete from genres where id = :id", new MapSqlParameterSource("id", id));
    }

    @Override
    public long count() {
        return jdbc.queryForObject("select count(id) from genres", Collections.emptyMap(), Long.class);
    }

    private Genre genreRowMapper(ResultSet resultSet, int i) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        return new Genre(id, name);
    }
}
