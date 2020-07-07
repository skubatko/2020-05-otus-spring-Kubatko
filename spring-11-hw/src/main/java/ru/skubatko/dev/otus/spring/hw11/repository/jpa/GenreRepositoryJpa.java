package ru.skubatko.dev.otus.spring.hw11.repository.jpa;

import ru.skubatko.dev.otus.spring.hw11.domain.Genre;
import ru.skubatko.dev.otus.spring.hw11.exception.DataNotFoundRepositoryException;
import ru.skubatko.dev.otus.spring.hw11.repository.GenreRepository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Genre> findById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public Genre findByName(String name) {
        return em.createQuery("SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
                       .setParameter("name", name)
                       .getSingleResult();
    }

    @Override
    public List<Genre> findAll() {
        return em.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() <= 0L) {
            em.persist(genre);
            return genre;
        } else {
            return em.merge(genre);
        }
    }

    @Override
    public void deleteById(long id) {
        em.remove(findById(id).orElseThrow(DataNotFoundRepositoryException::new));
    }
}
