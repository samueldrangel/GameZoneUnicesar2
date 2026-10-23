package com.gamezone.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A sale transaction with one or more items (products or accessories). It may
 * carry the discount granted by a promotion and extended warranty costs.
 */
public class Sale {

    private String saleId;
    private LocalDate date;
    private Customer customer;
    private Seller seller;
    private List<Product> items;
    private String appliedPromotionName;
    private double discountAmount;
    private double extendedWarrantyCost;
    public static final int RETURN_WINDOW_DAYS = 30;

    /**
     * Creates a sale without discount.
     *
     * @param saleId unique sale identifier
     * @param date sale date
     * @param customer the buyer
     * @param seller the seller who attended the sale
     * @param items the sold items; must contain at least one
     * @throws IllegalArgumentException if the item list is null or empty
     */
    public Sale(String saleId, LocalDate date, Customer customer, Seller seller, List<Product> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("A sale must contain at least one item");
        }
        this.saleId = saleId;
        this.date = date;
        this.customer = customer;
        this.seller = seller;
        this.items = new ArrayList<>(items);
        this.appliedPromotionName = null;
        this.discountAmount = 0;
        this.extendedWarrantyCost = 0.0;
    }

    public double getExtendedWarrantyCost() {
        return extendedWarrantyCost;
    }

    public void setExtendedWarrantyCost(double cost) {
        this.extendedWarrantyCost = cost;
    }

    public String getSaleId() {
        return saleId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Seller getSeller() {
        return seller;
    }

    public List<Product> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * @return the name of the applied promotion, or null if none was applied
     */
    public String getAppliedPromotionName() {
        return appliedPromotionName;
    }

    /**
     * @param appliedPromotionName the name of the applied promotion
     */
    public void setAppliedPromotionName(String appliedPromotionName) {
        this.appliedPromotionName = appliedPromotionName;
    }

    /**
     * @return the discount in pesos (0 if no promotion was applied)
     */
    public double getDiscountAmount() {
        return discountAmount;
    }

    /**
     * @param discountAmount the discount in pesos
     */
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    /**
     * Calculates the subtotal as the sum of the item prices.
     *
     * @return the subtotal before any discount
     */
    public double getSubtotal() {
        double total = 0;
        for (Product item : items) {
            total += item.getPrice();
        }
        return total;
    }

    /**
     * Calculates the final total.
     *
     * @return the subtotal minus the applied discount plus extended warranty
     * cost
     */
    public double getTotalAmount() {
        return getSubtotal() - discountAmount + extendedWarrantyCost;
    }

    /**
     * Builds the sale receipt with the subtotal, the discount, extended
     * warranty cost, and final total.
     *
     * @return the receipt text, in Spanish
     */
    public String generateReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== RECIBO DE VENTA =====\n");
        sb.append("Venta: ").append(saleId).append("\n");
        sb.append("Fecha: ").append(date).append("\n");
        sb.append("Cliente: ").append(customer.getName()).append("\n");
        sb.append("Vendedor: ").append(seller.getName()).append("\n");
        sb.append("Productos:\n");
        for (Product item : items) {
            sb.append(String.format("  - %s ($%.2f)%n", item.getTitle(), item.getPrice()));
        }
        sb.append(String.format("Subtotal: $%.2f%n", getSubtotal()));
        if (appliedPromotionName != null && discountAmount > 0) {
            sb.append(String.format("Descuento (%s): -$%.2f%n", appliedPromotionName, discountAmount));
        } else {
            sb.append("Descuento: $0.00 (sin promoción)\n");
        }
        sb.append(String.format("Garantías extendidas: $%.2f%n", extendedWarrantyCost));
        sb.append(String.format("Total final: $%.2f", getTotalAmount()));
        return sb.toString();
    }

    /**
     * @return true if today is within 30 calendar days after the sale date
     */
    public boolean canBeReturned() {
        return !LocalDate.now().isAfter(date.plusDays(RETURN_WINDOW_DAYS));
    }

    public int countOf(String productId) {
        int n = 0;
        for (Product p : items) {
            if (p.getId().equals(productId)) {
                n++;
            }
        }
        return n;
    }

    /**
     * @return the first sold item with that id, or null
     */
    public Product findItem(String productId) {
        for (Product p : items) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    /**
     * @return the price of the item after its proportional share of the
     * discount
     */
    public double getNetPrice(Product p) {
        double subtotal = getSubtotal();
        double ratio = subtotal > 0 ? discountAmount / subtotal : 0;
        return p.getPrice() * (1 - ratio);
    }
}
