package com.shiva.carrentalservice.exceptions;

public class VehicleTemporarilyUnavailableException extends RuntimeException {
    public VehicleTemporarilyUnavailableException(String message) {
        super(message);
    }
}
