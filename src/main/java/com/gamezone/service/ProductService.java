package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.*;
import java.util.List;

public class ProductService {

    private ProductRepository repository;
    private List<Product> products;

    public ProductService() {
        this.repository = new ProductRepository();
        this.products = repository.loadAll();
    }

    public List<Product> listAllProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
        repository.saveAll(products);
    }

    public Product findById(String id) {
        for (Product p : products) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public boolean updateStock(String productId, int quantitySold) {
        Product p = findById(productId);
        if (p != null) {
            p.setStock(p.getStock() - quantitySold);
            repository.saveAll(products);
            return true; // Retorna true si se actualizó con éxito
        }
        return false; // Retorna false si el producto no fue encontrado
    }

    /**
     * Increases the stock of a product.
     *
     * @param productId the product id
     * @param quantity units to add; must be positive
     * @return false if the product does not exist or the quantity is not
     * positive
     */
    public boolean restoreStock(String productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        return updateStock(productId, quantity);
    }
}
