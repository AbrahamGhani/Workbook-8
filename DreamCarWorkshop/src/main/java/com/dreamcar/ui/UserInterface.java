package com.dreamcar.ui;

import com.dreamcar.data.ContractFileManager;
import com.dreamcar.data.DealershipFileManager;
import com.dreamcar.model.*;

import java.util.*;

public class UserInterface {
    private Dealership dealership;

    static final Scanner scanner = new Scanner(System.in);

    public void display() {
        init();
        int choice = -1;

        while (choice != 99) {
            displayMenu();
            try {
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Clear newline
                switch (choice) {
                    case 1 -> processPriceRangeRequest();
                    case 2 -> processMakeModelRequest();
                    case 3 -> processYearRangeRequest();
                    case 4 -> processColorRequest();
                    case 5 -> processMileageRangeRequest();
                    case 6 -> processTypeRequest();
                    case 7 -> processAllVehiclesRequest();
                    case 8 -> processAddVehicleRequest();
                    case 9 -> processRemoveVehicleRequest();
                    case 10 -> processSellOrLeaseRequest();
                    case 99 -> System.out.println("Goodbye!");
                    default -> System.out.println("Invalid option. Please try again.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }


    private void init() {
        DealershipFileManager fileManager = new DealershipFileManager();
        dealership = fileManager.getDealership();
    }

    private void displayMenu() {
        System.out.println("\n=== Dealership Menu ===");
        System.out.println("1 - Find by Price");
        System.out.println("2 - Find by Make/Model");
        System.out.println("3 - Find by Year Range");
        System.out.println("4 - Find by Color");
        System.out.println("5 - Find by Mileage");
        System.out.println("6 - Find by Type");
        System.out.println("7 - List All Vehicles");
        System.out.println("8 - Add Vehicle");
        System.out.println("9 - Remove Vehicle");
        System.out.println("10 - Sell or Lease a Vehicle");
        System.out.println("99 - Quit");
        System.out.print("Select an option: ");
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        for (Vehicle v : vehicles) {
            System.out.println(v); // You can override `toString()` in Vehicle
        }
    }

    private void processAllVehiclesRequest() {
        List<Vehicle> vehicles = dealership.getAllVehicles();
        displayVehicles(vehicles);
    }

    private void processPriceRangeRequest() {
        double[] priceRange = PromptUser.promptPriceRange();

        List<Vehicle> results = dealership.getVehiclesByPrice(priceRange[0], priceRange[1]);
        displayVehicles(results);
    }

    private void processMakeModelRequest() {
        String[] makeModel = PromptUser.promptMakeModel();
        List<Vehicle> results = dealership.getVehiclesByMakeModel(makeModel[0],makeModel[1]);
        displayVehicles(results);
    }

    private void processYearRangeRequest() {
        System.out.print("Enter Starting Year: ");
        int startYear = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Ending Year: ");
        int endingYear = scanner.nextInt();
        scanner.nextLine();

        List<Vehicle> results = dealership.getVehiclesByYear(startYear, endingYear);
        displayVehicles(results);
    }

    private void processColorRequest() {
        System.out.print("Enter Color: ");
        String color = scanner.nextLine();
        List<Vehicle> results = dealership.getVehiclesByColor(color);
        displayVehicles(results);
    }

    private void processMileageRangeRequest() {
        System.out.print("Enter minimum mileage: ");
        int minMileage = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter maximum mileage: ");
        int maxMileage = scanner.nextInt();
        scanner.nextLine();

        List<Vehicle> results = dealership.getVehiclesByMileage(minMileage, maxMileage);
        displayVehicles(results);
    }

    private void processTypeRequest() {
        System.out.print("Enter vehicle type (Car, Truck, SUV, Van): ");
        String type = scanner.nextLine();

        List<Vehicle> results = dealership.getVehiclesByType(type);
        displayVehicles(results);
    }

    private void processAddVehicleRequest() {
        System.out.print("Enter VIN: ");
        int vin = scanner.nextInt(); scanner.nextLine();

        System.out.print("Enter year: ");
        int year = scanner.nextInt(); scanner.nextLine();

        System.out.print("Enter make: ");
        String make = scanner.nextLine();

        System.out.print("Enter model: ");
        String model = scanner.nextLine();

        System.out.print("Enter type: ");
        String type = scanner.nextLine();

        System.out.print("Enter color: ");
        String color = scanner.nextLine();

        System.out.print("Enter mileage: ");
        int mileage = scanner.nextInt(); scanner.nextLine();

        System.out.print("Enter price: ");
        double price = scanner.nextDouble(); scanner.nextLine();

        Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, mileage, price);
        dealership.addVehicle(vehicle);

        DealershipFileManager dfm = new DealershipFileManager();
        dfm.saveDealership(dealership);

        System.out.println("Vehicle added successfully!");
    }

    private void processRemoveVehicleRequest() {
        System.out.print("Enter VIN of vehicle to remove: ");
        int vin = scanner.nextInt(); scanner.nextLine();

        dealership.removeVehicle(vin);

        DealershipFileManager dfm = new DealershipFileManager();
        dfm.saveDealership(dealership);
        System.out.println("Vehicle removed successfully!");System.out.println("Vehicles remaining:");
        for (Vehicle v : dealership.getAllVehicles()) {
            System.out.println(v.getVin());
        }

    }

    private String getTodayDate() {
        return new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    private void processSellOrLeaseRequest() {
        System.out.print("Enter VIN of the vehicle to sell or lease: ");
        int vin = scanner.nextInt();
        scanner.nextLine();

        Vehicle vehicle = dealership.getVehicleByVin(vin);

        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        // Don't allow leasing cars older than 3 years
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        boolean canLease = vehicle.getYear() >= currentYear - 3;

        System.out.print("Enter customer's full name: ");
        String name = scanner.nextLine();

        System.out.print("Enter customer's email: ");
        String email = scanner.nextLine();

        System.out.print("Is this a (1) Sale or (2) Lease? ");
        int option = scanner.nextInt();
        scanner.nextLine();

        Contract contract;

        if (option == 1) {
            System.out.print("Is the customer financing? (yes/no): ");
            boolean isFinanced = scanner.nextLine().trim().equalsIgnoreCase("yes");

            contract = new SalesContract(
                    getTodayDate(), name, email, vehicle, isFinanced
            );
        } else if (option == 2 && canLease) {
            contract = new LeaseContract(
                    getTodayDate(), name, email, vehicle
            );
        } else {
            System.out.println("Invalid option or vehicle not eligible for leasing.");
            return;
        }

        // Save contract
        ContractFileManager manager = new ContractFileManager();
        manager.saveContract(contract);

        // Remove from inventory
        dealership.removeVehicle(vin);
        new DealershipFileManager().saveDealership(dealership);
        System.out.println("Vehicles remaining:");
        for (Vehicle v : dealership.getAllVehicles()) {
            System.out.println(v.getVin());
        }
        System.out.println("Contract recorded and vehicle removed from inventory.");
    }


}
