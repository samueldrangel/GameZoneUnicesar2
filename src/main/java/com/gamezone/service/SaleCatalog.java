package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.SaleRepository;
import java.util.*;

/** Single in-memory source of sales, shared by the services that need them. */
public class SaleCatalog {
    private final SaleRepository repository;
    private final List<Sale> sales;

    /** Loads the stored sales, resolving references from the given lists. */
    public SaleCatalog(SaleRepository repository, List<Person> persons, List<Product> products) {
        this.repository = repository;
        this.sales = new ArrayList<>(repository.loadAll(persons, products));
    }

    /** @return a copy of all sales */
    public List<Sale> listAll() { return new ArrayList<>(sales); }

    /** @return the sale with that id, or null */
    public Sale findById(String saleId) {
        for (Sale s : sales) if (s.getSaleId().equals(saleId)) return s;
        return null;
    }

    /** Adds a sale and saves all sales. */
    public void add(Sale sale) {
        sales.add(sale);
        repository.saveAll(sales);
    }
}