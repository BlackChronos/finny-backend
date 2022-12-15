package com.github.sbooster.templates.backend.core.example.exception;

import org.slf4j.helpers.MessageFormatter;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
    }

    public UserNotFoundException(String message, Object... arguments) {
        super(MessageFormatter.arrayFormat(message, arguments).getMessage());
    }
}
