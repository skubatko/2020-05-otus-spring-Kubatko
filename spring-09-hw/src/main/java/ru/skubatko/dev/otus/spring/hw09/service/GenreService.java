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
    public Genre findById(long id) {
        return repository.findById(id).orElse(null);
    }

    public Genre findByName(String name) {
        return repository.findByName(name);
    }

    @Override
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
}
