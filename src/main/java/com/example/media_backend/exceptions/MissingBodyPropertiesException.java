package com.example.media_backend.exceptions;

public class MissingBodyPropertiesException extends Exception {
    public MissingBodyPropertiesException(String message) {
        super(message);
    }

    public MissingBodyPropertiesException(String message, Throwable cause) {
        super(message, cause);
    }
}
