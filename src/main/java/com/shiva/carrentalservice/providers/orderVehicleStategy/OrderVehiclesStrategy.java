package com.shiva.carrentalservice.providers.orderVehicleStategy;

import com.shiva.carrentalservice.models.vehicle.Vehicle;
import lombok.NonNull;

import java.util.List;

public interface OrderVehiclesStrategy {
    List<Vehicle> getVehicles(@NonNull List<Vehicle> vehicles);
}
