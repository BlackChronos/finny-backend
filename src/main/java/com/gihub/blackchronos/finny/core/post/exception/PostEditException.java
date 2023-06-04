package com.gihub.blackchronos.finny.core.post.exception;

import com.gihub.blackchronos.finny.exception.AbstractMonoException;

public class PostEditException extends AbstractMonoException {
    public PostEditException(String message, Object... arguments) {
        super(message, arguments);
    }
}
