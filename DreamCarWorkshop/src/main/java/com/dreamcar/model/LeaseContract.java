package com.dreamcar.model;

/**
 * LeaseContract represents a lease agreement for a vehicle.
 * It calculates lease-specific values like expected ending value,
 * lease fee, total price, and monthly payments based on fixed rules.
 */
public class LeaseContract extends Contract {

    // Constants for lease calculations
    private static final double LEASE_FEE_RATE = 0.07;          // 7% of original price
    private static final double ENDING_VALUE_RATE = 0.50;       // 50% of original price
    private static final double INTEREST_RATE = 0.04 / 12;      // 4% annual interest divided monthly
    private static final int LEASE_TERM_MONTHS = 36;            // 3-year lease

    /**
     * Constructor to initialize LeaseContract with inherited contract info.
     * param date           the contract date
     * param customerName   the buyer's name
     * param customerEmail  the buyer's email
     * param vehicle        the vehicle being leased
     */
    public LeaseContract(String date, String customerName, String customerEmail, Vehicle vehicle) {
        super(date, customerName, customerEmail, vehicle);
    }

    /**
     * Calculates 50% of the original vehicle price, which is the value at lease end.
     * @return expected ending value
     */
    public double getExpectedEndingValue() {
        return getVehicleSold().getPrice() * ENDING_VALUE_RATE;
    }

    /**
     * Calculates the lease fee (7% of original vehicle price).
     * return lease fee
     */
    public double getLeaseFee() {
        return getVehicleSold().getPrice() * LEASE_FEE_RATE;
    }

    /**
     * Returns the total price to be financed for the lease.
     * This includes the ending value and lease fee.
     */
    @Override
    public double getTotalPrice() {
        return getExpectedEndingValue() + getLeaseFee();
    }

    /**
     * Calculates the monthly payment using a loan formula:
     * (P * r) / (1 - (1 + r)^-n)
     * Where:
     *   P = total lease amount,
     *   r = monthly interest rate,
     *   n = number of months
     */
    @Override
    public double getMonthlyPayment() {
        double amountFinanced = getTotalPrice();

        return (amountFinanced * INTEREST_RATE) /
                (1 - Math.pow(1 + INTEREST_RATE, -LEASE_TERM_MONTHS));
    }
}
