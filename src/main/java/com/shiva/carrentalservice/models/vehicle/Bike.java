package com.shiva.carrentalservice.models.vehicle;

import lombok.NonNull;

public class Bike extends Vehicle {

    public Bike(@NonNull VehicleType vehicleType, int mileage, int passengerCapacity, @NonNull String name, @NonNull String model, double pricePerHour) {
        super(vehicleType, mileage, passengerCapacity, name, model, pricePerHour);
    }
}
