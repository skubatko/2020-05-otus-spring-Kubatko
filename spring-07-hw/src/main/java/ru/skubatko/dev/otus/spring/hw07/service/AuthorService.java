package ru.skubatko.dev.otus.spring.hw07.service;

import ru.skubatko.dev.otus.spring.hw07.dao.AuthorDao;
import ru.skubatko.dev.otus.spring.hw07.domain.Author;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService implements CrudService<Author> {

    private final AuthorDao dao;

    @Override
    public Author findById(long id) {
        try {
            return dao.findById(id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Author> findAll() {
        return dao.findAll();
    }

    @Override
    public int insert(Author author) {
        try {
            return dao.insert(author);
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public int update(Author author) {
        try {
            return dao.update(author);
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public int deleteById(long id) {
        try {
            return dao.deleteById(id);
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public long count() {
        return dao.count();
    }
}
