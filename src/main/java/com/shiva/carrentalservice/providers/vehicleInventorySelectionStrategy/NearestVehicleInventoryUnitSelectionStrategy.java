package com.shiva.carrentalservice.providers.vehicleInventorySelectionStrategy;

import com.shiva.carrentalservice.models.Address;
import com.shiva.carrentalservice.models.VehicleInventoryUnit;

import java.util.List;

public class NearestVehicleInventoryUnitSelectionStrategy implements VehicleInventoryUnitSelectionStrategy{
    public VehicleInventoryUnit getVehicleInventoryUnit(List<VehicleInventoryUnit> allInventoryUnits, Address userAddress) {
        VehicleInventoryUnit vehicleInventoryUnit = allInventoryUnits.get(0);
        double minDistance = vehicleInventoryUnit.getAddress().getDistance(userAddress);

        for(int i=1; i< allInventoryUnits.size();i++) {
            double currentDistance = allInventoryUnits.get(i).getAddress().getDistance(userAddress);
            if(currentDistance < minDistance) {
                minDistance = currentDistance;
                vehicleInventoryUnit = allInventoryUnits.get(i);
            }
        }
        return vehicleInventoryUnit;
    }
}
