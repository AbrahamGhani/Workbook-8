package com.dreamcar.ui;

import java.util.Scanner;

public class PromptUser {
static Scanner scanner = new Scanner(System.in);



    public static double[] promptPriceRange() {
        System.out.print("Enter Minimum Price: ");
        double priceMin = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter Maximum Price: ");
        double priceMax = scanner.nextDouble();
        scanner.nextLine();
        double[] priceRange = {priceMin, priceMax};
        return priceRange;
    }


    public static String[] promptMakeModel() {
        System.out.print("Enter Make: ");
        String make = scanner.nextLine();

        System.out.print("Enter Model: ");
        String model = scanner.nextLine();
        String[] MakeModel = {make, model};
        return MakeModel;
    }



}
