package com.gamezone.service;

import com.gamezone.model.Product;
import com.gamezone.validation.ProductValidator;
import java.util.*;
import validation.ProductValidator;

/**
 * Routes stock operations to the inventory that manages each item.
 */
public class InventoryCoordinator {

    private final List<InventoryService> inventories;

    /**
     * @param inventories the available inventories
     */
    public InventoryCoordinator(List<InventoryService> inventories) {
        this.inventories = new ArrayList<>(inventories);
    }

    private InventoryService forItem(Product item) {
        for (InventoryService i : inventories) {
            if (i.handles(item)) {
                return i;
            }
        }
        throw new IllegalStateException("No inventory registered for " + item.getTitle());
    }

    /**
     * Resolves every item and validates existence and stock; returns the stored
     * items by id.
     */
    public Map<String, Product> resolveAndCheck(List<Product> items, Map<String, Integer> requested) {
        Map<String, Product> resolved = new HashMap<>();
        for (Product item : items) {
            Product current = forItem(item).findById(item.getId());
            ProductValidator.validateProductExists(current, item.getId());
            resolved.put(item.getId(), current);
        }
        for (Map.Entry<String, Integer> e : requested.entrySet()) {
            ProductValidator.validateStockAvailability(resolved.get(e.getKey()), e.getValue());
        }
        return resolved;
    }

    /**
     * Decreases the stock of every resolved item by the requested units.
     */
    public void discount(Map<String, Product> resolved, Map<String, Integer> requested) {
        for (Map.Entry<String, Integer> e : requested.entrySet()) {
            forItem(resolved.get(e.getKey())).updateStock(e.getKey(), -e.getValue());
        }
    }

    /**
     * Increases the stock of one item.
     */
    public boolean restoreStock(Product item, int quantity) {
        return forItem(item).restoreStock(item.getId(), quantity);
    }
}
