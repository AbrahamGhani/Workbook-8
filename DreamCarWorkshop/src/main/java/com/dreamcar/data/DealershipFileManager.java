package com.dreamcar.data;

import com.dreamcar.model.Dealership;
import com.dreamcar.model.Vehicle;

import java.io.*;

public class DealershipFileManager {

    public Dealership getDealership() {
        Dealership dealership = null;

        try (BufferedReader reader = new BufferedReader(new FileReader("dealership.csv"))) {
            String header = reader.readLine();
            String[] dealerInfo = header.split("\\|");
            dealership = new Dealership(dealerInfo[0], dealerInfo[1], dealerInfo[2]);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                Vehicle vehicle = new Vehicle(
                        Integer.parseInt(data[0]),
                        Integer.parseInt(data[1]),
                        data[2], data[3], data[4],
                        data[5], Integer.parseInt(data[6]),
                        Double.parseDouble(data[7])
                );
                dealership.addVehicle(vehicle);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dealership;
    }

    public void saveDealership(Dealership dealership) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("dealership.csv"))) {

            // Write the dealership header
            writer.printf("%s|%s|%s\n",
                    dealership.getName(),
                    dealership.getAddress(),
                    dealership.getPhone()
            );

            // Write each vehicle
            for (Vehicle v : dealership.getAllVehicles()) {
                writer.printf("%d|%d|%s|%s|%s|%s|%d|%.2f\n",
                        v.getVin(),
                        v.getYear(),
                        v.getMake(),
                        v.getModel(),
                        v.getType(),
                        v.getColor(),
                        v.getOdometer(),
                        v.getPrice()
                );
            }
            System.out.println("Saving to: " + new File("dealership.csv").getAbsolutePath());
            System.out.println("Dealership saved to file.");

        } catch (IOException e) {
            System.out.println("Error saving dealership to file: " + e.getMessage());
        }
    }





}
