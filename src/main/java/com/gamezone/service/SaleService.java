package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import validation.SaleValidator;

/**
 * Service managing sales operations, validation rules, promotion applications,
 * stock updates, and warranties.
 */
public class SaleService {

    private final SaleCatalog catalog;
    private final InventoryCoordinator inventory;
    private final PromotionService promotionService;
    private final WarrantyService warrantyService;

    /**
     * Creates the service from its collaborators.
     */
    public SaleService(SaleCatalog catalog, InventoryCoordinator inventory,
            PromotionService promotionService, WarrantyService warrantyService) {
        this.catalog = catalog;
        this.inventory = inventory;
        this.promotionService = promotionService;
        this.warrantyService = warrantyService;
    }

    /**
     * Registers a sale without extended warranties.
     */
    public boolean registerSale(String saleId, Customer c, Seller s, List<Product> items) {
        return registerSale(saleId, c, s, items, new ArrayList<>());
    }

    /**
     * Registers a sale following the unified flow. @return false if the id
     * already exists
     */
    public boolean registerSale(String saleId, Customer customer, Seller seller,
            List<Product> items, List<String> extendedIds) {
        SaleValidator.validateSaleData(customer, seller, items);
        if (catalog.findById(saleId) != null) {
            return false;
        }
        Map<String, Integer> requested = countUnits(items);
        Map<String, Product> resolved = inventory.resolveAndCheck(items, requested);
        Sale sale = new Sale(saleId, LocalDate.now(), customer, seller, items);
        promotionService.applyBestPromotion(sale);
        sale.setExtendedWarrantyCost(warrantyService.issueWarrantiesFor(sale, extendedIds));
        inventory.discount(resolved, requested);
        catalog.add(sale);
        return true;
    }

    public List<Sale> listSales() {
        return catalog.listAll();
    }

    public Sale findSaleById(String saleId) {
        return catalog.findById(saleId);
    }
    // listSalesByCustomer / listSalesBySeller: same filters, over catalog.listAll()

    private Map<String, Integer> countUnits(List<Product> items) {
        Map<String, Integer> r = new LinkedHashMap<>();
        for (Product p : items) {
            r.merge(p.getId(), 1, Integer::sum);
        }
        return r;
    }
}
