package com.gamezone.model;

import java.time.LocalDate;

/**
 * Promotion that discounts a percentage of the subtotal when the sale
 * has a minimum number of items.
 */
public class BulkPurchaseDiscount extends Promotion {
    private int minimumQuantity;
    private double percentage;

    /**
     * @param id              promotion id
     * @param name            promotion name
     * @param startDate       first valid day
     * @param endDate         last valid day
     * @param minimumQuantity minimum number of items in the sale
     * @param percentage      discount percentage, between 0 and 100
     */
    public BulkPurchaseDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                                int minimumQuantity, double percentage) {
        super(id, name, startDate, endDate);
        this.minimumQuantity = minimumQuantity;
        this.percentage = percentage;
    }

    public int getMinimumQuantity() { return minimumQuantity; }
    public double getPercentage() { return percentage; }
    public void setMinimumQuantity(int minimumQuantity) { this.minimumQuantity = minimumQuantity; }
    public void setPercentage(double percentage) { this.percentage = percentage; }

    @Override
    public double calculateDiscount(Sale sale) {
        if (sale.getItems().size() >= minimumQuantity) {
            return sale.getSubtotal() * percentage / 100.0;
        }
        return 0;
    }
}