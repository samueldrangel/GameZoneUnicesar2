package com.gamezone.model;

import java.util.List;

/**
 * Abstract base class representing all accessories in the store, extending Product.
 */
public abstract class Accessory extends Product {
    private List<String> compatibleConsoleIds;

    public Accessory(String id, String title, double price, int stock, List<String> compatibleConsoleIds) {
        super(id, title, price, stock);
        this.compatibleConsoleIds = compatibleConsoleIds;
    }

    public List<String> getCompatibleConsoleIds() {
        return compatibleConsoleIds;
    }

    public void setCompatibleConsoleIds(List<String> compatibleConsoleIds) {
        this.compatibleConsoleIds = compatibleConsoleIds;
    }

    @Override
    public abstract String getDescription();
    
    public abstract String getTypeCode();
}