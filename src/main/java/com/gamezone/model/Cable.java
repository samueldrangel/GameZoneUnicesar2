package com.gamezone.model;

import java.util.List;

/**
 * Represents a cable accessory.
 */
public class Cable extends Accessory {
    private double lengthMeters;
    private String connectorType;

    public Cable(String id, String title, double price, int stock, List<String> compatibleConsoleIds, double lengthMeters, String connectorType) {
        super(id, title, price, stock, compatibleConsoleIds);
        this.lengthMeters = lengthMeters;
        this.connectorType = connectorType;
    }

    public double getLengthMeters() {
        return lengthMeters;
    }

    public void setLengthMeters(double lengthMeters) {
        this.lengthMeters = lengthMeters;
    }

    public String getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }

    @Override
    public String getDescription() {
        return String.format("Cable [%s] - %s | Length: %.1fm | Connector: %s | Price: $%.2f | Stock: %d", 
                getId(), getTitle(), lengthMeters, connectorType, getPrice(), getStock());
    }

    @Override
    public ProductCategory getCategory() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getTypeCode() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}