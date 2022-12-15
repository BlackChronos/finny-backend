package com.github.sbooster.templates.backend.core.example.repository;

import com.github.sbooster.templates.backend.core.example.model.Credentials;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CredentialsRepository extends ReactiveMongoRepository<Credentials, Long> {
    Mono<Credentials> findByUsername(String username);

    Mono<Boolean> existsByUsernameEqualsIgnoreCase(String username);
}
