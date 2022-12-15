package com.github.sbooster.templates.backend.core.example.exception;

import org.slf4j.helpers.MessageFormatter;

public class UsernameAlreadyRegisteredException extends RuntimeException {
    public UsernameAlreadyRegisteredException() {
    }

    public UsernameAlreadyRegisteredException(String message, Object... arguments) {
        super(MessageFormatter.arrayFormat(message, arguments).getMessage());
    }
}
