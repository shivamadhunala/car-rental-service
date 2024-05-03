package com.shiva.carrentalservice.providers.vehicleLock;

import com.shiva.carrentalservice.models.VehicleLock;
import com.shiva.carrentalservice.models.rentVehicle.RentVehicle;
import com.shiva.carrentalservice.models.user.User;
import com.shiva.carrentalservice.models.vehicle.Vehicle;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class VehicleLockProvider implements IVehicleLockProvider {

    /**
     * Key: vehicleId, value: TreeMap (Key: start, value: VehicleLock)
     * Simply, here we're storing all 'VehicleLock' of a vehicle in a TreeMap
     */
    private final HashMap<String, TreeMap<Long,VehicleLock>> vehicleWiseLockHashMap;
    private final HashMap<String, VehicleLock> vehicleLockHashMap;

    @Getter
    private final int timeoutInSecs;
    private static volatile IVehicleLockProvider lockVehicleProvider;

    private VehicleLockProvider(int timeoutInSecs) {
        this.vehicleWiseLockHashMap = new HashMap<>();
        this.vehicleLockHashMap = new HashMap<>();
        this.timeoutInSecs = timeoutInSecs;
    }

    @Override
    public synchronized void lockVehicle(Vehicle vehicle, LocalDateTime from, LocalDateTime to, User user) {
        long start = from.toInstant(ZoneOffset.UTC).toEpochMilli();;
        VehicleLock vehicleLock = new VehicleLock(vehicle, timeoutInSecs, LocalDateTime.now(), from, to);
        TreeMap<Long,VehicleLock> allLocks = vehicleWiseLockHashMap.computeIfAbsent(vehicle.getRegistrationId(), (k -> new TreeMap<>()));
        allLocks.put(start, vehicleLock);
        vehicleLockHashMap.put(vehicleLock.getId(), vehicleLock);
    }

    @Override
    public void unlockVehicle(RentVehicle rentVehicle) {
        long start = rentVehicle.getFrom().toInstant(ZoneOffset.UTC).toEpochMilli();
        for(Vehicle vehicle: rentVehicle.getVehicles()) {
            TreeMap<Long,VehicleLock> allLocks = vehicleWiseLockHashMap.get(vehicle.getRegistrationId());
            VehicleLock vehicleLock = allLocks.remove(start);
            if(vehicleLock != null){
                vehicleLockHashMap.remove(vehicleLock.getId());
            }
        }
    }

    public boolean isVehicleLocked(Vehicle vehicle, LocalDateTime from, LocalDateTime to, User user) {
        if(this.vehicleWiseLockHashMap.isEmpty()) {
            return false;
        }

        TreeMap<Long,VehicleLock> allLocks = vehicleWiseLockHashMap.get(vehicle.getRegistrationId());
        if(allLocks == null || allLocks.isEmpty()){
            return false;
        }

        Map.Entry<Long, VehicleLock> floor = allLocks.floorEntry(from.toInstant(ZoneOffset.UTC).toEpochMilli());
        if(floor == null) {
            floor = allLocks.firstEntry();
        }

        Map.Entry<Long, VehicleLock> entry = floor;

        long fromTimeInMillis = from.toInstant(ZoneOffset.UTC).toEpochMilli();
        long ToTimeInMillis = to.toInstant(ZoneOffset.UTC).toEpochMilli();

        while(entry != null ) {

            if(entry.getValue().isExpired()) {
                continue;
            }

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

            entry = allLocks.higherEntry(entry.getKey());
        }

        return false;
    }

    public synchronized static IVehicleLockProvider getVehicleLockProviderObj(int timeoutInSecs){
        if(lockVehicleProvider == null) {
            synchronized (VehicleLockProvider.class) {
                if(lockVehicleProvider == null){
                    lockVehicleProvider = new VehicleLockProvider(timeoutInSecs);
                }
            }
        }
        return lockVehicleProvider;
    }
}
