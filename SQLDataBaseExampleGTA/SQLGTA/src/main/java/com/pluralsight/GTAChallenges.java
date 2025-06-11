package com.pluralsight;  // Define the namespace for this class, grouping related classes together

import java.sql.*;        // Import JDBC classes for database connectivity
import java.util.Scanner;  // Import Scanner to read user input from the console

public class GTAChallenges {       // Main public class containing all methods for the CLI

    // Connection details for SQL Server (JDBC URL)
    private static final String DB_URL =
        "jdbc:sqlserver://skills4it.database.windows.net:1433;" +  // Server address and port
        "database=Courses;" +                                    // Target database name
        "user=gtareader@skills4it;" +                            // Username for authentication
        "password=StrongPass!2025;" +                           // Password (keep this secure!)
        "encrypt=true;" +                                        // Encrypt connection
        "trustServerCertificate=false;" +                        // Do not trust self-signed certs
        "loginTimeout=30;";                                      // Wait time before timeout

    public static void main(String[] args) {
        // Entry point: start here when the program runs
        Scanner inputScanner = new Scanner(System.in);
        // Scanner reads integers from System.in for menu selection

        while (true) {  // Infinite loop: keeps showing menu until user exits
            System.out.println("\n=== Bay City SQL CLI ===");  // Print blank line + header
            System.out.println("1. Suspect Scanner (WHERE)");
            System.out.println("2. Vehicle Watchlist (JOIN + WHERE)");
            System.out.println("3. Reward Tracker (GROUP BY + SUM + ORDER BY)");
            System.out.println("4. Elite Agent Filter (GROUP BY + HAVING)");
            System.out.println("5. Search Person");
            System.out.println("6. Search Vehicle");
            System.out.println("7. Search Vehicles A Person Owns");
            System.out.println("8. Find AVG Mission Payout");
            System.out.println("9. Find Inactive Agents");
            System.out.println("10. Find Top Earning Criminals");
            System.out.println("0. Exit");
            System.out.print("Select mission: ");  // Prompt without newline

            int choice = inputScanner.nextInt();  // Read user's menu choice

            // Decide which method to call based on user input
            switch (choice) {
                case 1 -> runSuspectScanner();           // Call method 1
                case 2 -> runVehicleWatchlist();         // Call method 2
                case 3 -> runRewardTracker();            // Call method 3
                case 4 -> runEliteAgentFilter();         // Call method 4
                case 5 -> searchPerson();                // Call method 5
                case 6 -> searchVehicle();               // Call method 6
                case 7 -> searchAPersonVehicles();       // Call method 7
                case 8 -> runAvgPayout();                // Call method 8
                case 9 -> runInactiveAgentReport();      // Call method 9
                case 10 -> runHighestEarningCriminals(); // Call method 10
                case 0 -> System.exit(0);                // Exit program
                default -> System.out.println("Invalid choice."); // Handle bad input
            }
        }
    }

