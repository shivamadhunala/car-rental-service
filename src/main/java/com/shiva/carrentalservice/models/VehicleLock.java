package com.shiva.carrentalservice.models;

import com.shiva.carrentalservice.models.vehicle.Vehicle;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class VehicleLock {
    private final String id;
    private final int timeout;
    private final LocalDateTime lockedAt;
    private final LocalDateTime from;
    private final LocalDateTime to;
    private final Vehicle vehicle;

    public VehicleLock(Vehicle vehicle, int timeout, LocalDateTime lockedAt, LocalDateTime from, LocalDateTime to) {
        this.id = UUID.randomUUID().toString();
        this.timeout = timeout;
        this.lockedAt = lockedAt;
        this.vehicle = vehicle;
        this.from = from;
        this.to = to;
    }

    public boolean isExpired(){
        LocalDateTime expiresAt = lockedAt.plusSeconds(timeout);
        LocalDateTime now = LocalDateTime.now();
        return expiresAt.isBefore(now);
    }

}
