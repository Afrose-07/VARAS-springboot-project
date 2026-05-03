package com.varas.exception;

public class AccidentNotFoundException extends RuntimeException {
    public AccidentNotFoundException(String caseId) {
        super("Accident not found with Case ID: " + caseId);
    }
}
