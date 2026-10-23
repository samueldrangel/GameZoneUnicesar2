package com.gamezone.service;

import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.persistence.ReturnRepository;
import java.time.LocalDate;
import java.util.*;

/**
 * Business rules for returns and the monthly balance.
 */
public class ReturnService {

    private final ReturnRepository repository;
    private final SaleCatalog catalog;
    private final InventoryCoordinator inventory;
    private final WarrantyService warrantyService;
    private final List<Return> returns;

    /**
     * @param repository the return repository
     * @param catalog sale catalog
     * @param inventory inventory coordinator
     * @param warrantyService warranty service
     */
    public ReturnService(ReturnRepository repository, SaleCatalog catalog, 
                         InventoryCoordinator inventory, WarrantyService warrantyService) {
        this.repository = repository;
        this.catalog = catalog;
        this.inventory = inventory;
        this.warrantyService = warrantyService;
        this.returns = new ArrayList<>(repository.loadAll(catalog.listAll()));
    }

    /**
     * Registers a return after validating existence, deadline and ownership.
     *
     * @param saleId original sale id
     * @param productIds ids of the returned products
     * @param reason return reason
     * @return the registered return
     */
    public Return registerReturn(String saleId, List<String> productIds, String reason) {
        Sale sale = catalog.findById(saleId);
        if (sale == null) {
            throw new IllegalArgumentException("La venta " + saleId + " no existe.");
        }
        if (!sale.canBeReturned()) {
            throw new IllegalArgumentException(
                    "No se puede registrar la devolución: han pasado más de " + Sale.RETURN_WINDOW_DAYS + " días desde la venta.");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Debe indicar al menos un producto a devolver.");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el motivo de la devolución.");
        }

        Map<String, Integer> alreadyReturned = countReturned(saleId);
        Map<String, Integer> requested = new LinkedHashMap<>();
        for (String id : productIds) {
            requested.merge(id, 1, Integer::sum);
        }

        List<Product> toReturn = new ArrayList<>();
        double warrantyRefund = 0;

        for (Map.Entry<String, Integer> e : requested.entrySet()) {
            String id = e.getKey();
            Product sold = sale.findItem(id);
            if (sold == null) {
                throw new IllegalArgumentException(
                        "El producto " + id + " no pertenece a la venta " + saleId + ".");
            }
            int available = sale.countOf(id) - alreadyReturned.getOrDefault(id, 0);
            if (e.getValue() > available) {
                throw new IllegalArgumentException(
                        "El producto " + id + " ya fue devuelto o excede la cantidad comprada.");
            }
            
            for (int i = 0; i < e.getValue(); i++) {
                toReturn.add(sold);
            }

            // Stock and warranty restoration using inventory coordinator and product methods
            inventory.restoreStock(sold, e.getValue());
            if (sold.supportsWarranty()) {
                warrantyRefund += warrantyService.cancelWarranties(id, saleId);
            }
        }

        Return r = new Return("RET-" + (returns.size() + 1), LocalDate.now(), sale,
                toReturn, reason.trim().replace(',', ' '), warrantyRefund);

        returns.add(r);
        repository.saveAll(returns);
        return r;
    }

    /**
     * @return all returns
     */
    public List<Return> viewAllReturns() {
        return new ArrayList<>(returns);
    }

    /**
     * @return the returns of the sales of a customer
     */
    public List<Return> viewReturnsByCustomer(String customerId) {
        List<Return> result = new ArrayList<>();
        for (Return r : returns) {
            if (r.getSale().getCustomer().getId().equals(customerId)) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * @return the returns of a sale
     */
    public List<Return> viewReturnsBySale(String saleId) {
        List<Return> result = new ArrayList<>();
        for (Return r : returns) {
            if (r.getSale().getSaleId().equals(saleId)) {
                result.add(r);
            }
        }
        return result;
    }

    public double calculateMonthlySales(int month, int year) {
        checkMonth(month);
        double total = 0;
        for (Sale s : catalog.listAll()) {
            if (s.getDate().getMonthValue() == month && s.getDate().getYear() == year) {
                total += s.getTotalAmount();
            }
        }
        return total;
    }

    public double calculateMonthlyReturns(int month, int year) {
        checkMonth(month);
        double total = 0;
        for (Return r : returns) {
            if (r.getDate().getMonthValue() == month && r.getDate().getYear() == year) {
                total += r.getRefundAmount();
            }
        }
        return total;
    }

    /**
     * Net balance of a month: sales total minus returns total.
     *
     * @param month month from 1 to 12
     * @param year year
     * @return the net balance
     */
    public double generateMonthlyBalance(int month, int year) {
        return calculateMonthlySales(month, year) - calculateMonthlyReturns(month, year);
    }

    private void checkMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }
    }

    private Map<String, Integer> countReturned(String saleId) {
        Map<String, Integer> count = new HashMap<>();
        for (Return r : returns) {
            if (r.getSale().getSaleId().equals(saleId)) {
                for (Product p : r.getReturnedProducts()) {
                    count.merge(p.getId(), 1, Integer::sum);
                }
            }
        }
        return count;
    }
}
