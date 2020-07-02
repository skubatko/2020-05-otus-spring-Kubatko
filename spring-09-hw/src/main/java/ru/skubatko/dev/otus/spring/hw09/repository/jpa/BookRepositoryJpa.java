package ru.skubatko.dev.otus.spring.hw09.repository.jpa;

import ru.skubatko.dev.otus.spring.hw09.domain.Book;
import ru.skubatko.dev.otus.spring.hw09.domain.BookComment;
import ru.skubatko.dev.otus.spring.hw09.repository.BookRepository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
    public Optional<Book> findByName(String name) {
        TypedQuery<Book> query = em.createQuery("select b from Book b where b.name = :name", Book.class);
        query.setParameter("name", name);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        Optional<Book> dbBook = findByName(book.getName());
        if (dbBook.isPresent()) {
            update(book);
        } else {
            book.getBookComments().forEach(comment -> comment.setBook(book));
            em.persist(book);
        }

        return book;
    }

    @Override
    public void update(Book book) {
        Optional<Book> dbBookOptional = findById(book.getId());
        if (dbBookOptional.isEmpty()) {
            return;
        }

        Book dbBook = dbBookOptional.get();

        dbBook.setAuthor(book.getAuthor());
        dbBook.setGenre(book.getGenre());

        List<BookComment> dbBookComments = dbBook.getBookComments();
        dbBookComments.forEach(comment -> em.remove(comment));
        dbBookComments.clear();

        book.getBookComments().forEach(comment -> {
            comment.setBook(dbBook);
            dbBookComments.add(comment);
            em.persist(comment);
        });

        em.merge(dbBook);
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public long count() {
        return em.createQuery("select count(b) from Book b", Long.class).getSingleResult();
    }
}
