package com.github.sbooster.templates.backend.core.account.controller;

import com.github.sbooster.templates.backend.core.account.AccountService;
import com.github.sbooster.templates.backend.core.account.exception.BadCredentialsException;
import com.github.sbooster.templates.backend.core.account.model.AccountToken;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class SignInController {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @MessageMapping("signIn")
    @PreAuthorize("isAnonymous()")
    public Mono<AccountToken> signIn(@RequestBody SignInRequest request) {
        return this.accountService.getByUsername(request.username)
                .filter(credentials -> this.passwordEncoder.matches(request.password, credentials.getPassword()))
                .switchIfEmpty(new BadCredentialsException("Invalid username or password").toMono())
                .flatMap(account -> this.accountService.getToken(account, request.remember));
    }

    public record SignInRequest(
            @Pattern(regexp = "sbooster.validator.email.regexp", message = "sbooster.validator.email.error")
            String username,
            @Pattern(regexp = "sbooster.validator.password.regexp", message = "sbooster.validator.password.error")
            String password,
            boolean remember
    ) {

    }
}
