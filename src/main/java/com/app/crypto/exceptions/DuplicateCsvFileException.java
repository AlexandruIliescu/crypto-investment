package com.app.crypto.exceptions;

public class DuplicateCsvFileException extends RuntimeException {

    public DuplicateCsvFileException(String message) {
        super(message);
    }
}