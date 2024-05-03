package com.shiva.carrentalservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private double latitude;
    private double longitude;

    public double getDistance(@NonNull final Address otherAddress) {
        return Math.sqrt( (Math.pow(Math.abs(otherAddress.latitude - this.latitude),2)) + (Math.pow(Math.abs(otherAddress.longitude - this.longitude),2)));
    }
}
