package com.github.sbooster.templates.backend.core.account.exception;


import com.github.sbooster.templates.backend.exception.AbstractMonoException;

public class BadCredentialsException extends AbstractMonoException {
    public BadCredentialsException(String message, Object... arguments) {
        super(message, arguments);
    }
}
