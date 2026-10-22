package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Console;
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
    private final SaleService saleService;
    private final ProductService productService;
    private final AccessoryService accessoryService; // <-- Añadido
    private final List<Return> returns;
    private WarrantyService warrantyService; // Añadido aquí

    /**
     * @param repository the return repository
     * @param saleService service used to find sales
     * @param productService service used to restore product stock
     * @param accessoryService service used to restore accessory stock
     */

    // Constructor completo actualizado
    public ReturnService(ReturnRepository repository, 
                         SaleService saleService, 
                         ProductService productService, 
                         AccessoryService accessoryService, 
                         WarrantyService warrantyService, 
                         List<Return> returns) {
        this.repository = repository;
        this.saleService = saleService;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.warrantyService = warrantyService;
        this.returns = returns;
    }

    /**
     * Registers a return after validating existence, deadline and ownership.
     *
     * @param saleId original sale id
     * @param productIds ids of the returned products (repeat an id for several
     * units)
     * @param reason return reason
     * @return the registered return
     * @throws IllegalArgumentException with a Spanish message if a validation
     * fails
     */
    public Return registerReturn(String saleId, List<String> productIds, String reason) {
        Sale sale = saleService.findSaleById(saleId);
        if (sale == null) {
            throw new IllegalArgumentException("La venta " + saleId + " no existe.");
        }
        if (!sale.canBeReturned()) {
            throw new IllegalArgumentException(
                    "No se puede registrar la devolución: han pasado más de 30 días desde la venta.");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Debe indicar al menos un producto a devolver.");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el motivo de la devolución.");
        }

        Map<String, Product> soldById = new HashMap<>();
        Map<String, Integer> soldCount = new HashMap<>();
        for (Product p : sale.getItems()) {
            soldById.putIfAbsent(p.getId(), p);
            soldCount.merge(p.getId(), 1, Integer::sum);
        }
        Map<String, Integer> alreadyReturned = countReturned(saleId);
        Map<String, Integer> requested = new LinkedHashMap<>();
        for (String id : productIds) {
            requested.merge(id, 1, Integer::sum);
        }

        List<Product> toReturn = new ArrayList<>();
        for (Map.Entry<String, Integer> e : requested.entrySet()) {
            String id = e.getKey();
            if (!soldById.containsKey(id)) {
                throw new IllegalArgumentException(
                        "El producto " + id + " no pertenece a la venta " + saleId + ".");
            }
            int available = soldCount.get(id) - alreadyReturned.getOrDefault(id, 0);
            if (e.getValue() > available) {
                throw new IllegalArgumentException(
                        "El producto " + id + " ya fue devuelto o excede la cantidad comprada.");
            }
            for (int i = 0; i < e.getValue(); i++) {
                toReturn.add(soldById.get(id));
            }
        }

        double warrantyRefund = 0;
        for (String id : requested.keySet()) {
            if (soldById.get(id) instanceof Console) {
                warrantyRefund += warrantyService.cancelWarranties(id, saleId);
            }
        }

// Asegúrate de pasar 'warrantyRefund' al constructor del objeto Return:
        Return r = new Return("RET-" + (returns.size() + 1), LocalDate.now(), sale,
                toReturn, reason.trim().replace(',', ' '), warrantyRefund);

        // Restauración de stock diferenciada entre accesorios y productos generales
        for (Map.Entry<String, Integer> e : requested.entrySet()) {
            String productId = e.getKey();
            int quantity = e.getValue();

            Product product = soldById.get(productId);
            if (product instanceof Accessory) {
                accessoryService.restoreStock(productId, quantity);
            } else {
                productService.restoreStock(productId, quantity);
            }
        }

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
        for (Sale s : saleService.listAllSales()) {
            if (s.getDate().getMonthValue() == month && s.getDate().getYear() == year) {
                total += s.getTotalAmount();   // final total, with discount and extended warranties
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
     * @throws IllegalArgumentException if the month is invalid
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
