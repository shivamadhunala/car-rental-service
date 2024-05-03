package com.shiva.carrentalservice.models.vehicle;

import lombok.NonNull;

public class Car extends Vehicle {
    public Car(@NonNull VehicleType vehicleType, int mileage, int passengerCapacity, @NonNull String name, @NonNull String model, double pricePerHour) {
        super(vehicleType, mileage, passengerCapacity, name, model, pricePerHour);
    }
}
