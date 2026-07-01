package com.msp.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public BaseException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}