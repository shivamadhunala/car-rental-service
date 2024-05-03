package com.shiva.carrentalservice.services;

import com.shiva.carrentalservice.exceptions.NoSuchElementFoundException;
import com.shiva.carrentalservice.models.vehicle.Vehicle;
import com.shiva.carrentalservice.models.vehicle.VehicleFactory;
import com.shiva.carrentalservice.models.vehicle.VehicleType;
import lombok.NonNull;

import java.util.HashMap;

public class VehicleService {
    private final HashMap<String, Vehicle> allVehicles;

    public VehicleService() {
        this.allVehicles = new HashMap<>();
    }

    public Vehicle createVehicle(@NonNull VehicleType vehicleType, int mileage, int passengerCapacity, @NonNull String name, @NonNull String model, double pricePerHour) {
        Vehicle vehicle = VehicleFactory.createVehicle(vehicleType, mileage, passengerCapacity, name, model, pricePerHour);
        allVehicles.put(vehicle.getRegistrationId(), vehicle);
        return vehicle;
    }

    public Vehicle getVehicle(@NonNull String registrationId) {
        if(!allVehicles.containsKey(registrationId)) {
            throw new NoSuchElementFoundException("Vehicle: " + registrationId + " not found!!!");
        }
        return allVehicles.get(registrationId);
    }

}
