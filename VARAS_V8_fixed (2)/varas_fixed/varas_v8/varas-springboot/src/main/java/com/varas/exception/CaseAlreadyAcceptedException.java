package com.varas.exception;

public class CaseAlreadyAcceptedException extends RuntimeException {
    public CaseAlreadyAcceptedException(String caseId, String acceptedBy) {
        super("Case " + caseId + " has already been accepted by: " + acceptedBy);
    }
}
