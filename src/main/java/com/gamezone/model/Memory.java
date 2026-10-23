package com.gamezone.model;

import java.util.List;

/**
 * Represents a memory storage accessory.
 */
public class Memory extends Accessory {
    private int capacityGb;
    private String memoryType;

    public Memory(String id, String title, double price, int stock, List<String> compatibleConsoleIds, int capacityGb, String memoryType) {
        super(id, title, price, stock, compatibleConsoleIds);
        this.capacityGb = capacityGb;
        this.memoryType = memoryType;
    }

    public int getCapacityGb() {
        return capacityGb;
    }

    public void setCapacityGb(int capacityGb) {
        this.capacityGb = capacityGb;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }

    @Override
    public String getDescription() {
        return String.format("Memory [%s] - %s | Capacity: %dGB | Type: %s | Price: $%.2f | Stock: %d", 
                getId(), getTitle(), capacityGb, memoryType, getPrice(), getStock());
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