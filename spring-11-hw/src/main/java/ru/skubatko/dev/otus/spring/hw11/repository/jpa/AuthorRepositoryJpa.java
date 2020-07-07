package ru.skubatko.dev.otus.spring.hw11.repository.jpa;

import ru.skubatko.dev.otus.spring.hw11.domain.Author;
import ru.skubatko.dev.otus.spring.hw11.exception.DataNotFoundRepositoryException;
import ru.skubatko.dev.otus.spring.hw11.repository.AuthorRepository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public Author findByName(String name) {
        return em.createQuery("SELECT a FROM Author a WHERE a.name = :name", Author.class)
                       .setParameter("name", name)
                       .getSingleResult();
    }

    @Override
    public List<Author> findAll() {
        return em.createQuery("SELECT a FROM Author a", Author.class).getResultList();
    }

    @Override
    public Author save(Author author) {
        if (author.getId() <= 0L) {
            em.persist(author);
            return author;
        } else {
            return em.merge(author);
        }
    }

    @Override
    public void deleteById(long id) {
        em.remove(findById(id).orElseThrow(DataNotFoundRepositoryException::new));
    }
}
