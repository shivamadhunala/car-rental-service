package com.shiva.carrentalservice.services;

import com.shiva.carrentalservice.exceptions.NoSuchElementFoundException;
import com.shiva.carrentalservice.exceptions.VehiclePermanentlyUnavailableException;
import com.shiva.carrentalservice.exceptions.VehicleTemporarilyUnavailableException;
import com.shiva.carrentalservice.models.Address;
import com.shiva.carrentalservice.models.SearchInventory;
import com.shiva.carrentalservice.models.VehicleInventoryUnit;
import com.shiva.carrentalservice.models.rentVehicle.RentVehicle;
import com.shiva.carrentalservice.models.user.User;
import com.shiva.carrentalservice.models.vehicle.Vehicle;
import com.shiva.carrentalservice.models.vehicle.VehicleType;
import com.shiva.carrentalservice.providers.orderVehicleStategy.OrderVehiclesStrategy;
import com.shiva.carrentalservice.providers.vehicleInventorySelectionStrategy.VehicleInventoryUnitSelectionStrategy;
import com.shiva.carrentalservice.providers.vehicleLock.IVehicleLockProvider;
import com.shiva.carrentalservice.providers.vehicleLock.VehicleLockProvider;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class RentVehicleService implements SearchInventory {
    private final VehicleInventoryService vehicleInventoryService;
    private final IVehicleLockProvider lockVehicleProvider;
    private final HashMap<String, RentVehicle> rentalBookings;
    private final HashMap<String, TreeMap<Long,RentVehicle>> rentedVehiclesById;

    public RentVehicleService(VehicleInventoryService vehicleInventoryService, IVehicleLockProvider lockVehicleProvider) {
        this.vehicleInventoryService = vehicleInventoryService;
        this.lockVehicleProvider = lockVehicleProvider;
        this.rentalBookings = new HashMap<>();
        this.rentedVehiclesById = new HashMap<>();
    }

    public RentVehicle rentVehicle(List<Vehicle> vehicles, VehicleInventoryUnit vehicleInventoryUnit, User user, LocalDateTime from, LocalDateTime to){

       // Check if all of this vehicles can be rented, A vehicle cannot be rented if it is already rented or being rented
        for(Vehicle vehicle:vehicles) {
            if(this.lockVehicleProvider.isVehicleLocked(vehicle, from, to,user)) {
                throw new VehicleTemporarilyUnavailableException("Vehicle: " + vehicle.getRegistrationId() + " is unavailable temporarily!");
            } else if(isVehicleBooked(vehicle, from, to)){
                throw new VehiclePermanentlyUnavailableException("Vehicle: " + vehicle.getRegistrationId() + " is already booked!");
            }
        }

       // Lock these vehicles temporarily
        for(Vehicle vehicle:vehicles) {
            this.lockVehicleProvider.lockVehicle(vehicle,from,to,user);
        }

        RentVehicle rentVehicle = new RentVehicle(vehicles,from, to, vehicleInventoryUnit, user);
        this.rentalBookings.put(rentVehicle.getId(), rentVehicle);
        return  rentVehicle;
    }

    public RentVehicle getRentalBooking(String rentalBookingId) {
        if(!this.rentalBookings.containsKey(rentalBookingId)) {
            throw new NoSuchElementFoundException("RentVehicle: " + rentalBookingId + " not found");
        }
        return this.rentalBookings.get(rentalBookingId);
    }

    public void confirmRentalBooking(String rentalBookingId) {
        if(!this.rentalBookings.containsKey(rentalBookingId)) {
            throw new NoSuchElementFoundException("RentVehicle: " + rentalBookingId + " not found");
        }
        RentVehicle rentVehicle =  this.rentalBookings.get(rentalBookingId);
        rentVehicle.confirmReservation();
        this.lockVehicleProvider.unlockVehicle(rentVehicle);

        for(Vehicle vehicle: rentVehicle.getVehicles()) {
            this.rentedVehiclesById.computeIfAbsent(vehicle.getRegistrationId(), k -> new TreeMap<>()).put(rentVehicle.getFrom().toInstant(ZoneOffset.UTC).toEpochMilli(),rentVehicle);
        }

    }

    public void cancelBooking(String rentalBookingId) {
        if(!this.rentalBookings.containsKey(rentalBookingId)) {
            throw new NoSuchElementFoundException("RentVehicle: " + rentalBookingId + " not found");
        }
        RentVehicle rentVehicle =  this.rentalBookings.get(rentalBookingId);
        rentVehicle.confirmReservation();
        this.lockVehicleProvider.unlockVehicle(rentVehicle);
    }

    @Override
    public HashMap<String, List<Vehicle>> getVehiclesByType(LocalDateTime from, LocalDateTime to, VehicleType vehicleType, Address userAddress, double maxDistance, OrderVehiclesStrategy orderVehiclesStrategy) {

        List<VehicleInventoryUnit> allVehicleInventoryUnits = this.vehicleInventoryService.getInventoryUnits(maxDistance, userAddress); // Get all the inventories which are in 'maxDistance'
        HashMap<String, List<Vehicle>> inventoryWiseVehicles = new HashMap<>(); // store vehicles inventory wise

        for(VehicleInventoryUnit vehicleInventoryUnit: allVehicleInventoryUnits) {
            List<Vehicle> vehicles = vehicleInventoryUnit.getVehicleInventory().getVehicles(vehicleType);

            List<Vehicle> availableVehicles = new ArrayList<>();
            for(Vehicle vehicle: vehicles){
                if(!this.isVehicleBooked(vehicle, from, to) && !this.lockVehicleProvider.isVehicleLocked(vehicle, from, to,null)){
                    availableVehicles.add(vehicle);
                }
            }

            availableVehicles = orderVehiclesStrategy.getVehicles(availableVehicles);
            if(!availableVehicles.isEmpty()) {
                inventoryWiseVehicles.put(vehicleInventoryUnit.getId(), availableVehicles);
            }
        }

        return inventoryWiseVehicles;
    }

    @Override
    public HashMap<String, List<Vehicle>> getVehiclesInInventory(LocalDateTime from, LocalDateTime to, VehicleType vehicleType, Address userAddress, VehicleInventoryUnitSelectionStrategy vehicleInventoryUnitSelectionStrategy, OrderVehiclesStrategy orderVehiclesStrategy) {
        VehicleInventoryUnit vehicleInventoryUnit = vehicleInventoryService.getInventoryUnit(vehicleInventoryUnitSelectionStrategy, userAddress);
        List<Vehicle> allVehicles = new ArrayList<>();

        for(Vehicle vehicle: vehicleInventoryUnit.getVehicles(vehicleType)){
            if(!this.isVehicleBooked(vehicle, from, to) && !this.lockVehicleProvider.isVehicleLocked(vehicle, from, to,null)){
                allVehicles.add(vehicle);
            }
        }

        allVehicles = orderVehiclesStrategy.getVehicles(allVehicles);
        HashMap<String, List<Vehicle>> hashMap = new HashMap<>();
        hashMap.put(vehicleInventoryUnit.getId(), allVehicles);
        return hashMap;
    }

    public VehicleInventoryUnit getInventoryUnit(@NonNull VehicleInventoryUnitSelectionStrategy vehicleInventoryUnitSelectionStrategy, @NonNull Address userAddress){
        return this.vehicleInventoryService.getInventoryUnit(vehicleInventoryUnitSelectionStrategy, userAddress);
    }

    public HashMap<String, List<Vehicle>> getVehiclesInInventory(LocalDateTime from, LocalDateTime to, VehicleType vehicleType, VehicleInventoryUnit vehicleInventoryUnit, OrderVehiclesStrategy orderVehiclesStrategy) {
        List<Vehicle> allVehicles = new ArrayList<>();
        for(Vehicle vehicle: vehicleInventoryUnit.getVehicles(vehicleType)){
            if(!this.isVehicleBooked(vehicle, from, to) && !this.lockVehicleProvider.isVehicleLocked(vehicle, from, to,null)){
                allVehicles.add(vehicle);
            }
        }

        allVehicles = orderVehiclesStrategy.getVehicles(allVehicles);
        HashMap<String, List<Vehicle>> hashMap = new HashMap<>();
        hashMap.put(vehicleInventoryUnit.getId(), allVehicles);
        return hashMap;
    }

    public boolean isVehicleBooked(Vehicle vehicle, LocalDateTime from, LocalDateTime to){
        if(this.rentedVehiclesById.isEmpty()) {
            return false;
        }

        TreeMap<Long, RentVehicle> rentedVehicles = this.rentedVehiclesById.get(vehicle.getRegistrationId());
        if(rentedVehicles == null || rentedVehicles.isEmpty()){
            return false;
        }

        Map.Entry<Long, RentVehicle> floor = rentedVehicles.floorEntry(from.toEpochSecond(ZoneOffset.UTC));
        if(floor == null) {
            floor = rentedVehicles.firstEntry();
        }

        long fromTimeInMillis = from.toInstant(ZoneOffset.UTC).toEpochMilli();
        long ToTimeInMillis = to.toInstant(ZoneOffset.UTC).toEpochMilli();
        Map.Entry<Long, RentVehicle> entry =  floor;
        while(entry != null ){
            long existingFrom = entry.getKey();
            long existingTo = entry.getValue().getTo().toInstant(ZoneOffset.UTC).toEpochMilli();;

            if(fromTimeInMillis <= existingFrom) {
                if(existingFrom <= ToTimeInMillis){
                    return true;
                }
            } else {
                if(fromTimeInMillis <=  existingTo) {
                    return true;
                }
            }

            if(ToTimeInMillis < existingFrom){
                break;
            }

            entry = rentedVehicles.higherEntry(entry.getKey());
        }
        return false;
    }

}
