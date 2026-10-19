package com.gamezone.model;

import java.time.LocalDate;

/**
 * Promotion that discounts a percentage of the whole sale subtotal.
 */
public class PercentageDiscount extends Promotion {
    private double percentage;

    /**
     * @param id         promotion id
     * @param name       promotion name
     * @param startDate  first valid day
     * @param endDate    last valid day
     * @param percentage discount percentage, between 0 and 100
     */
    public PercentageDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                              double percentage) {
        super(id, name, startDate, endDate);
        this.percentage = percentage;
    }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }

    @Override
    public double calculateDiscount(Sale sale) {
        return sale.getSubtotal() * percentage / 100.0;
    }
}