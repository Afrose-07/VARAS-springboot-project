package com.varas.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String role) {
        super("Invalid credentials for " + role + " login.");
    }
}
