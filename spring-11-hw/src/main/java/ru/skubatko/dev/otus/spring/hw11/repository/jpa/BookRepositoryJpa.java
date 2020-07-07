package ru.skubatko.dev.otus.spring.hw11.repository.jpa;

import ru.skubatko.dev.otus.spring.hw11.domain.Book;
import ru.skubatko.dev.otus.spring.hw11.exception.DataNotFoundRepositoryException;
import ru.skubatko.dev.otus.spring.hw11.repository.BookRepository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public Book findByName(String name) {
        return em.createQuery("SELECT b FROM Book b WHERE b.name = :name", Book.class)
                       .setParameter("name", name)
                       .getSingleResult();
    }

    @Override
    public List<Book> findAll() {
        return em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() <= 0L) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public void deleteById(long id) {
        em.remove(findById(id).orElseThrow(DataNotFoundRepositoryException::new));
    }
}
