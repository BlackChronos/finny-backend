package com.gihub.blackchronos.finny.core.account.exception;


import com.gihub.blackchronos.finny.exception.AbstractMonoException;

public class AccountAlreadyExistsException extends AbstractMonoException {
    public AccountAlreadyExistsException(String message, Object... arguments) {
        super(message, arguments);
    }
}
