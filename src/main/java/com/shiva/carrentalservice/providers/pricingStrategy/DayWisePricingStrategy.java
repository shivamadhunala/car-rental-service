package com.shiva.carrentalservice.providers.pricingStrategy;

import com.shiva.carrentalservice.models.rentVehicle.RentVehicle;

public class DayWisePricingStrategy implements PricingStrategy {
    @Override
    public double getPrice(RentVehicle rentVehicle, double lateFee) {
        return 0;
    }
}
