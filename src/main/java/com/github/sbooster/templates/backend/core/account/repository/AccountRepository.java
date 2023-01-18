package com.github.sbooster.templates.backend.core.account.repository;

import com.github.sbooster.templates.backend.core.account.model.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, Long> {
    Mono<Account> findByUsername(String username);

    Mono<Boolean> existsByUsernameEqualsIgnoreCase(String username);
}
