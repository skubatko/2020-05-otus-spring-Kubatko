package ru.skubatko.dev.otus.spring.hw09.repository.jpa;

import ru.skubatko.dev.otus.spring.hw09.domain.Genre;
import ru.skubatko.dev.otus.spring.hw09.repository.GenreRepository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
    public Optional<Genre> findByName(String name) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name = :name", Genre.class);
        query.setParameter("name", name);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Genre save(Genre genre) {
        Optional<Genre> dbGenre = findByName(genre.getName());
        if (dbGenre.isPresent()) {
            update(genre);
        } else {
            em.persist(genre);
        }

        return genre;
    }

    @Override
    public void update(Genre genre) {
        Optional<Genre> dbGenreOptional = findById(genre.getId());
        if (dbGenreOptional.isEmpty()) {
            return;
        }

        Genre dbGenre = dbGenreOptional.get();

        dbGenre.setName(genre.getName());

        em.merge(dbGenre);
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Genre g where g.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public long count() {
        return em.createQuery("select count(g) from Genre g", Long.class).getSingleResult();
    }
}
