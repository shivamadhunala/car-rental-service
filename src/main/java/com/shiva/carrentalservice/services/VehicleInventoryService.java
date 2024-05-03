package com.shiva.carrentalservice.services;

import com.shiva.carrentalservice.exceptions.NoSuchElementFoundException;
import com.shiva.carrentalservice.models.Address;
import com.shiva.carrentalservice.models.VehicleInventory;
import com.shiva.carrentalservice.models.VehicleInventoryUnit;
import com.shiva.carrentalservice.models.vehicle.Vehicle;
import com.shiva.carrentalservice.models.vehicle.VehicleType;
import com.shiva.carrentalservice.providers.orderVehicleStategy.OrderVehiclesStrategy;
import com.shiva.carrentalservice.providers.vehicleInventorySelectionStrategy.VehicleInventoryUnitSelectionStrategy;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class VehicleInventoryService {
    private final HashMap<String, VehicleInventoryUnit> allVehiclesInventory;

    public VehicleInventoryService() {
        this.allVehiclesInventory = new HashMap<>();
    }

    public VehicleInventory createInventory() {
        return new VehicleInventory();
    }

    public VehicleInventoryUnit createVehicleInventoryUnit(@NonNull VehicleInventory vehicleInventory, @NonNull Address address) {
        VehicleInventoryUnit vehicleInventoryUnit = new VehicleInventoryUnit(vehicleInventory, address);
        allVehiclesInventory.put(vehicleInventoryUnit.getId(), vehicleInventoryUnit);
        return vehicleInventoryUnit;
    }


    public VehicleInventoryUnit getInventoryUnit(@NonNull VehicleInventoryUnitSelectionStrategy vehicleInventoryUnitSelectionStrategy, @NonNull Address userAddress){
        return vehicleInventoryUnitSelectionStrategy.getVehicleInventoryUnit(new ArrayList<>(allVehiclesInventory.values()), userAddress);
    }

    public List<VehicleInventoryUnit> getInventoryUnits(double maxDistance, Address userAddress){
        List<VehicleInventoryUnit> inventoryUnits = new ArrayList<>();
        for(Map.Entry<String, VehicleInventoryUnit> entry: allVehiclesInventory.entrySet()) {
            if(entry.getValue().getAddress().getDistance(userAddress) <= maxDistance) {
                inventoryUnits.add(entry.getValue());
            }
        }
        return inventoryUnits;
    }
}
