package com.shiva.carrentalservice.providers.vehicleInventorySelectionStrategy;

import com.shiva.carrentalservice.models.Address;
import com.shiva.carrentalservice.models.VehicleInventoryUnit;

import java.util.List;

public interface VehicleInventoryUnitSelectionStrategy {
    public VehicleInventoryUnit getVehicleInventoryUnit(List<VehicleInventoryUnit> allInventoryUnits, Address userAddress);
}
