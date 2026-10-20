package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.WarrantyRepository;
import java.time.LocalDate;
import java.util.*;

/** Business rules for warranties: assignment, validity and expiring-soon queries. */
public class WarrantyService {
    private final WarrantyRepository repository;
    private final List<Warranty> warranties = new ArrayList<>();

    /** @param repository the warranty repository */
    public WarrantyService(WarrantyRepository repository) {
        this.repository = repository;
    }

    /** Loads stored warranties once sales and products are available. */
    public void loadStored(List<Sale> sales, List<Product> products) {
        warranties.clear();
        warranties.addAll(repository.loadAll(sales, products));
    }

    /** Creates and stores a basic warranty. @return the new warranty */
    public BasicWarranty assignBasicWarranty(Product product, Sale sale, LocalDate startDate) {
        BasicWarranty w = new BasicWarranty(newId(), product, sale, startDate);
        warranties.add(w);
        repository.saveAll(warranties);
        return w;
    }

    /** Creates and stores an extended warranty. @return the new warranty */
    public ExtendedWarranty assignExtendedWarranty(Product product, Sale sale, LocalDate startDate) {
        ExtendedWarranty w = new ExtendedWarranty(newId(), product, sale, startDate);
        warranties.add(w);
        repository.saveAll(warranties);
        return w;
    }

    /**
     * Finds the warranty of a product in a sale, preferring the extended one.
     *
     * @return the warranty, or null if none exists
     */
    public Warranty findWarrantyByProduct(String productId, String saleId) {
        Warranty found = null;
        for (Warranty w : warranties) {
            if (w.getProduct().getId().equals(productId) && w.getSale().getSaleId().equals(saleId)) {
                if (w instanceof ExtendedWarranty) return w;
                if (found == null) found = w;
            }
        }
        return found;
    }

    /** @return all warranties */
    public List<Warranty> listAllWarranties() {
        return new ArrayList<>(warranties);
    }

    /** @return the warranties valid today */
    public List<Warranty> listActiveWarranties() {
        List<Warranty> result = new ArrayList<>();
        for (Warranty w : warranties) if (w.isActive(LocalDate.now())) result.add(w);
        return result;
    }

    /**
     * @param daysAhead number of days to look ahead
     * @return active warranties ending within the next daysAhead days
     * @throws IllegalArgumentException if daysAhead is negative
     */
    public List<Warranty> listWarrantiesExpiringSoon(int daysAhead) {
        if (daysAhead < 0) {
            throw new IllegalArgumentException("Los días de anticipación no pueden ser negativos.");
        }
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(daysAhead);
        List<Warranty> result = new ArrayList<>();
        for (Warranty w : warranties) {
            if (w.isActive(today) && !w.getEndDate().isAfter(limit)) result.add(w);
        }
        return result;
    }

    private String newId() {
        return "W-" + UUID.randomUUID().toString().substring(0, 8);
    }
}