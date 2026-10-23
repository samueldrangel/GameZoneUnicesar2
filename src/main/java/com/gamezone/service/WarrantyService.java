package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.SaleRepository;
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
    public WarrantyService(WarrantyRepository repository, SaleRepository saleRepository,
                       ProductService productService, AccessoryService accessoryService,
                       PersonService personService) {
    this.repository = repository;
    List<Product> all = new ArrayList<>(productService.listAllProducts());
    all.addAll(accessoryService.listAllAccessories());
    Map<String, Sale> salesById = new HashMap<>();
    for (Sale s : saleRepository.loadAll(personService.listAllPersons(), all)) {
        salesById.put(s.getSaleId(), s);
    }
    Map<String, Product> productsById = new HashMap<>();
    for (Product p : all) productsById.put(p.getId(), p);
    for (String[] row : repository.loadRows()) {
        Sale sale = salesById.get(row[2]);
        Product product = productsById.get(row[3]);
        if (sale == null || product == null) continue;
        LocalDate start = LocalDate.parse(row[4]);
        warranties.add("EXTENDED".equals(row[0])
                ? new ExtendedWarranty(row[1], product, sale, start)
                : new BasicWarranty(row[1], product, sale, start));
    }
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
    
    
    /**
     * Removes the warranties of a product in a sale.
     *
     * @return the refundable cost: 0 for basic warranties, the additional cost
     * for extended ones
     */
    public double cancelWarranties(String productId, String saleId) {
        double refundable = 0;
        Iterator<Warranty> it = warranties.iterator();
        while (it.hasNext()) {
            Warranty w = it.next();
            if (w.getProduct().getId().equals(productId) && w.getSale().getSaleId().equals(saleId)) {
                if (w instanceof ExtendedWarranty) {
                    refundable += w.getAdditionalCost();
                }
                it.remove();
            }
        }
        repository.saveAll(warranties);
        return refundable;
    }
    
    /** Creates a factory for one warranty type. */
    @FunctionalInterface
    public interface WarrantyFactory {

        Warranty create(String id, Product product, Sale sale, LocalDate start);
    }

    private static final Map<String, WarrantyFactory> FACTORIES
            = Map.of("BASIC", BasicWarranty::new, "EXTENDED", ExtendedWarranty::new);

    public WarrantyService(WarrantyRepository repository, SaleCatalog catalog, List<Product> products) {
        this.repository = repository;
        Map<String, Product> productsById = new HashMap<>();
        for (Product p : products) {
            productsById.put(p.getId(), p);
        }
        for (String[] row : repository.loadRows()) {
            Sale sale = catalog.findById(row[2]);
            Product product = productsById.get(row[3]);
            WarrantyFactory factory = FACTORIES.get(row[0]);
            if (sale == null || product == null || factory == null) {
                continue;
            }
            warranties.add(factory.create(row[1], product, sale, LocalDate.parse(row[4])));
        }
    }

    /**
     * Issues the basic warranty of each console and the requested extended
     * ones. @return the extended cost
     */
    public double issueWarrantiesFor(Sale sale, List<String> extendedIds) {
        double extendedCost = 0;
        for (Product item : sale.getItems()) {
            if (item.supportsWarranty()) {
                assignBasicWarranty(item, sale, sale.getDate());
                if (extendedIds != null && extendedIds.contains(item.getId())) {
                    extendedCost += assignExtendedWarranty(item, sale, sale.getDate()).getAdditionalCost();
                }
            }
        }
        return extendedCost;
    }
// WarrantyRepository.saveAll escribe w.getTypeCode() en vez de decidir con instanceof (V-08).
}