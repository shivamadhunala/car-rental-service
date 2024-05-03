package com.shiva.carrentalservice.models;

import com.shiva.carrentalservice.models.vehicle.Vehicle;
import com.shiva.carrentalservice.models.vehicle.VehicleFactory;
import com.shiva.carrentalservice.models.vehicle.VehicleType;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleInventory {
    private final HashMap<String, Vehicle> allVehicles;

    public VehicleInventory() {
        this.allVehicles = new HashMap<>();
    }

    public void addVehicle(@NonNull Vehicle vehicle){
        allVehicles.put(vehicle.getRegistrationId(), vehicle);
    }

    public List<Vehicle> getVehicles(VehicleType vehicleType) {
        List<Vehicle> vehicles = new ArrayList<>();
        for(Map.Entry<String, Vehicle> entry : allVehicles.entrySet()) {
            if(entry.getValue().getVehicleType().equals(vehicleType)) {
                vehicles.add(entry.getValue());
           }
        }
        return vehicles;
    }

}
