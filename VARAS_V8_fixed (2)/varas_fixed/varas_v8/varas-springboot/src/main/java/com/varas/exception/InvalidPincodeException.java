package com.varas.exception;

public class InvalidPincodeException extends RuntimeException {
    public InvalidPincodeException(String pincode) {
        super("Invalid PINCODE: " + pincode + ". Must be a valid Tirunelveli District pincode.");
    }
}
