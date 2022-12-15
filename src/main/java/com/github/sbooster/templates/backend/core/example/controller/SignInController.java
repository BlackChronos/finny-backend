package com.github.sbooster.templates.backend.core.example.controller;

import com.github.sbooster.templates.backend.core.example.CredentialsService;
import com.github.sbooster.templates.backend.core.example.exception.InvalidPasswordException;
import com.github.sbooster.templates.backend.core.example.model.Credentials;
import com.github.sbooster.templates.backend.core.example.model.CredentialsToken;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Pattern;

@Controller
public class SignInController {
    private final CredentialsService credentialsService;
    private final PasswordEncoder passwordEncoder;

    public SignInController(CredentialsService credentialsService, PasswordEncoder passwordEncoder) {
        this.credentialsService = credentialsService;
        this.passwordEncoder = passwordEncoder;
    }

    @MessageMapping("signIn")
    @PreAuthorize("isAnonymous()")
    public Mono<CredentialsToken> signIn(@RequestBody SignInRequest request) {
        return credentialsService.getByUsername(request.email)
                .filter(credentials -> passwordEncoder.matches(request.password, credentials.getPassword()))
                .switchIfEmpty(Mono.error(() -> new InvalidPasswordException("Invalid email or password")))
                .map(Credentials::generateToken);
    }

    public record SignInRequest(
            @Pattern(regexp = "sbooster.validator.email.regexp", message = "sbooster.validator.email.error")
            String email,
            @Pattern(regexp = "sbooster.validator.password.regexp", message = "sbooster.validator.password.error")
            String password) {

    }
}
