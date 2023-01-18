package com.github.sbooster.templates.backend;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class Application {

    public Application() {
        this.disableReactorErrors();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public void disableReactorErrors() {
        Hooks.onErrorDropped(e -> {
        });
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Algorithm algorithm(@Value("${sbooster.jwt.secret}") String secret) {
        return Algorithm.HMAC256(secret);
    }
}
