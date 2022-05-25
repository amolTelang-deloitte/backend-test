package com.luggageBuddy.application.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class SpaceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public SpaceException(String message) {
        super(message);
    }
}
