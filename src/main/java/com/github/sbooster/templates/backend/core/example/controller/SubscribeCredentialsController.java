package com.github.sbooster.templates.backend.core.example.controller;

import com.github.sbooster.templates.backend.core.example.CredentialsService;
import com.github.sbooster.templates.backend.core.example.model.Credentials;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;

import java.util.Objects;

@Controller
public class SubscribeCredentialsController {
    private final CredentialsService credentialsService;

    public SubscribeCredentialsController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @MessageMapping("subscribeCredentials")
    public Flux<Credentials> subscribeCredentials(@RequestBody SubscribeCredentialsRequest request) {
        return credentialsService.getChanges().getFlux()
                .filter(credentials -> Objects.equals(credentials.getId(), request.id));
    }

    public record SubscribeCredentialsRequest(Long id) {
    }
}
