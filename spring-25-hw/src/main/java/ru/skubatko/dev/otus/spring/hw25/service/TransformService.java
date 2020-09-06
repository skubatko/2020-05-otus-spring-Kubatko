package ru.skubatko.dev.otus.spring.hw25.service;

import ru.skubatko.dev.otus.spring.hw25.model.nosql.NoSqlBook;
import ru.skubatko.dev.otus.spring.hw25.model.nosql.NoSqlComment;
import ru.skubatko.dev.otus.spring.hw25.model.sql.SqlBook;
import ru.skubatko.dev.otus.spring.hw25.model.sql.SqlComment;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransformService {

    public NoSqlBook transform(SqlBook book) {
        List<NoSqlComment> comments =
                book.getComments().stream()
                        .map(SqlComment::getContent)
                        .map(content -> new NoSqlComment(content, book.getName()))
                        .collect(Collectors.toList());

        return new NoSqlBook(book.getName(), book.getAuthor().getName(), book.getGenre().getName(), comments);
    }
}
