package ru.skubatko.dev.otus.spring.hw09.service;

import ru.skubatko.dev.otus.spring.hw09.dao.BookDao;
import ru.skubatko.dev.otus.spring.hw09.domain.Book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService implements CrudService<Book> {

    private final BookDao dao;

    @Override
    public Book findById(long id) {
        try {
            return dao.findById(id);
        } catch (DataAccessException e) {
            log.debug("findById() - verdict: db operation failed", e);
            return null;
        }
    }

    @Override
    public List<Book> findAll() {
        return dao.findAll();
    }

    @Override
    public int insert(Book book) {
        try {
            return dao.insert(book);
        } catch (DataAccessException e) {
            log.debug("insert() - verdict: db operation failed", e);
            return 0;
        }
    }

    @Override
    public int update(Book book) {
        try {
            return dao.update(book);
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
