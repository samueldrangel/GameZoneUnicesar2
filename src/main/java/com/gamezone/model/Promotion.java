package com.gamezone.model;

import java.time.LocalDate;

/**
 * Base class for promotions that grant a discount over a sale during a period.
 */
public abstract class Promotion {
    private String id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * @param id        unique promotion identifier
     * @param name      promotion name
     * @param startDate first day the promotion is valid
     * @param endDate   last day the promotion is valid
     */
    public Promotion(String id, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    /**
     * Checks whether the promotion is valid on the given date (both limits included).
     *
     * @param date the date to check
     * @return true if the date is inside the validity range
     */
    public boolean isActive(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Calculates the discount this promotion would grant to a sale.
     *
     * @param sale the sale to evaluate
     * @return the discount amount in pesos (0 if the promotion does not apply)
     */
    public abstract double calculateDiscount(Sale sale);
    
    public abstract String getTypeCode();
}