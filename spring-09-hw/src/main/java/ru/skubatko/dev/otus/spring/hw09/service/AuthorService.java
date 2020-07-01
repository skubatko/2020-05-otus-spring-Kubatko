package ru.skubatko.dev.otus.spring.hw09.service;

import ru.skubatko.dev.otus.spring.hw09.domain.Author;
import ru.skubatko.dev.otus.spring.hw09.repository.AuthorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorService implements CrudService<Author> {

    private final AuthorRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Author findById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Author findByName(String name) {
        return repository.findByName(name).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void save(Author author) {
        repository.save(author);
    }

    @Override
    @Transactional
    public void update(Author author) {
        repository.update(author);
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
