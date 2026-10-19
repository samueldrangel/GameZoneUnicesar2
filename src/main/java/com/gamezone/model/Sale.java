package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a sale transaction in the store.
 * 
 * @author Samuel David Rangel Martinez
 */
public class Sale {
    private String saleId;
    private LocalDate date;
    private Customer customer;
    private Seller seller;
    private Product product;
    private int quantity;
    private double totalAmount;

    public Sale(String saleId, LocalDate date, Customer customer, Seller seller, Product product, int quantity) {
        this.saleId = saleId;
        this.date = date;
        this.customer = customer;
        this.seller = seller;
        this.product = product;
        this.quantity = quantity;
        this.totalAmount = product.getPrice() * quantity;
    }

    public String getSaleSummary() {
        return "Sale [ID: " + saleId + ", Date: " + date + 
               ", Customer: " + customer.getName() + 
               ", Seller: " + seller.getName() + 
               ", Product: " + product.getTitle() + 
               ", Qty: " + quantity + ", Total: $" + totalAmount + "]";
    }

    public String getSaleId() { return saleId; }
    public LocalDate getDate() { return date; }
    public Customer getCustomer() { return customer; }
    public Seller getSeller() { return seller; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getTotalAmount() { return totalAmount; }
}
