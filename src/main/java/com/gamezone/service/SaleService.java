package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.SaleRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for managing sales operations and business rules.
 * 
 * @author Samuel David Rangel Martinez
 */
public class SaleService {
    private SaleRepository repository;
    private List<Sale> sales;
    private ProductService productService;

    public SaleService(PersonService personService, ProductService productService) {
        this.productService = productService;
        this.repository = new SaleRepository();
        this.sales = repository.loadAll(personService, productService);
    }

    public List<Sale> listAllSales() {
        return sales;
    }

    public boolean registerSale(String saleId, Customer customer, Seller seller, Product product, int quantity) {
        if (product.getStock() < quantity) {
            System.out.println("Error: Insufficient stock for product " + product.getTitle());
            return false;
        }

        Sale newSale = new Sale(saleId, LocalDate.now(), customer, seller, product, quantity);
        sales.add(newSale);
        
        // Update product stock through ProductService
        productService.updateStock(product.getId(), quantity);
        
        // Save sales
        repository.saveAll(sales);
        return true;
    }
}