package com.shiva.carrentalservice.providers.orderVehicleStategy;

import com.shiva.carrentalservice.models.vehicle.Vehicle;
import lombok.NonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CheapestToCostliestVehicleSorterStrategy implements OrderVehiclesStrategy {
    @Override
    public List<Vehicle> getVehicles(@NonNull List<Vehicle> vehicles) {
        vehicles.sort(Comparator.comparingDouble(Vehicle::getPricePerHour));
        return vehicles;
    }
}
