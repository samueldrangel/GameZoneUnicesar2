package com.gamezone.model;

import java.time.LocalDate;

/** Six-month warranty against factory defects, at no extra cost. */
public class BasicWarranty extends Warranty {
    /** Creates a basic warranty. */
    public BasicWarranty(String id, Product product, Sale sale, LocalDate startDate) {
        super(id, product, sale, startDate);
    }

    @Override
    public int getDurationInMonths() { return 6; }

    @Override
    public String getWarrantyType() { return "Garantía Básica"; }

    @Override
    public double getAdditionalCost() { return 0.0; }
}
