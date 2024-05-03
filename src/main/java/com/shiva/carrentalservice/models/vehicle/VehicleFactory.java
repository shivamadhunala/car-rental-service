package com.shiva.carrentalservice.models.vehicle;

import lombok.NonNull;

public class VehicleFactory {
    public static Vehicle createVehicle(@NonNull VehicleType vehicleType, int mileage, int passengerCapacity, String name, String model, double pricePerHour){
        if(vehicleType.equals(VehicleType.BIKE)){
            return new Bike(vehicleType, mileage, passengerCapacity, name, model, pricePerHour);
        } else if(vehicleType.equals(VehicleType.CAR)) {
            return new Car(vehicleType, mileage, passengerCapacity, name, model, pricePerHour);
        } else if(vehicleType.equals(VehicleType.TRUCK)) {
            return new Truck(vehicleType, mileage, passengerCapacity, name, model, pricePerHour);
        } else {
            return null; // ideally an exception should be thrown!
        }
    }
}
