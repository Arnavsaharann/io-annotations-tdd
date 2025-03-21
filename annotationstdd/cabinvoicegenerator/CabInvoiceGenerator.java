package com.bridgelabz.cabinvoicegenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Enum for ride categories
enum RideCategory {
    NORMAL,
    PREMIUM
}

// CabInvoiceGenerator class to calculate fares and generate invoices
public class CabInvoiceGenerator {
    // Rates for Normal Rides
    private static final int NORMAL_RATE_PER_KM = 10;
    private static final int NORMAL_RATE_PER_MINUTE = 1;
    private static final int NORMAL_MINIMUM_FARE = 5;

    // Rates for Premium Rides
    private static final int PREMIUM_RATE_PER_KM = 15;
    private static final int PREMIUM_RATE_PER_MINUTE = 2;
    private static final int PREMIUM_MINIMUM_FARE = 20;

    // Method to calculate fare for a single ride based on its category
    public double calculateFare(double distance, int time, RideCategory category) {
        double fare;
        switch (category) {
            case NORMAL -> fare = (distance * NORMAL_RATE_PER_KM) + (time * NORMAL_RATE_PER_MINUTE);
            case PREMIUM -> fare = (distance * PREMIUM_RATE_PER_KM) + (time * PREMIUM_RATE_PER_MINUTE);
            default -> throw new IllegalArgumentException("Unknown ride category: " + category);
        }
        return Math.max(fare, category == RideCategory.NORMAL ? NORMAL_MINIMUM_FARE : PREMIUM_MINIMUM_FARE);
    }

    // Method to calculate total fare for multiple rides
    public double calculateTotalFare(List<Ride> rides) {
        return rides.stream()
                .mapToDouble(ride -> calculateFare(ride.getDistance(), ride.getTime(), ride.getCategory()))
                .sum();
    }

    // Method to generate enhanced invoice
    public Invoice getEnhancedInvoice(List<Ride> rides) {
        double totalFare = calculateTotalFare(rides);
        int totalRides = rides.size();
        double averageFarePerRide = totalFare / totalRides;
        return new Invoice(totalRides, totalFare, averageFarePerRide);
    }
}

// Ride class for ride details
class Ride {
    private final double distance;
    private final int time;
    private final RideCategory category;

    public Ride(double distance, int time, RideCategory category) {
        this.distance = distance;
        this.time = time;
        this.category = category;
    }

    public double getDistance() {
        return distance;
    }

    public int getTime() {
        return time;
    }

    public RideCategory getCategory() {
        return category;
    }
}

// Invoice class for invoice details
class Invoice {
    private final int totalRides;
    private final double totalFare;
    private final double averageFarePerRide;

    public Invoice(int totalRides, double totalFare, double averageFarePerRide) {
        this.totalRides = totalRides;
        this.totalFare = totalFare;
        this.averageFarePerRide = averageFarePerRide;
    }

    public void print() {
        System.out.println("Enhanced Invoice:");
        System.out.printf("Total Rides ", totalRides);
        System.out.printf("Total Fare ", totalFare);
        System.out.printf("Average Fare Per Ride ", averageFarePerRide);
    }

    public int getTotalRides() {
        return totalRides;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public double getAverageFarePerRide() {
        return averageFarePerRide;
    }
}

// RideDatabase class to store rides for users
class RideDatabase {
    private final Map<String, List<Ride>> rideMappings = new HashMap<>();

    // Adds rides for a given user ID
    public void addRides(String userId, List<Ride> rides) {
        rideMappings.computeIfAbsent(userId, k -> new ArrayList<>()).addAll(rides);
    }

    // Retrieves rides for a given user ID
    public List<Ride> getRides(String userId) {
        return rideMappings.getOrDefault(userId, List.of());
    }
}

// InvoiceService to generate invoices for specific users
class InvoiceService {
    private final RideDatabase rideRepository;
    private final CabInvoiceGenerator invoiceGenerator;

    public InvoiceService(RideDatabase rideRepository, CabInvoiceGenerator invoiceGenerator) {
        this.rideRepository = rideRepository;
        this.invoiceGenerator = invoiceGenerator;
    }

    // Generates an invoice for a specific user ID
    public Invoice generateInvoiceForUser(String userId) {
        List<Ride> rides = rideRepository.getRides(userId);
        if (rides.isEmpty()) {
            System.out.println("No rides found for user ID: " + userId);
            return new Invoice(0, 0, 0);
        }
        return invoiceGenerator.getEnhancedInvoice(rides);
    }
}

// Main class to demonstrate functionality
class Main {
    public static void main(String[] args) {
        CabInvoiceGenerator invoiceGenerator = new CabInvoiceGenerator();
        RideDatabase rideDatabase = new RideDatabase();
        InvoiceService invoiceService = new InvoiceService(rideDatabase, invoiceGenerator);

        // Add Normal and Premium rides for User1
        rideDatabase.addRides("User1", List.of(
                new Ride(2.0, 5, RideCategory.NORMAL),
                new Ride(0.1, 1, RideCategory.PREMIUM)
        ));

        // Add Normal and Premium rides for User2
        rideDatabase.addRides("User2", List.of(
                new Ride(5.0, 10, RideCategory.NORMAL),
                new Ride(3.0, 8, RideCategory.PREMIUM)
        ));

        // Generate invoice for User1
        System.out.println("Invoice for User1:");
        Invoice user1Invoice = invoiceService.generateInvoiceForUser("User1");
        user1Invoice.print();

        // Generate invoice for User2
        System.out.println("\nInvoice for User2:");
        Invoice user2Invoice = invoiceService.generateInvoiceForUser("User2");
        user2Invoice.print();
    }
}
