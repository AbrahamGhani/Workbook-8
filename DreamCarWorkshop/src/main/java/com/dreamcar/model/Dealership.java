package com.dreamcar.model;
import java.util.ArrayList;
import java.util.List;

public class Dealership {

    private String name;
    private String address;
    private String phone;
    private List<Vehicle> inventory;

    public Dealership(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.inventory = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        inventory.add(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(inventory);
    }

    // Stubs for later implementation
    public List<Vehicle> getVehiclesByPrice(double min, double max) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle v : getAllVehicles()) {
            if (v.getPrice() >= min && v.getPrice() <= max) {
                results.add(v);
            }
        }
        return results;
    }


    public List<Vehicle> getVehiclesByMakeModel(String make, String model) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle v : getAllVehicles()) {
            if (v.getMake().equalsIgnoreCase(make) && v.getModel().equalsIgnoreCase(model)) {
                results.add(v);
            }
        }
        return results;
    }


    public List<Vehicle> getVehiclesByYear(int min, int max) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle v : getAllVehicles()) {
            if (v.getYear() >= min && v.getYear() <= max) {
                results.add(v);
            }
        }
        return results;

    }

    public List<Vehicle> getVehiclesByColor(String color) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle v : getAllVehicles()) {
            if (v.getColor().equalsIgnoreCase(color)) {
                results.add(v);
            }
        }
        return results;
    }

    public List<Vehicle> getVehiclesByMileage(int min, int max) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle v : getAllVehicles()) {
            if (v.getOdometer() >= min && v.getOdometer() <= max) {
                results.add(v);
            }
        }
        return results;
    }

    public List<Vehicle> getVehiclesByType(String type) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle v : getAllVehicles()) {
            if (v.getType().equalsIgnoreCase(type)) {
                results.add(v);
            }
        }
        return results;
    }

    public Vehicle getVehicleByVin(int vin) {
        for (Vehicle v : getAllVehicles()) {
            if (v.getVin() == vin) {
                return v;
            }
        }
        return null; // not found
    }

    public void removeVehicle(int vin) {
        inventory.removeIf(v -> v.getVin() == vin);
    }

    // Getters and setters for name, address, phone

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
