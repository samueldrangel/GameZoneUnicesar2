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
    private final double warrantyRefund;
    private double refundAmount;

    /** Creates a return that also refunds the cancelled extended warranties. */
    public Return(String returnId, LocalDate date, Sale sale, List<Product> returnedProducts,
                  String reason, double warrantyRefund) {
        this.returnId = returnId;
        this.date = date;
        this.sale = sale;
        this.returnedProducts = new ArrayList<>(returnedProducts);
        this.reason = reason;
        this.warrantyRefund = warrantyRefund;
        calculateRefundAmount();
    }

    /** Creates a return without warranty refund. */
    public Return(String returnId, LocalDate date, Sale sale, List<Product> returnedProducts,
                  String reason) {
        this(returnId, date, sale, returnedProducts, reason, 0);
    }

    public String getReturnId() { return returnId; }
    public LocalDate getDate() { return date; }
    public Sale getSale() { return sale; }
    public List<Product> getReturnedProducts() { return new ArrayList<>(returnedProducts); }
    public String getReason() { return reason; }
    public double getWarrantyRefund() { return warrantyRefund; }
    public double getRefundAmount() { return refundAmount; }


    private double itemRefund(Product p) { return sale.getNetPrice(p); }

    /**
     * Sums the proportional refund of each item plus the warranty refund.
     *
     * @return the refund amount
     */
    public double calculateRefundAmount() {
        double total = 0;
        for (Product p : returnedProducts) total += itemRefund(p);
        refundAmount = total + warrantyRefund;
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
            sb.append(String.format(
                    "  - %s | Precio de lista: $%.2f | Descuento proporcional: -$%.2f | Reembolso: $%.2f%n",
                    p.getTitle(), p.getPrice(), p.getPrice() - itemRefund(p), itemRefund(p)));
        }
        if (warrantyRefund > 0) {
            sb.append(String.format("Reembolso por garantías extendidas: $%.2f%n", warrantyRefund));
        }
        sb.append("Motivo: ").append(reason).append("\n");
        sb.append(String.format("Monto reembolsado: $%.2f", refundAmount));
        return sb.toString();
    }
}
