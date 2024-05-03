package com.shiva.carrentalservice.models.rentVehicle;

import com.shiva.carrentalservice.exceptions.IllegalStateException;
import com.shiva.carrentalservice.models.VehicleInventoryUnit;
import com.shiva.carrentalservice.models.user.User;
import com.shiva.carrentalservice.models.vehicle.Vehicle;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class RentVehicle {
    private final List<Vehicle> vehicles;
    private final LocalDateTime from;
    private final LocalDateTime to;

    @Setter
    private  LocalDateTime departedAt;
    @Setter
    private  LocalDateTime arrivedAt;
    private RentVehicleStatus rentVehicleStatus;
    private final String id;
    private final VehicleInventoryUnit vehicleInventoryUnit;
    private final User user;

    public RentVehicle(List<Vehicle> vehicles, LocalDateTime from, LocalDateTime to, VehicleInventoryUnit vehicleInventoryUnit, User user) {
        this.id = UUID.randomUUID().toString();
        this.vehicleInventoryUnit = vehicleInventoryUnit;
        this.vehicles = vehicles;
        this.from = from;
        this.to = to;
        this.rentVehicleStatus = RentVehicleStatus.CREATED;
        this.user = user;
    }

    public void cancelReservation(){
        if(!this.rentVehicleStatus.equals(RentVehicleStatus.CREATED)) {
            throw new IllegalStateException("Booking: " + this.id + " cannot be cancelled as rentVehicleStatus is " + this.rentVehicleStatus);
        }

        this.rentVehicleStatus = RentVehicleStatus.CANCELLED;
    }

    public void confirmReservation(){
        if(!this.rentVehicleStatus.equals(RentVehicleStatus.CREATED)) {
            throw new IllegalStateException("Booking: " + this.id + " cannot be confirmed as rentVehicleStatus is " + this.rentVehicleStatus);
        }

        this.rentVehicleStatus = RentVehicleStatus.CONFIRMED;
    }

}
