package com.github.sbooster.templates.backend.core.account.controller;

import com.github.sbooster.templates.backend.core.account.AccountService;
import com.github.sbooster.templates.backend.core.account.model.Account;
import com.github.sbooster.templates.backend.core.account.model.AccountToken;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class ChangePasswordController {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @MessageMapping("changePassword")
    @PreAuthorize("isAuthenticated()")
    public Mono<AccountToken> changePassword(@AuthenticationPrincipal Account credentials,
                                             @RequestBody ChangePasswordRequest request) {
        boolean matches = this.passwordEncoder.matches(request.currentPassword, credentials.getPassword());
        Assert.isTrue(matches, "The current password is incorrect");
        return this.accountService.changePassword(credentials, request.newPassword)
                .flatMap(account -> this.accountService.getToken(account, true));
    }


    public record ChangePasswordRequest(
            String currentPassword,
            @Pattern(regexp = "sbooster.validator.password.regexp", message = "sbooster.validator.password.error")
            String newPassword
    ) {
    }
}
