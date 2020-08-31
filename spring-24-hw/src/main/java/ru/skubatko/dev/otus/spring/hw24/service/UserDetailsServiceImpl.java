package ru.skubatko.dev.otus.spring.hw24.service;

import ru.skubatko.dev.otus.spring.hw24.domain.User;
import ru.skubatko.dev.otus.spring.hw24.domain.UserDetailsImpl;
import ru.skubatko.dev.otus.spring.hw24.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(userName);
        user.orElseThrow(() -> new UsernameNotFoundException(userName + " not found"));
        return user.map(UserDetailsImpl::new).get();
    }
}
