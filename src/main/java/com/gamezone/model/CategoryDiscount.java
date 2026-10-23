package com.gamezone.model;

import java.time.LocalDate;

/**
 * Promotion that discounts a percentage only over the items of one category
 * ("VIDEOGAME" or "CONSOLE").
 */
public class CategoryDiscount extends Promotion {

    private double percentage;
    private String targetCategory;

    /**
     * @param id promotion id
     * @param name promotion name
     * @param startDate first valid day
     * @param endDate last valid day
     * @param percentage discount percentage, between 0 and 100
     * @param targetCategory VIDEOGAME or CONSOLE
     */
    public CategoryDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
            double percentage, String targetCategory) {
        super(id, name, startDate, endDate);
        this.percentage = percentage;
        this.targetCategory = targetCategory;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getTargetCategory() {
        return targetCategory;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setTargetCategory(String targetCategory) {
        this.targetCategory = targetCategory;
    }

    @Override
    public double calculateDiscount(Sale sale) {
        double categoryTotal = 0;
        for (Product item : sale.getItems()) {
            if (belongsToTargetCategory(item)) {
                categoryTotal += item.getPrice();
            }
        }
        return categoryTotal * percentage / 100.0;
    }

    private boolean belongsToTargetCategory(Product item) {
        return item.getCategory().name().equals(targetCategory);
    }

    @Override
    public String getTypeCode() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
