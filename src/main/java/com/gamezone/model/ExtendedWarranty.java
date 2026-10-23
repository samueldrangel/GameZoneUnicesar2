package com.gamezone.model;

import java.time.LocalDate;

/** Twelve-month warranty for defects and accidental damage, costing 10% of the product price. */
public class ExtendedWarranty extends Warranty {
    /** Creates an extended warranty. */
    public ExtendedWarranty(String id, Product product, Sale sale, LocalDate startDate) {
        super(id, product, sale, startDate);
    }

    @Override
    public int getDurationInMonths() { return 12; }

    @Override
    public String getWarrantyType() { return "Garantía Extendida"; }

    @Override
    public double getAdditionalCost() { return getProduct().getPrice() * 0.10; }

    @Override
    public String getTypeCode() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
