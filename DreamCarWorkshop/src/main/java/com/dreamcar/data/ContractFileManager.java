package com.dreamcar.data;

import com.dreamcar.model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class ContractFileManager {
    private static final String CONTRACT_FILE = "contracts.csv"; // File path/name

    /**
     * Saves a contract (sale or lease) by appending it to the contracts file.
     * param contract the contract to save
     */

    public void saveContract(Contract contract) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONTRACT_FILE, true))) {
            StringBuilder sb = new StringBuilder();

            Vehicle v = contract.getVehicleSold();

            // Common fields
            if (contract instanceof SalesContract) {
                sb.append("SALE|");
            } else {
                sb.append("LEASE|");
            }
            sb.append(contract.getDate()).append("|");
            sb.append(contract.getCustomerName()).append("|");
            sb.append(contract.getCustomerEmail()).append("|");
            sb.append(v.getVin()).append("|");
            sb.append(v.getYear()).append("|");
            sb.append(v.getMake()).append("|");
            sb.append(v.getModel()).append("|");
            sb.append(v.getType()).append("|");
            sb.append(v.getColor()).append("|");
            sb.append(v.getOdometer()).append("|");
            sb.append(v.getPrice()).append("|");

            // Specific to SalesContract
            if (contract instanceof SalesContract) {
                SalesContract sc = (SalesContract) contract;
                sb.append(v.getPrice() * 0.05).append("|"); // sales tax
                sb.append(100.0).append("|"); // recording fee
                sb.append(sc.getProcessingFee()).append("|");
                sb.append(sc.getTotalPrice()).append("|");
                if (sc.isFinanced()) {
                    sb.append("YES|");
                } else {
                    sb.append("NO|");
                }
                sb.append(sc.getMonthlyPayment());

                // Specific to LeaseContract
            } else if (contract instanceof LeaseContract) {
                LeaseContract lc = (LeaseContract) contract;
                sb.append(lc.getExpectedEndingValue()).append("|");
                sb.append(lc.getLeaseFee()).append("|");
                sb.append(lc.getTotalPrice()).append("|");
                sb.append(lc.getMonthlyPayment());
            }

            // Write to file
            writer.write(sb.toString());
            writer.newLine(); // move to next line for next contract

        } catch (IOException e) {
            System.err.println("Error saving contract: " + e.getMessage());
        }
    }




}
