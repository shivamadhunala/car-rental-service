package com.shiva.carrentalservice.exceptions;

public class VehiclePermanentlyUnavailableException extends RuntimeException {
    public VehiclePermanentlyUnavailableException(String message) {
        super(message);
    }
}
