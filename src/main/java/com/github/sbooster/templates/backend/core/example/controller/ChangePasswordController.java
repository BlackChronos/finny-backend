package com.github.sbooster.templates.backend.core.example.controller;

import com.github.sbooster.templates.backend.core.example.CredentialsService;
import com.github.sbooster.templates.backend.core.example.exception.InvalidPasswordException;
import com.github.sbooster.templates.backend.core.example.model.Credentials;
import com.github.sbooster.templates.backend.core.example.model.CredentialsToken;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Pattern;

@Controller
public class ChangePasswordController {
    private final CredentialsService credentialsService;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordController(CredentialsService credentialsService, PasswordEncoder passwordEncoder) {
        this.credentialsService = credentialsService;
        this.passwordEncoder = passwordEncoder;
    }

    @MessageMapping("changePassword")
    @PreAuthorize("isAuthenticated()")
    public Mono<CredentialsToken> changePassword(@AuthenticationPrincipal Credentials credentials, @RequestBody ChangePasswordRequest request) {
        if (!this.passwordEncoder.matches(request.currentPassword, credentials.getPassword())) {
            return Mono.error(new InvalidPasswordException("Invalid current password"));
        }
        return credentialsService.changePassword(credentials, request.newPassword).map(Credentials::generateToken);
    }

    public record ChangePasswordRequest(
            String currentPassword,
            @Pattern(regexp = "sbooster.validator.password.regexp", message = "sbooster.validator.password.error")
            String newPassword) {

    }
}
