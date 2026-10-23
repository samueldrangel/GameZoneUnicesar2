package com.gamezone.service;

import com.gamezone.model.Product;

/** Stock operations for one family of products. All implementations share this contract. */
public interface InventoryService {
    /** @return true if this inventory manages the given item */
    boolean handles(Product item);
    /** @return the stored item with that id, or null */
    Product findById(String id);
    /** Adds a delta to the stock (negative to decrease). @return true when applied */
    boolean updateStock(String id, int delta);
    /** Increases the stock. @return false if the quantity is not positive */
    boolean restoreStock(String id, int quantity);
}
