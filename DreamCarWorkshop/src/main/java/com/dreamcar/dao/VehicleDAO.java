package com.dreamcar.dao;

import com.dreamcar.model.Vehicle;
import com.dreamcar.ui.PromptUser;

import java.sql.*;
import java.util.*;

public class VehicleDAO {

    static DatabaseConnection db = new DatabaseConnection();
    static String url = db.getUrl();

    private void processPriceRangeRequest() {
        // Define the SQL query to retrieve anything from vehicles where the price range is between a range to be set
        String query = "SELECT * " +
                "FROM AG.Vehicles " +
                "WHERE Price " +
                "BETWEEN ? AND ?";

        // Use try-with-resources to ensure the database connection is closed automatically
        try (Connection conn = DriverManager.getConnection(url)) {

            // Prepare the SQL statement to prevent SQL injection and allow parameter substitution
            PreparedStatement stmt = conn.prepareStatement(query);

            double[] priceRange = PromptUser.promptPriceRange();
            stmt.setDouble(1, priceRange[0]);
            stmt.setDouble(2, priceRange[1]);
            // Execute the query and store the result in a ResultSet object
            ResultSet rs = stmt.executeQuery();

            // Print a header to clearly indicate which suspects are being listed
            System.out.println("\n--- Vehicles in price range---");

            // Iterate through each row in the result set
            while (rs.next()) {
                // Print the data in formatted columns: Make,Model,Price
                // %-20s reserves 20 character width for Make and Model to align the output neatly
                System.out.printf("%-20s %-20s %.2f\n",
                        rs.getString("Make"),       // Retrieve the "Make" column as a String
                        rs.getString("Model"),      // Retrieve the "Model" column as a String
                        rs.getDouble("Price"));  // Retrieve the "Price" column as an int
            }

        } catch (SQLException e) {
            // Catch and print any SQL exceptions that occur during query execution or connection
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void processMakeModelRequest() {
        // Define the SQL query to retrieve anything from vehicles where the price range is between a range to be set
        String query = "SELECT * " +
                "FROM AG.Vehicles " +
                "WHERE Make LIKE ? " +
                "OR Model LIKE ?";

        // Use try-with-resources to ensure the database connection is closed automatically
        try (Connection conn = DriverManager.getConnection(url)) {

            // Prepare the SQL statement to prevent SQL injection and allow parameter substitution
            PreparedStatement stmt = conn.prepareStatement(query);

            String[] MakeModel = PromptUser.promptMakeModel();
            stmt.setString(1, MakeModel[0]);
            stmt.setString(2, MakeModel[1]);
            // Execute the query and store the result in a ResultSet object
            ResultSet rs = stmt.executeQuery();

            // Print a header to clearly indicate which suspects are being listed
            System.out.println("\n--- Vehicles that have requested make or model---");

            // Iterate through each row in the result set
            while (rs.next()) {
                // Print the data in formatted columns: Make,Model,Price
                // %-20s reserves 20 character width for Make and Model to align the output neatly
                System.out.printf("%-20s %-20s %.2f\n",
                        rs.getString("Make"),       // Retrieve the "Make" column as a String
                        rs.getString("Model"),      // Retrieve the "Model" column as a String
                        rs.getDouble("Price"));  // Retrieve the "Price" column as an int
            }

        } catch (SQLException e) {
            // Catch and print any SQL exceptions that occur during query execution or connection
            System.err.println("Error: " + e.getMessage());
        }
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

}
