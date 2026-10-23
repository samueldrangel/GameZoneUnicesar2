package com.gamezone.model;

import java.time.LocalDate;

/** Base class for warranties attached to a product sold in a sale. */
public abstract class Warranty {
    private final String id;
    private final Product product;
    private final Sale sale;
    private final LocalDate startDate;
    private final LocalDate endDate;

    /**
     * Creates a warranty; the end date is derived from the duration of the subclass.
     *
     * @param id        warranty id
     * @param product   covered product
     * @param sale      sale in which the warranty was issued
     * @param startDate first day of coverage
     */
    public Warranty(String id, Product product, Sale sale, LocalDate startDate) {
        this.id = id;
        this.product = product;
        this.sale = sale;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(getDurationInMonths());
    }

    public String getId() { return id; }
    public Product getProduct() { return product; }
    public Sale getSale() { return sale; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    /** @return the duration in months */
    public abstract int getDurationInMonths();

    /** @return the warranty type name, in Spanish */
    public abstract String getWarrantyType();

    /** @return the extra cost this warranty adds to the sale */
    public abstract double getAdditionalCost();

    /**
     * @param date the date to check
     * @return true if the date is between the start and end dates (both included)
     */
    public boolean isActive(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /** @return a Spanish certificate with the warranty details */
    public String generateWarrantyCertificate() {
        return String.format("===== CERTIFICADO DE GARANTÍA =====%nGarantía: %s%nTipo: %s%n"
                + "Producto: %s%nVenta: %s%nVigencia: %s a %s%nCosto adicional: $%.2f",
                id, getWarrantyType(), product.getTitle(), sale.getSaleId(),
                startDate, endDate, getAdditionalCost());
    }
    
    public abstract String getTypeCode();
}
