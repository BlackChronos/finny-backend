package com.github.sbooster.templates.backend.core.account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.sbooster.templates.backend.core.account.exception.AccountAlreadyExistsException;
import com.github.sbooster.templates.backend.core.account.exception.AccountNotFoundException;
import com.github.sbooster.templates.backend.core.account.model.Account;
import com.github.sbooster.templates.backend.core.account.model.AccountToken;
import com.github.sbooster.templates.backend.core.account.model.AccountType;
import com.github.sbooster.templates.backend.core.account.repository.AccountRepository;
import com.github.sbooster.templates.backend.rsocket.security.service.ReactiveCredentialsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@Service
public class AccountService implements ReactiveCredentialsService<Account> {
    private final AccountRepository accountRepository;
    private final Function<String, String> passwordEncoder;
    private final Algorithm algorithm;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, Algorithm algorithm) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder::encode;
        this.algorithm = algorithm;
    }

    public Mono<Account> create(String username, String password, AccountType... types) {
        return this.accountRepository.existsByUsernameEqualsIgnoreCase(username)
                .filter(value -> !value)
                .switchIfEmpty(new AccountAlreadyExistsException("Account with username {} already exists", username).toMono())
                .flatMap(value -> {
                            Set<AccountType> authorities = new HashSet<>(Arrays.asList(types));
                            return this.save(
                                    Account.builder()
                                            .enabled(true)
                                            .locked(false)
                                            .createdAt(Instant.now())
                                            .username(username)
                                            .password(this.passwordEncoder.apply(password))
                                            .authorities(authorities)
                                            .build()
                            );
                        }
                );
    }

    @Override
    public Mono<Account> getById(Long id) {
        return this.accountRepository.findById(id)
                .switchIfEmpty(new AccountNotFoundException("Account with accountId {} is missing", id).toMono());
    }

    @Override
    public Mono<Account> getByUsername(String username) {
        return this.accountRepository.findByUsername(username)
                .switchIfEmpty(new AccountNotFoundException("Account with username {} is missing", username).toMono());
    }

    @Override
    public Mono<Account> save(Account account) {
        return this.accountRepository.save(account);
    }

    @Override
    public Mono<Account> changePassword(Account account, String password) {
        account.setPassword(this.passwordEncoder.apply(password));
        return this.save(account);
    }

    public Mono<AccountToken> getToken(Account account, boolean remember) {
        Instant now = Instant.now();
        return Mono.just(
                new AccountToken(
                        JWT.create()
                                .withSubject(String.valueOf(account.getId()))
                                .withIssuedAt(now)
                                .withExpiresAt(now.plus(Duration.of(remember ? 62 : 1, ChronoUnit.HALF_DAYS)))
                                .withClaim("ema", account.getUsername())
                                .withClaim("pwd", account.getPassword())
                                .withArrayClaim("typ", account.getAuthorities()
                                        .stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toArray(String[]::new)
                                )
                                .sign(algorithm)
                )
        );
    }
}
