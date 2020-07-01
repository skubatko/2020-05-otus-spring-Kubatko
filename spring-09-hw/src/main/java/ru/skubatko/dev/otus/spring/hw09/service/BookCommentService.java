package ru.skubatko.dev.otus.spring.hw09.service;

import ru.skubatko.dev.otus.spring.hw09.domain.BookComment;
import ru.skubatko.dev.otus.spring.hw09.repository.BookCommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookCommentService implements CrudService<BookComment> {

    private final BookCommentRepository repository;

    @Override
    @Transactional(readOnly = true)
    public BookComment findById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookComment> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void save(BookComment bookComment) {
        repository.save(bookComment);
    }

    @Override
    @Transactional
    public void update(BookComment bookComment) {
        repository.update(bookComment);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }
}
