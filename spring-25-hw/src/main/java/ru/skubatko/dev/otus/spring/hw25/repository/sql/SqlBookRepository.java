package ru.skubatko.dev.otus.spring.hw25.repository.sql;

import ru.skubatko.dev.otus.spring.hw25.model.sql.SqlAuthor;
import ru.skubatko.dev.otus.spring.hw25.model.sql.SqlBook;
import ru.skubatko.dev.otus.spring.hw25.model.sql.SqlGenre;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SqlBookRepository extends JpaRepository<SqlBook, Long> {

    @EntityGraph(attributePaths = {"author", "genre", "comments"})
    @Override
    List<SqlBook> findAll();

    SqlBook findByName(String name);

    List<SqlBook> findByAuthor(SqlAuthor author);

    List<SqlBook> findByGenre(SqlGenre genre);
}
