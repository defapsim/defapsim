package com.defapsim.exceptions;

public class ApplicationDoesNotExistException extends RuntimeException {

    public ApplicationDoesNotExistException(String message) {
        super(message);
    }
}
