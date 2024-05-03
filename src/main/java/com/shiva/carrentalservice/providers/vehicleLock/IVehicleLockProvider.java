package com.shiva.carrentalservice.providers.vehicleLock;

import com.shiva.carrentalservice.models.VehicleLock;
import com.shiva.carrentalservice.models.rentVehicle.RentVehicle;
import com.shiva.carrentalservice.models.user.User;
import com.shiva.carrentalservice.models.vehicle.Vehicle;

import java.time.LocalDateTime;

public interface IVehicleLockProvider {
    public void lockVehicle(Vehicle vehicle, LocalDateTime from, LocalDateTime to, User user);
    public void unlockVehicle(RentVehicle rentVehicle);
    public boolean isVehicleLocked(Vehicle vehicle, LocalDateTime from, LocalDateTime to, User user);
}
