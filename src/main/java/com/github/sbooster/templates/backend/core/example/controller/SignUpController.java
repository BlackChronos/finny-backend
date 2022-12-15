package com.github.sbooster.templates.backend.core.example.controller;

import com.github.sbooster.templates.backend.core.example.CredentialsService;
import com.github.sbooster.templates.backend.core.example.model.Credentials;
import com.github.sbooster.templates.backend.core.example.model.CredentialsToken;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Pattern;

@Controller
public class SignUpController {
    private final CredentialsService credentialsService;

    public SignUpController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @MessageMapping("signUp")
    @PreAuthorize("isAnonymous()")
    public Mono<CredentialsToken> signUp(@RequestBody SignUpRequest request) {
        return credentialsService.create(request.email, request.password).map(Credentials::generateToken);
    }

    public record SignUpRequest(
            @Pattern(regexp = "sbooster.validator.email.regexp", message = "sbooster.validator.email.error")
            String email,
            @Pattern(regexp = "sbooster.validator.password.regexp", message = "sbooster.validator.password.error")
            String password) {

    }
}
