package com.shiva.carrentalservice;

import com.shiva.carrentalservice.models.Address;
import com.shiva.carrentalservice.models.VehicleInventory;
import com.shiva.carrentalservice.models.VehicleInventoryUnit;
import com.shiva.carrentalservice.models.user.Person;
import com.shiva.carrentalservice.models.user.User;
import com.shiva.carrentalservice.models.vehicle.Vehicle;
import com.shiva.carrentalservice.models.vehicle.VehicleFactory;
import com.shiva.carrentalservice.models.vehicle.VehicleType;
import com.shiva.carrentalservice.providers.orderVehicleStategy.CheapestToCostliestVehicleSorterStrategy;
import com.shiva.carrentalservice.providers.orderVehicleStategy.OrderVehiclesStrategy;
import com.shiva.carrentalservice.providers.vehicleInventorySelectionStrategy.NearestVehicleInventoryUnitSelectionStrategy;
import com.shiva.carrentalservice.providers.vehicleInventorySelectionStrategy.VehicleInventoryUnitSelectionStrategy;
import com.shiva.carrentalservice.providers.vehicleLock.IVehicleLockProvider;
import com.shiva.carrentalservice.providers.vehicleLock.VehicleLockProvider;
import com.shiva.carrentalservice.services.RentVehicleService;
import com.shiva.carrentalservice.services.UserService;
import com.shiva.carrentalservice.services.VehicleInventoryService;
import com.shiva.carrentalservice.services.VehicleService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CarRentalApplicationDriver {
    private final IVehicleLockProvider vehicleLockProvider;

    private final UserService userService;
    private final VehicleService vehicleService;
    private final VehicleInventoryService vehicleInventoryService;
    private final RentVehicleService rentVehicleService;

    private final User shiva, rishi;

    public CarRentalApplicationDriver() {
        this.vehicleLockProvider = VehicleLockProvider.getVehicleLockProviderObj(300);
        userService = new UserService();
        vehicleService = new VehicleService();
        vehicleInventoryService = new VehicleInventoryService();
        rentVehicleService = new RentVehicleService(vehicleInventoryService, vehicleLockProvider);

        Address gachibowli = new Address("Gachibowli", "Hyderabad", "Telangana","500500","India", 10, 0);
        Address kondapur = new Address("Kondapur", "Hyderabad", "Telangana","500501","India", 30, 0);

        createInventoryAndVehicles(gachibowli, createVehicleSet1());
        createInventoryAndVehicles(kondapur, createVehicleSet2());
        shiva = new User(new Person("Shiva",new Address("Gachibowli", "Hyderabad", "Telangana","500500","India", 15, 0), "shiva@gmail.com","9182738937"));
        rishi = new User(new Person("Rishi",new Address("Kondapur", "Hyderabad", "Telangana","500500","India", 25, 0), "rishi@gmail.com","9705106468"));

        checkAndStartBooking(shiva);
    }

    private void checkAndStartBooking(User test){
        VehicleInventoryUnitSelectionStrategy nearestVehicleInventoryUnitSelectionStrategy = new NearestVehicleInventoryUnitSelectionStrategy();
        VehicleInventoryUnit vehicleInventoryUnit = this.rentVehicleService.getInventoryUnit(nearestVehicleInventoryUnitSelectionStrategy, test.getPerson().getAddress());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = LocalDateTime.of(now.getYear(),now.getMonth(), now.getDayOfMonth() + 1, now.getHour(), now.getMinute(),0,0);
        LocalDateTime to = now.plusDays(1);

        OrderVehiclesStrategy cheapestToCostliestVehicleSorterStrategy = new CheapestToCostliestVehicleSorterStrategy();

        HashMap<String, List<Vehicle>> vehicleHashMap = this.rentVehicleService.getVehiclesInInventory(from, to, VehicleType.CAR, vehicleInventoryUnit, cheapestToCostliestVehicleSorterStrategy);

        List<Vehicle> vehicles = new ArrayList<>();

        for(Map.Entry<String, List<Vehicle>> entry : vehicleHashMap.entrySet()) {
            System.out.println("Inventory: " + entry.getKey());
            for(Vehicle vehicle: entry.getValue()) {
                if(vehicles.size() == 0 ) {
                    vehicles.add(vehicle);
                }
                System.out.println("Name: " + vehicle.getName() + ", ");
            }
        }

        this.rentVehicleService.rentVehicle(vehicles, vehicleInventoryUnit, test, from, to);
        System.out.println("System testing!!!");
    }

    private void createInventoryAndVehicles(Address inventoryAddress, List<Vehicle> vehicles) {
        VehicleInventory vehicleInventory = this.vehicleInventoryService.createInventory();
        VehicleInventoryUnit vehicleInventoryUnit = this.vehicleInventoryService.createVehicleInventoryUnit(vehicleInventory, inventoryAddress);

        for(Vehicle vehicle: vehicles) {
            vehicleInventoryUnit.getVehicleInventory().addVehicle(vehicle);
        }

    }

    private List<Vehicle> createVehicleSet1() {
        List<Vehicle> allVehicles = new ArrayList<>();

        allVehicles.add(VehicleFactory.createVehicle(VehicleType.CAR, getRandomMileage(15, 25), getRandomPassengerCapacity(4, 8), "Maruti Suzuki Swift", "ZXI+", getRandomPricePerHour(15, 400)));
        allVehicles.add(VehicleFactory.createVehicle(VehicleType.CAR, getRandomMileage(15, 25), getRandomPassengerCapacity(4, 8), "Hyundai i20", "Asta", getRandomPricePerHour(15, 400)));
        allVehicles.add(VehicleFactory.createVehicle(VehicleType.CAR, getRandomMileage(15, 25), getRandomPassengerCapacity(4, 8), "Tata Tiago", "XZ", getRandomPricePerHour(15, 400)));
        allVehicles.add(VehicleFactory.createVehicle(VehicleType.CAR, getRandomMileage(15, 25), getRandomPassengerCapacity(4, 8), "Honda City", "VX", getRandomPricePerHour(15, 400)));
        allVehicles.add(VehicleFactory.createVehicle(VehicleType.CAR, getRandomMileage(15, 25), getRandomPassengerCapacity(4, 8), "Maruti Suzuki Baleno", "Alpha", getRandomPricePerHour(15, 400)));

        allVehicles.add(VehicleFactory.createVehicle(VehicleType.BIKE, getRandomMileage(30, 60), getRandomPassengerCapacity(2,3), "Bajaj Pulsar 150", "Classic", getRandomPricePerHour(100, 250)));
        allVehicles.add(VehicleFactory.createVehicle(VehicleType.BIKE, getRandomMileage(30, 60), getRandomPassengerCapacity(2,3), "Royal Enfield Classic 350", "Standard", getRandomPricePerHour(100, 250)));

        return allVehicles;
    }

    private List<Vehicle> createVehicleSet2() {
        List<Vehicle> allVehicles = new ArrayList<>();

        allVehicles.add(VehicleFactory.createVehicle(VehicleType.CAR, getRandomMileage(15, 25), getRandomPassengerCapacity(4, 8), "Toyota Corolla", "GLI", getRandomPricePerHour(15, 400)));
        allVehicles.add(VehicleFactory.createVehicle(VehicleType.CAR, getRandomMileage(15, 25), getRandomPassengerCapacity(4, 8), "Honda Civic", "RS", getRandomPricePerHour(15, 400)));
        allVehicles.add(VehicleFactory.createVehicle(VehicleType.CAR, getRandomMileage(15, 25), getRandomPassengerCapacity(4, 8), "Volkswagen Polo", "GTI", getRandomPricePerHour(15, 400)));
        allVehicles.add(VehicleFactory.createVehicle(VehicleType.CAR, getRandomMileage(15, 25), getRandomPassengerCapacity(4, 8), "Ford Mustang", "GT Premium", getRandomPricePerHour(15, 400)));
        allVehicles.add(VehicleFactory.createVehicle(VehicleType.CAR, getRandomMileage(15, 25), getRandomPassengerCapacity(4, 8), "Mercedes-Benz C-Class", "C300", getRandomPricePerHour(15, 400)));

        allVehicles.add(VehicleFactory.createVehicle(VehicleType.BIKE, getRandomMileage(30, 60), getRandomPassengerCapacity(2,3), "KTM Duke 200", "Standard", getRandomPricePerHour(100, 250)));
        allVehicles.add(VehicleFactory.createVehicle(VehicleType.BIKE, getRandomMileage(30, 60), getRandomPassengerCapacity(2,3), "Yamaha FZ-S", "V3", getRandomPricePerHour(100, 250)));

        return allVehicles;
    }

    private static int getRandomMileage(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    private static int getRandomPassengerCapacity(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    private static int getRandomPricePerHour(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }


}
