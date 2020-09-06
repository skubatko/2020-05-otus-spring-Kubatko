package ru.skubatko.dev.otus.spring.hw25.repository;

import ru.skubatko.dev.otus.spring.hw25.model.sql.SqlBook;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SqlBookRepository extends JpaRepository<SqlBook, Long> {

    @EntityGraph(attributePaths = {"author", "genre", "comments"})
    @Override
    List<SqlBook> findAll();
}
