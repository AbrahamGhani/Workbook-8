package com.dreamcar.model;

/**
 * SalesContract represents a contract where the vehicle is sold to a customer.
 * It handles different fees, taxes, and loan terms depending on the sale price
 * and whether the buyer chooses to finance the vehicle.
 */
public class SalesContract extends Contract {

    // Constants for fees and tax
    private static final double SALES_TAX_RATE = 0.05;          // 5% sales tax
    private static final double RECORDING_FEE = 100.0;          // Fixed recording fee
    private static final double LOW_PRICE_THRESHOLD = 10000.0;  // Threshold for processing fee rules
    private boolean isFinanced; // Whether the customer chose to finance the vehicle

    /**
     * Constructor to initialize SalesContract.
     * param date           the contract date
     * param customerName   the buyer's name
     * param customerEmail  the buyer's email
     * param vehicle        the vehicle being sold
     * param isFinanced     true if financing, false if paying in full
     */
    public SalesContract(String date, String customerName, String customerEmail, Vehicle vehicle, boolean isFinanced) {
        super(date, customerName, customerEmail, vehicle);
        this.isFinanced = isFinanced;
    }

    /**
     * Determines the processing fee based on the vehicle price.
     * - $295 if price is under $10,000
     * - $495 otherwise
     * return processing fee
     */
    public double getProcessingFee() {
        double price = getVehicleSold().getPrice();

        if (price < LOW_PRICE_THRESHOLD) {
            return 295.0;
        } else {
            return 495.0;
        }
    }

    /**
     * Calculates the total upfront cost of the vehicle:
     * vehicle price + sales tax + recording fee + processing fee
     * This is used as the total amount for cash buyers or as the loan principal for financing.
     */
    @Override
    public double getTotalPrice() {
        double price = getVehicleSold().getPrice();
        double salesTax = price * SALES_TAX_RATE;

        return price + salesTax + RECORDING_FEE + getProcessingFee();
    }

    /**
     * Calculates the monthly payment if the customer chose to finance.
     * Uses standard loan formula:
     * (P * r) / (1 - (1 + r)^-n)
     * - 4.25% for 48 months if total >= $10,000
     * - 5.25% for 24 months otherwise
     * Returns 0.0 if not financed.
     */
    @Override
    public double getMonthlyPayment() {
        if (!isFinanced) return 0.0;

        double principal = getTotalPrice();
        double rate;
        int months;

        if (principal >= LOW_PRICE_THRESHOLD) {
            rate = 0.0425 / 12; // 4.25% annual -> monthly
            months = 48;
        } else {
            rate = 0.0525 / 12; // 5.25% annual -> monthly
            months = 24;
        }

        return (principal * rate) / (1 - Math.pow(1 + rate, -months));
    }

    /**
     * Returns whether the vehicle is being financed.
     * return true if financed, false otherwise
     */
    public boolean isFinanced() {
        return isFinanced;
    }
}
