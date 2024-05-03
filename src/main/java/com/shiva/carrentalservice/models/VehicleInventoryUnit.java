package com.shiva.carrentalservice.models;

import com.shiva.carrentalservice.models.vehicle.Vehicle;
import com.shiva.carrentalservice.models.vehicle.VehicleType;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@Getter
public class VehicleInventoryUnit {
    private final VehicleInventory vehicleInventory;
    private final Address address;
    private final String id;

    public VehicleInventoryUnit(VehicleInventory vehicleInventory, Address address) {
        this.id = UUID.randomUUID().toString();
        this.vehicleInventory = vehicleInventory;
        this.address = address;
    }

    public List<Vehicle> getVehicles(@NonNull VehicleType vehicleType) {
        return this.vehicleInventory.getVehicles(vehicleType);
    }

}
