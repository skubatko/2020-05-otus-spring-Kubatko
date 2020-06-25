package ru.skubatko.dev.otus.spring.hw07.service;

import ru.skubatko.dev.otus.spring.hw07.dao.GenreDao;
import ru.skubatko.dev.otus.spring.hw07.domain.Genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService implements CrudService<Genre> {

    private final GenreDao dao;

    @Override
    public Genre findById(long id) {
        try {
            return dao.findById(id);
        } catch (DataAccessException e) {
            log.debug("findById() - verdict: db operation failed", e);
            return null;
        }
    }

    @Override
    public List<Genre> findAll() {
        return dao.findAll();
    }

    @Override
    public int insert(Genre genre) {
        try {
            return dao.insert(genre);
        } catch (DataAccessException e) {
            log.debug("insert() - verdict: db operation failed", e);
            return 0;
        }
    }

    @Override
    public int update(Genre genre) {
        try {
            return dao.update(genre);
        } catch (DataAccessException e) {
            log.debug("update() - verdict: db operation failed", e);
            return 0;
        }
    }

    @Override
    public int deleteById(long id) {
        try {
            return dao.deleteById(id);
        } catch (DataAccessException e) {
            log.debug("deleteById() - verdict: db operation failed", e);
            return 0;
        }
    }

    @Override
    public long count() {
        return dao.count();
    }
}
