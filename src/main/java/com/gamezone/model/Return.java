package com.gamezone.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** A (possibly partial) return of products from a previous sale. */
public class Return {
    private final String returnId;
    private final LocalDate date;
    private final Sale sale;
    private final List<Product> returnedProducts;
    private final String reason;
    private double refundAmount;

    /**
     * Creates a return and calculates its refund.
     *
     * @param returnId         return id
     * @param date             return date
     * @param sale             original sale
     * @param returnedProducts returned products (one entry per unit)
     * @param reason           return reason
     */
    public Return(String returnId, LocalDate date, Sale sale,
                  List<Product> returnedProducts, String reason) {
        this.returnId = returnId;
        this.date = date;
        this.sale = sale;
        this.returnedProducts = new ArrayList<>(returnedProducts);
        this.reason = reason;
        calculateRefundAmount();
    }

    public String getReturnId() { return returnId; }
    public LocalDate getDate() { return date; }
    public Sale getSale() { return sale; }
    public List<Product> getReturnedProducts() { return new ArrayList<>(returnedProducts); }
    public String getReason() { return reason; }
    public double getRefundAmount() { return refundAmount; }

    /**
     * Sums the prices of the returned products and stores the result.
     *
     * @return the refund amount
     */
    public double calculateRefundAmount() {
        double total = 0;
        for (Product p : returnedProducts) total += p.getPrice();
        refundAmount = total;
        return refundAmount;
    }

    /** @return a Spanish receipt with the return details */
    public String generateReturnReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== RECIBO DE DEVOLUCIÓN =====\n");
        sb.append("Devolución: ").append(returnId).append("\n");
        sb.append("Fecha: ").append(date).append("\n");
        sb.append("Venta original: ").append(sale.getSaleId()).append("\n");
        sb.append("Productos devueltos:\n");
        for (Product p : returnedProducts) {
            sb.append(String.format("  - %s ($%.2f)%n", p.getTitle(), p.getPrice()));
        }
        sb.append("Motivo: ").append(reason).append("\n");
        sb.append(String.format("Monto reembolsado: $%.2f", refundAmount));
        return sb.toString();
    }
}
