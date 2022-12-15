package com.github.sbooster.templates.backend.core.example;

import com.github.sbooster.templates.backend.core.example.exception.UserNotFoundException;
import com.github.sbooster.templates.backend.core.example.exception.UsernameAlreadyRegisteredException;
import com.github.sbooster.templates.backend.core.example.model.Credentials;
import com.github.sbooster.templates.backend.core.example.repository.CredentialsRepository;
import com.github.sbooster.templates.backend.rsocket.security.service.ReactiveCredentialsService;
import com.github.sbooster.templates.backend.util.MutableFlux;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@Service
public class CredentialsService implements ReactiveCredentialsService<Credentials> {
    @Getter
    private final MutableFlux<Credentials> changes = new MutableFlux<>();
    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;

    public CredentialsService(CredentialsRepository credentialsRepository, PasswordEncoder passwordEncoder) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<Credentials> create(String username, String password) {
        return credentialsRepository.existsByUsernameEqualsIgnoreCase(username)
                .filter(value -> !value)
                .switchIfEmpty(Mono.error(() -> new UsernameAlreadyRegisteredException("User with specified username already registered")))
                .flatMap(value -> save(Credentials.builder()
                        .enabled(true)
                        .locked(false)
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .authorities(new HashSet<>())
                        .build()));
    }

    @Override
    public Mono<Credentials> getById(Long id) {
        return credentialsRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new UserNotFoundException("User with id {} is missing", id)));
    }

    @Override
    public Mono<Credentials> getByUsername(String username) {
        return credentialsRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(() -> new UserNotFoundException("User with username {} is missing", username)));
    }

    public Mono<Credentials> save(Credentials credentials) {
        return credentialsRepository.save(credentials).map(changes::add);
    }

    @Override
    public Mono<Credentials> changePassword(Credentials credentials, String newPassword) {
        credentials.setPassword(passwordEncoder.encode(newPassword));
        return save(credentials);
    }
}
