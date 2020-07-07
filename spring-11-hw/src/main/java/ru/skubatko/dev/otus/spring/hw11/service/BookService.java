package ru.skubatko.dev.otus.spring.hw11.service;

import ru.skubatko.dev.otus.spring.hw11.domain.Book;
import ru.skubatko.dev.otus.spring.hw11.domain.Comment;
import ru.skubatko.dev.otus.spring.hw11.repository.BookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService implements CrudService<Book> {

    private final BookRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Book findById(long id) {
        Optional<Book> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return null;
        }

        Book book = optional.get();
        book.getComments().forEach(Comment::getContent);
        return book;
    }

    @Transactional(readOnly = true)
    public Book findByName(String name) {
        Book book = repository.findByName(name);
        book.getComments().forEach(Comment::getContent);
        return book;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        List<Book> books = repository.findAll();
        books.forEach(book -> book.getComments().forEach(Comment::getContent));
        return books;
    }

    @Override
    @Transactional
    public void save(Book book) {
        repository.save(book);
    }

    @Override
    @Transactional
    public void update(Book book) {
        repository.save(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
