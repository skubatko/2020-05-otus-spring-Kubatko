package ru.skubatko.dev.otus.spring.hw07.service;

import ru.skubatko.dev.otus.spring.hw07.dao.AuthorDao;
import ru.skubatko.dev.otus.spring.hw07.domain.Author;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService implements CrudService<Author> {

    private final AuthorDao dao;

    @Override
    public Author findById(long id) {
        return dao.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return dao.findAll();
    }

    @Override
    public int insert(Author author) {
        return dao.insert(author);
    }

    @Override
    public int update(Author author) {
        return dao.update(author);
    }

    @Override
    public int deleteById(long id) {
        return dao.deleteById(id);
    }

    @Override
    public long count() {
        return dao.count();
    }
}
