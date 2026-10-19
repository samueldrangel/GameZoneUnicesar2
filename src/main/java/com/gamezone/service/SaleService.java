package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Product;
import com.gamezone.model.Customer;
import com.gamezone.model.Seller;
import com.gamezone.model.Sale;
import com.gamezone.persistence.SaleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service managing sales operations, validation rules, and stock updates.
 */
public class SaleService {
    private ProductService productService;
    private AccessoryService accessoryService;
    private PersonService personService;
    private SaleRepository saleRepository;
    private List<Sale> sales;

    public SaleService(ProductService productService, AccessoryService accessoryService, PersonService personService, SaleRepository saleRepository) {
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.personService = personService;
        this.saleRepository = saleRepository;
        this.sales = saleRepository.loadAll(personService, productService);
        if (this.sales == null) {
            this.sales = new ArrayList<>();
        }
    }

    public boolean registerSale(String id, Customer customer, Seller seller, Product product, int quantity) {
        if (product == null || customer == null || seller == null || quantity <= 0) {
            return false;
        }

        // Validate stock availability
        if (product.getStock() < quantity) {
            return false;
        }

        // Deduct inventory dynamically based on the item type
        if (product instanceof Accessory) {
            accessoryService.updateStock(product.getId(), quantity);
        } else {
            productService.updateStock(product.getId(), quantity);
        }

        // Create Sale and persist
        Sale sale = new Sale(id, LocalDate.now(), customer, seller, product, quantity);
        sales.add(sale);
        saleRepository.saveAll(sales);

        return true;
    }

    public List<Sale> listAllSales() {
        return new ArrayList<>(sales);
    }
}