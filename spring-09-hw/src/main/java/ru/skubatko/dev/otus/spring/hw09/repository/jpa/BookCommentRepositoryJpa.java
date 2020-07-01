package ru.skubatko.dev.otus.spring.hw09.repository.jpa;

import ru.skubatko.dev.otus.spring.hw09.domain.BookComment;
import ru.skubatko.dev.otus.spring.hw09.repository.BookCommentRepository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
    public Optional<BookComment> findByContent(String content) {
        TypedQuery<BookComment> query =
                em.createQuery("select bc from BookComment bc where bc.content = :content", BookComment.class);
        query.setParameter("content", content);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<BookComment> findAll() {
        TypedQuery<BookComment> query = em.createQuery("select bc from BookComment bc", BookComment.class);
        return query.getResultList();
    }

    @Override
    public BookComment save(BookComment bookComment) {
        if (bookComment.getId() <= 0L) {
            em.persist(bookComment);
            return bookComment;
        }
        return em.merge(bookComment);
    }

    @Override
    public void update(BookComment bookComment) {
        Optional<BookComment> dbBookComment = findById(bookComment.getId());
        if (dbBookComment.isPresent()) {
            em.merge(bookComment);
        }
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from BookComment bc where bc.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public long count() {
        return em.createQuery("select count(bc) from BookComment bc", Long.class).getSingleResult();
    }
}
