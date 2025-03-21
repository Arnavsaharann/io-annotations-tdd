package com.bridgelabz.cabinvoicegenerator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

public class CabInvoiceGeneratorTest {

    @Test
    public void givenDistanceAndTimeForNormalRide_ShouldReturnTotalFare() {
        CabInvoiceGenerator invoiceGenerator = new CabInvoiceGenerator();
        double distance = 2.0;
        int time = 5;
        double fare = invoiceGenerator.calculateFare(distance, time, RideCategory.NORMAL);
        assertEquals(25, fare, 0.0);
    }

    @Test
    public void givenDistanceAndTimeForPremiumRide_ShouldReturnTotalFare() {
        CabInvoiceGenerator invoiceGenerator = new CabInvoiceGenerator();
        double distance = 2.0;
        int time = 5;
        double fare = invoiceGenerator.calculateFare(distance, time, RideCategory.PREMIUM);
        assertEquals(40, fare, 0.0);
    }

    @Test
    public void givenMultipleNormalAndPremiumRides_ShouldReturnAggregateTotalFare() {
        CabInvoiceGenerator invoiceGenerator = new CabInvoiceGenerator();
        Ride normalRide = new Ride(2.0, 5, RideCategory.NORMAL);
        Ride premiumRide = new Ride(0.1, 1, RideCategory.PREMIUM);

        double totalFare = invoiceGenerator.calculateTotalFare(List.of(normalRide, premiumRide));
        assertEquals(45, totalFare, 0.0);
    }

    @Test
    public void givenMultipleNormalAndPremiumRides_ShouldReturnCorrectEnhancedInvoice() {
        CabInvoiceGenerator invoiceGenerator = new CabInvoiceGenerator();
        Ride normalRide = new Ride(2.0, 5, RideCategory.NORMAL);
        Ride premiumRide = new Ride(0.1, 1, RideCategory.PREMIUM);

        Invoice invoice = invoiceGenerator.getEnhancedInvoice(List.of(normalRide, premiumRide));
        assertEquals(2, invoice.getTotalRides());
        assertEquals(45, invoice.getTotalFare(), 0.0);
        assertEquals(22.5, invoice.getAverageFarePerRide(), 0.0);
    }
}