    public static void runSuspectScanner() {
        // Define the SQL query to retrieve Name, Alias, and WantedLevel for all citizens with WantedLevel >= ?
        String query = "SELECT Name, Alias, WantedLevel FROM GTA.Citizens WHERE WantedLevel >= ?";

        // Use try-with-resources to ensure the database connection is closed automatically
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            // Prepare the SQL statement to prevent SQL injection and allow parameter substitution
            PreparedStatement stmt = conn.prepareStatement(query);

            // Set the first parameter (?) in the query to the integer value 2
            // This means we are searching for citizens with a WantedLevel of 2 or more
            stmt.setInt(1, 2);

            // Execute the query and store the result in a ResultSet object
            ResultSet rs = stmt.executeQuery();

            // Print a header to clearly indicate which suspects are being listed
            System.out.println("\n--- Suspects with WantedLevel >= 2 ---");

            // Iterate through each row in the result set
            while (rs.next()) {
                // Print the data in formatted columns: Name, Alias, and Wanted Level
                // %-20s reserves 20 character width for Name and Alias to align the output neatly
                System.out.printf("%-20s %-20s Wanted Level: %d\n",
                        rs.getString("Name"),       // Retrieve the "Name" column as a String
                        rs.getString("Alias"),      // Retrieve the "Alias" column as a String
                        rs.getInt("WantedLevel"));  // Retrieve the "WantedLevel" column as an int
            }

        } catch (SQLException e) {
            // Catch and print any SQL exceptions that occur during query execution or connection
            System.err.println("Error: " + e.getMessage());
        }
    }


    public static void runVehicleWatchlist() {
        // Define SQL query to select citizen name and their stolen vehicle's type and brand
        String query = "SELECT c.Name, v.Type, v.Brand " +
                "FROM GTA.Citizens c JOIN GTA.Vehicles v ON c.CitizenID = v.OwnerID " +
                "WHERE v.IsStolen = 1";

        // Use try-with-resources to auto-close the DB connection
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            // Prepare the SQL query to safely execute it
            PreparedStatement stmt = conn.prepareStatement(query);

            // Execute the query and retrieve the result set
            ResultSet rs = stmt.executeQuery();

            // Print a header to explain the data being shown
            System.out.println("\n--- Stolen Vehicles and Their Owners ---");

            // Iterate over each row in the result set
            while (rs.next()) {
                // Print the results in a nicely aligned tabular format
                System.out.printf("%-20s %-15s %-15s\n",
                        rs.getString("Name"),   // Get the citizen's name
                        rs.getString("Type"),   // Get the type of the stolen vehicle
                        rs.getString("Brand")); // Get the brand of the stolen vehicle
            }

        } catch (SQLException e) {
            // Handle and print any SQL error that occurs
            System.err.println("Error: " + e.getMessage());
        }
    }


    public static void runRewardTracker() {
        // Define SQL query to get each citizen's total earnings from missions
        String query = "SELECT C.Name, SUM(M.Reward) AS TotalEarnings " +
                "FROM GTA.Citizens C " +
                "JOIN GTA.Assignments A ON C.CitizenID = A.CitizenID " +
                "JOIN GTA.Missions M ON M.MissionID = A.MissionID " +
                "GROUP BY c.Name ORDER BY TotalEarnings DESC";

        // Use try-with-resources for automatic cleanup
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            // Prepare and compile the SQL query
            PreparedStatement stmt = conn.prepareStatement(query);

            // Execute the query and capture the result
            ResultSet rs = stmt.executeQuery();

            // Print a section header
            System.out.println("\n--- Reward Tracker ---");

            // Loop through all the resulting rows
            while (rs.next()) {
                // Display citizen name and their total earnings formatted with 2 decimal places
                System.out.printf("%-20s $%.2f\n",
                        rs.getString("Name"),         // Get the name of the citizen
                        rs.getDouble("TotalEarnings") // Get the total earnings (sum of rewards)
                );
            }

        } catch (SQLException e) {
            // Catch and print any exceptions that occur during DB access
            System.err.println("Error: " + e.getMessage());
        }
    }


    public static void runEliteAgentFilter() {
        // Define SQL query to find elite agents with at least 2 missions and total rewards >= 4000
        String query = "SELECT c.Name, COUNT(*) AS MissionCount, SUM(m.Reward) AS TotalEarnings " +
                "FROM GTA.Citizens c JOIN GTA.Missions m ON c.ID = m.CitizenID " +
                "GROUP BY c.Name HAVING COUNT(*) >= 2 AND SUM(m.Reward) >= 4000";

        // Automatically manage the DB connection using try-with-resources
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            // Prepare the query for execution
            PreparedStatement stmt = conn.prepareStatement(query);

            // Execute the query and store results in a ResultSet
            ResultSet rs = stmt.executeQuery();

            // Print header for elite agents section
            System.out.println("\n--- Elite Agents ---");

            // Iterate over the result set
            while (rs.next()) {
                // Print formatted name, mission count, and total earnings
                System.out.printf("%-20s Missions: %d, Earnings: $%.2f\n",
                        rs.getString("Name"),             // Retrieve citizen name
                        rs.getInt("MissionCount"),        // Number of missions completed
                        rs.getDouble("TotalEarnings"));   // Total reward earnings
            }

        } catch (SQLException e) {
            // Handle SQL errors by printing them to standard error
            System.err.println("Error: " + e.getMessage());
        }
    }


    // ----------------------------
    // Method: searchPerson
    // Purpose: Prompt for a name substring and search in Citizens table
    public static void searchPerson() {
        Scanner scanner = new Scanner(System.in);              // New Scanner for user input
        System.out.print("Enter person name to search: ");    // Prompt user
        String name = scanner.nextLine();                      // Read full line input

        // Use LIKE with wildcards for flexible matching
        String query = "SELECT * FROM GTA.Citizens WHERE Name LIKE ?";
        try (
            Connection conn = DriverManager.getConnection(DB_URL);       // Open DB connection
            PreparedStatement stmt = conn.prepareStatement(query)        // Prepare statement with placeholder
        ) {
            stmt.setString(1, "%" + name + "%");                    // Surround input with % for partial matches
            ResultSet rs = stmt.executeQuery();                         // Execute the query

            System.out.println(" --- Search Results for '" + name + "' ---");
            while (rs.next()) {                                          // Iterate results
                int id = rs.getInt("CitizenID");                      // Citizen ID column
                String fullName = rs.getString("Name");               // Name column
                String alias = rs.getString("Alias");                 // Alias column
                int level = rs.getInt("WantedLevel");                // Wanted level column
                // Print formatted details per person
                System.out.printf("ID: %d, Name: %s, Alias: %s, WantedLevel: %d ", id, fullName, alias, level);
            }
        } catch (SQLException e) {                                     // SQL errors
            System.err.println("Error: " + e.getMessage());          // Print error
        }
    }

    // ----------------------------
    // Method: searchVehicle
    // Purpose: Prompt for brand or type and find matching vehicles
    public static void searchVehicle() {
        Scanner scanner = new Scanner(System.in);                     // Scanner for input
        System.out.print("Enter vehicle brand or type: ");          // Prompt user
        String input = scanner.nextLine();                            // Read input line

        // Query both Brand and Type with OR condition
        String query = "SELECT * FROM GTA.Vehicles WHERE Brand LIKE ? OR Type LIKE ?";

        try (
            Connection conn = DriverManager.getConnection(DB_URL);       // Connect to DB
            PreparedStatement stmt = conn.prepareStatement(query)        // Prepare statement
        ) {
            stmt.setString(1, "%" + input + "%");                    // First placeholder: Brand
            stmt.setString(2, "%" + input + "%");                    // Second placeholder: Type
            ResultSet rs = stmt.executeQuery();                         // Execute

            System.out.println("--- Vehicle Search for '" + input + "' ---");
            while (rs.next()) {                                          // Iterate rows
                int vid = rs.getInt("VehicleID");                     // Vehicle ID column
                String brand = rs.getString("Brand");                 // Brand column
                String type = rs.getString("Type");                   // Type column
                boolean stolen = rs.getBoolean("IsStolen");           // Stolen flag
                // Print vehicle details with status
                System.out.printf("ID: %d, Brand: %s, Type: %s, Stolen: %b ",
                    vid, brand, type, stolen
                );
            }
        } catch (SQLException e) {                                     // Handle exceptions
            System.err.println("Error: " + e.getMessage());          // Display error
        }
    }

    // ----------------------------
    // Method: searchAPersonVehicles
    // Purpose: Given a citizen ID, list all their vehicles
    public static void searchAPersonVehicles() {
        Scanner scanner = new Scanner(System.in);                     // Scanner for ID input
        System.out.print("Enter person ID: ");                      // Prompt for ID
        int id = scanner.nextInt();                                   // Read integer ID

        // Query vehicles where OwnerID matches provided ID
        String query = 
            "SELECT VehicleID, Brand, Type FROM GTA.Vehicles WHERE OwnerID = ?";

        try (
            Connection conn = DriverManager.getConnection(DB_URL);   // DB connection
            PreparedStatement stmt = conn.prepareStatement(query)    // Prepare statement
        ) {
            stmt.setInt(1, id);                                     // Fill in ID placeholder
            ResultSet rs = stmt.executeQuery();                     // Execute

            System.out.println("--- Vehicles Owned by ID " + id + " ---");
            while (rs.next()) {                                      // Iterate results
                int vid = rs.getInt("VehicleID");                 // VehicleID
                String brand = rs.getString("Brand");             // Brand
                String type = rs.getString("Type");               // Type
                // Print each vehicle line
                System.out.printf(
                    "ID: %d, Brand: %s, Type: %s ",
                    vid, brand, type);
            }
        } catch (SQLException e) {                                 // SQL exception
            System.err.println("Error: " + e.getMessage());      // Error message
        }
    }

    // ----------------------------
    // Method: runAvgPayout
    // Purpose: Calculate the average reward amount for all missions
    public static void runAvgPayout() {
        // Simple aggregate to compute average
        String query = "SELECT AVG(Reward) AS AvgPayout FROM GTA.Missions";

        try (
            Connection conn = DriverManager.getConnection(DB_URL);   // Open connection
            PreparedStatement stmt = conn.prepareStatement(query);   // Prepare
            ResultSet rs = stmt.executeQuery()                      // Execute and fetch
        ) {
            if (rs.next()) {                                       // Check if there's a result
                double avg = rs.getDouble("AvgPayout");          // Read AvgPayout column
                System.out.printf("Average Mission Payout: $%.2f ", avg); // Print formatted
            }
        } catch (SQLException e) {                                 // Handle SQL errors
            System.err.println("Error: " + e.getMessage());      // Print error message
        }
    }

    // ----------------------------
    // Method: runInactiveAgentReport
    // Purpose: Find agents with zero assigned missions
    public static void runInactiveAgentReport() {
        // Use NOT EXISTS subquery to find agents without assignments
        String query = 
            "SELECT AgentID FROM GTA.Agents a " +
            "WHERE NOT EXISTS (SELECT 1 FROM GTA.Assignments asn WHERE asn.AgentID = a.AgentID)";

        try (
            Connection conn = DriverManager.getConnection(DB_URL);   // Connect to DB
            PreparedStatement stmt = conn.prepareStatement(query);   // Prepare statement
            ResultSet rs = stmt.executeQuery()                      // Execute
        ) {
            System.out.println("--- Inactive Agents ---");
            while (rs.next()) {                                     // Iterate rows
                int agentId = rs.getInt("AgentID");              // Read agent ID
                System.out.println("Agent ID: " + agentId);      // Print ID
            }
        } catch (SQLException e) {                                 // Catch SQL exceptions
            System.err.println("Error: " + e.getMessage());      // Display error
        }
    }

    // ----------------------------
    // Method: runHighestEarningCriminals
    // Purpose: Show top criminals by total mission rewards
    public static void runHighestEarningCriminals() {
        // Similar to runRewardTracker but filter for criminals only
        String query =
            "SELECT C.Name, SUM(M.Reward) AS Earnings " +
            "FROM GTA.Citizens C " +
            "JOIN GTA.Assignments A ON C.CitizenID = A.CitizenID " +
            "JOIN GTA.Missions M ON M.MissionID = A.MissionID " +
            "WHERE C.IsCriminal = 1 " +
            "GROUP BY C.Name " +
            "ORDER BY Earnings DESC LIMIT 5;";                   // Top 5 criminals

        try (
            Connection conn = DriverManager.getConnection(DB_URL);   // Connect
            PreparedStatement stmt = conn.prepareStatement(query);   // Prepare
            ResultSet rs = stmt.executeQuery()                      // Execute
        ) {
            System.out.println("--- Top 5 Earning Criminals ---");
            while (rs.next()) {                                     // Loop results
                String name = rs.getString("Name");              // Criminal name
                double earnings = rs.getDouble("Earnings");      // Total earnings
                // Print criminal earnings
                System.out.printf("%s: $%.2f", name, earnings);
            }
        } catch (SQLException e) {                                 // SQL errors
            System.err.println("Error: " + e.getMessage());      // Print error
        }
    }
}
