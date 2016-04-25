package com.tw.resources.exception;

public class InternalException extends RuntimeException{
    public InternalException() {
    }

    public InternalException(String message) {
        super(message);
    }
}
