package com.varas.exception;

public class DuplicateAccidentException extends RuntimeException {
    public DuplicateAccidentException(String vehicle, String pincode, String village) {
        super("Duplicate accident already reported for vehicle: " + vehicle +
              " at " + village + " (" + pincode + ")");
    }
}
