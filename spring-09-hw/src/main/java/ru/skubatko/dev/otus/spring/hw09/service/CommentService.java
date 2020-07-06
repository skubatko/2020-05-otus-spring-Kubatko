package ru.skubatko.dev.otus.spring.hw09.service;

import ru.skubatko.dev.otus.spring.hw09.domain.Comment;
import ru.skubatko.dev.otus.spring.hw09.repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService implements CrudService<Comment> {

    private final CommentRepository repository;

    @Override
    public Comment findById(long id) {
        return repository.findById(id).orElse(null);
    }

    public Comment findByName(String content) {
        return repository.findByContent(content);
    }

    @Override
    public List<Comment> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void save(Comment comment) {
        repository.save(comment);
    }

    @Override
    @Transactional
    public void update(Comment comment) {
        repository.save(comment);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
