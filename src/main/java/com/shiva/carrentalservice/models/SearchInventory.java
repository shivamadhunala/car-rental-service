package com.shiva.carrentalservice.models;

import com.shiva.carrentalservice.models.vehicle.Vehicle;
import com.shiva.carrentalservice.models.vehicle.VehicleType;
import com.shiva.carrentalservice.providers.orderVehicleStategy.OrderVehiclesStrategy;
import com.shiva.carrentalservice.providers.vehicleInventorySelectionStrategy.VehicleInventoryUnitSelectionStrategy;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public interface SearchInventory {
    public HashMap<String, List<Vehicle>> getVehiclesByType(LocalDateTime from, LocalDateTime to, VehicleType vehicleType, Address userAddress, double maxDistance, OrderVehiclesStrategy orderVehiclesStrategy);
    public HashMap<String, List<Vehicle>> getVehiclesInInventory(LocalDateTime from, LocalDateTime to, VehicleType vehicleType, Address userAddress, VehicleInventoryUnitSelectionStrategy vehicleInventoryUnitSelectionStrategy, OrderVehiclesStrategy orderVehiclesStrategy);
}
