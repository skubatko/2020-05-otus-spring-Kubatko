package ru.skubatko.dev.otus.spring.hw11.repository.jpa;

import ru.skubatko.dev.otus.spring.hw11.domain.Comment;
import ru.skubatko.dev.otus.spring.hw11.exception.DataNotFoundRepositoryException;
import ru.skubatko.dev.otus.spring.hw11.repository.CommentRepository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public Comment findByContent(String content) {
        return em.createQuery("SELECT c FROM Comment c WHERE c.content = :content", Comment.class)
                       .setParameter("content", content)
                       .getSingleResult();
    }

    @Override
    public List<Comment> findAll() {
        return em.createQuery("SELECT c FROM Comment c", Comment.class).getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() <= 0L) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public void deleteById(long id) {
        em.remove(findById(id).orElseThrow(DataNotFoundRepositoryException::new));
    }
}
