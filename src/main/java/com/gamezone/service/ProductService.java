package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.*;
import exceptions.BusinessRuleException;
import java.util.List;
import validation.ProductValidator;

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
        if (findById(product.getId()) != null) {
            throw new BusinessRuleException("Ya existe un producto con el identificador " + product.getId());
        }
        products.add(product);
        repository.saveAll(products);
    }

    public boolean registerVideoGame(String id, String title, double price, int stock,
            String platform, String genre, String ageRating) {
        ProductValidator.validateProductData(id, title, price, stock);
        if (findById(id) != null) {
            throw new BusinessRuleException("Ya existe un producto con el identificador " + id);
        }
        products.add(new VideoGame(id, title, price, stock, platform, genre, ageRating));
        repository.saveAll(products);
        return true;
    }

    public boolean registerConsole(String id, String title, double price, int stock,
            String brand, String model, String generation) {
        ProductValidator.validateProductData(id, title, price, stock);
        if (findById(id) != null) {
            throw new BusinessRuleException("Ya existe un producto con el identificador " + id);
        }
        products.add(new Console(id, title, price, stock, brand, model, generation));
        repository.saveAll(products);
        return true;
    }

    public Product findById(String id) {
        for (Product p : products) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public Product getById(String id) {
        Product p = findById(id);
        ProductValidator.validateProductExists(p, id);
        return p;
    }

    public boolean updateStock(String productId, int delta) {
        Product p = getById(productId);
        if (delta < 0) {
            ProductValidator.validateStockAvailability(p, -delta);
        }
        p.setStock(p.getStock() + delta);
        repository.saveAll(products);
        return true;
    }

    /**
     * Increases the stock of a product.
     *
     * @param productId the product id
     * @param quantity units to add; must be positive
     * @return false if the quantity is not positive (lanza excepción si el
     * producto no existe vía getById)
     */
    public boolean restoreStock(String productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        return updateStock(productId, quantity);
    }
}
