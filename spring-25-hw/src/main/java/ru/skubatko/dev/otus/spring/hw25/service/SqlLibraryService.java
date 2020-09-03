package ru.skubatko.dev.otus.spring.hw25.service;

import ru.skubatko.dev.otus.spring.hw25.dto.BookDto;
import ru.skubatko.dev.otus.spring.hw25.repository.SqlBookRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SqlLibraryService {

    private final SqlBookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<BookDto> findAllBooks() {
        return bookRepository.findAll().stream().map(BookDto::toDto).collect(Collectors.toList());
    }
}
