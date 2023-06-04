package com.gihub.blackchronos.finny.core.account.exception;


import com.gihub.blackchronos.finny.exception.AbstractMonoException;

public class BadCredentialsException extends AbstractMonoException {
    public BadCredentialsException(String message, Object... arguments) {
        super(message, arguments);
    }
}
