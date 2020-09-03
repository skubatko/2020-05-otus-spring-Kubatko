package ru.skubatko.dev.otus.spring.hw25.service;

import ru.skubatko.dev.otus.spring.hw25.model.nosql.NoSqlBook;
import ru.skubatko.dev.otus.spring.hw25.repository.NoSqlBookRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoSqlLibraryService {

    private final NoSqlBookRepository repository;

    public List<NoSqlBook> findAllBooks() {
        return repository.findAll();
    }
}
