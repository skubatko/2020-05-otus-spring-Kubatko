package ru.skubatko.dev.otus.spring.hw07.dao.jdbc;

import ru.skubatko.dev.otus.spring.hw07.dao.BookDao;
import ru.skubatko.dev.otus.spring.hw07.domain.Book;

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
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Book findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return jdbc.queryForObject("select * from books where id = :id", params, this::bookRowMapper);
    }

    @Override
    public List<Book> findAll() {
        return jdbc.query("select * from books", this::bookRowMapper);
    }

    @Override
    public int insert(Book book) {
        return jdbc.update("insert into books (id, name, author_id, genre_id) values (:id, :name, :authorId, :genreId)",
                new BeanPropertySqlParameterSource(book));
    }

    @Override
    public int update(Book book) {
        return jdbc.update("update books set name = :name where id = :id",
                new BeanPropertySqlParameterSource(book));
    }

    @Override
    public int deleteById(long id) {
        return jdbc.update("delete from books where id = :id", new MapSqlParameterSource("id", id));
    }

    @Override
    public long count() {
        return jdbc.queryForObject("select count(*) from books", Collections.emptyMap(), Long.class);
    }

    private Book bookRowMapper(ResultSet resultSet, int i) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        long authorId = resultSet.getLong("author_id");
        long genreId = resultSet.getLong("genre_id");
        return new Book(id, name, authorId, genreId);
    }
}
