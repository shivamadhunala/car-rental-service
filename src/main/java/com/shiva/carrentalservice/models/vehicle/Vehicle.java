package com.shiva.carrentalservice.models.vehicle;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Getter
public abstract class Vehicle {
    private final String registrationId;
    private final VehicleType vehicleType;

    @Setter
    private VehicleCondition vehicleCondition;

    private final int mileage;
    private final int passengerCapacity;

    private final String name, model;

    @Setter
    private double pricePerHour;
    @Setter
    private double pricePerDay;

    // other meta - data

    public Vehicle(@NonNull VehicleType vehicleType, int mileage, int passengerCapacity, @NonNull String name, @NonNull String model, double pricePerHour) {
        this.registrationId = UUID.randomUUID().toString();
        this.vehicleType = vehicleType;
        this.mileage = mileage;
        this.passengerCapacity = passengerCapacity;
        this.name = name;
        this.model = model;
        this.pricePerHour = pricePerHour;
        this.vehicleCondition = VehicleCondition.IN_SERVICE;
    }

    // Some other abstract methods here
}
