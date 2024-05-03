package com.shiva.carrentalservice.providers.pricingStrategy;

import com.shiva.carrentalservice.models.rentVehicle.RentVehicle;

public interface PricingStrategy {
    public double getPrice(RentVehicle rentVehicle, double lateFee);
}
