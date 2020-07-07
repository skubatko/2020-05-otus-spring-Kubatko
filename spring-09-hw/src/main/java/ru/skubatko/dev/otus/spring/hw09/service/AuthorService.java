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
    public Author findById(long id) {
        return repository.findById(id).orElse(null);
    }

    public Author findByName(String name) {
        return repository.findByName(name);
    }

    @Override
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
        repository.save(author);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
