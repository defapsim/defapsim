package com.defapsim.exceptions;

public class AlreadyDeployedException extends RuntimeException {

    public AlreadyDeployedException(String message) {
        super(message);
    }
}
