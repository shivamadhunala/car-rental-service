package com.shiva.carrentalservice.providers.pricingStrategy;

import com.shiva.carrentalservice.models.rentVehicle.RentVehicle;

public class HourlyPricingStrategy implements PricingStrategy{
    @Override
    public double getPrice(RentVehicle rentVehicle, double lateFeePerHour) {
        return 0;
    }
}
