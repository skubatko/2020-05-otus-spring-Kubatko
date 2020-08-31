package ru.skubatko.dev.otus.spring.hw24.repository;

import ru.skubatko.dev.otus.spring.hw24.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);
}
