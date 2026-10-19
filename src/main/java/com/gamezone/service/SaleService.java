package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Product;
import com.gamezone.model.Customer;
import com.gamezone.model.Seller;

import java.util.List;

/**
 * Service managing sales operations, validation rules, and stock updates for products and accessories.
 */
public class SaleService {
    private ProductService productService;
    private AccessoryService accessoryService;

    public SaleService(ProductService productService, AccessoryService accessoryService) {
        this.productService = productService;
        this.accessoryService = accessoryService;
    }

    public boolean registerSale(String id, Customer customer, Seller seller, List<Product> purchasedItems) {
        if (purchasedItems == null || purchasedItems.isEmpty()) {
            return false;
        }

        // Validate stock for all items (products and accessories)
        for (Product item : purchasedItems) {
            if (item.getStock() <= 0) {
                return false;
            }
        }

        // Deduct inventory dynamically based on the item type
        for (Product item : purchasedItems) {
            if (item instanceof Accessory) {
                accessoryService.updateStock(item.getId(), 1);
            } else {
                productService.updateStock(item.getId(), 1);
            }
        }

        return true;
    }
}