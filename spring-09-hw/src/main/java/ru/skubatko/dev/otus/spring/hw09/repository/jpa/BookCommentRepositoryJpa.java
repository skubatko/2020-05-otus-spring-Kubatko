package ru.skubatko.dev.otus.spring.hw09.repository.jpa;

import ru.skubatko.dev.otus.spring.hw09.domain.BookComment;
import ru.skubatko.dev.otus.spring.hw09.exceptions.DataNotFoundRepositoryException;
import ru.skubatko.dev.otus.spring.hw09.repository.BookCommentRepository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class BookCommentRepositoryJpa implements BookCommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<BookComment> findById(long id) {
        return Optional.ofNullable(em.find(BookComment.class, id));
    }

    @Override
    public BookComment findByContent(String content) {
        return em.createQuery("select bc from BookComment bc where bc.content = :content", BookComment.class)
                       .setParameter("content", content)
                       .getSingleResult();
    }

    @Override
    public List<BookComment> findAll() {
        return em.createQuery("select bc from BookComment bc", BookComment.class).getResultList();
    }

    @Override
    public BookComment save(BookComment bookComment) {
        if (bookComment.getId() <= 0L) {
            em.persist(bookComment);
            return bookComment;
        } else {
            return em.merge(bookComment);
        }
    }

    @Override
    public void deleteById(long id) {
        em.remove(findById(id).orElseThrow(DataNotFoundRepositoryException::new));
    }

    @Override
    public long count() {
        return em.createQuery("select count(bc) from BookComment bc", Long.class).getSingleResult();
    }
}
