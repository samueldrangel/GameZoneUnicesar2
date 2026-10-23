package com.gamezone.model;

import java.util.List;

/**
 * Represents a controller accessory.
 */
public class Controller extends Accessory {
    private String connectionType;

    public Controller(String id, String title, double price, int stock, List<String> compatibleConsoleIds, String connectionType) {
        super(id, title, price, stock, compatibleConsoleIds);
        this.connectionType = connectionType;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public String getDescription() {
        return String.format("Controller [%s] - %s | Connection: %s | Price: $%.2f | Stock: %d", 
                getId(), getTitle(), connectionType, getPrice(), getStock());
    }

    @Override
    public ProductCategory getCategory() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}