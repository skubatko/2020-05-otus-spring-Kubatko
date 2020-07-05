package ru.skubatko.dev.otus.spring.hw09.service;

import ru.skubatko.dev.otus.spring.hw09.domain.Genre;
import ru.skubatko.dev.otus.spring.hw09.repository.GenreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService implements CrudService<Genre> {

    private final GenreRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Genre findById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Genre findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void save(Genre genre) {
        repository.save(genre);
    }

    @Override
    @Transactional
    public void update(Genre genre) {
        repository.save(genre);
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
