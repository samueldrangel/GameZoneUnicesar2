package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;
import com.gamezone.persistence.AccessoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer class containing business logic and validation rules for accessories.
 */
public class AccessoryService {
    private AccessoryRepository repository;
    private List<Accessory> accessories;

    public AccessoryService(AccessoryRepository repository) {
        this.repository = repository;
        this.accessories = repository.loadAll();
    }

    public void registerController(String id, String title, double price, int stock, List<String> compatibleConsoles, String connectionType) {
        accessories.add(new Controller(id, title, price, stock, compatibleConsoles, connectionType));
        repository.saveAll(accessories);
    }

    public void registerCable(String id, String title, double price, int stock, List<String> compatibleConsoles, double length, String connector) {
        accessories.add(new Cable(id, title, price, stock, compatibleConsoles, length, connector));
        repository.saveAll(accessories);
    }

    public void registerMemory(String id, String title, double price, int stock, List<String> compatibleConsoles, int capacity, String memoryType) {
        accessories.add(new Memory(id, title, price, stock, compatibleConsoles, capacity, memoryType));
        repository.saveAll(accessories);
    }

    public List<Accessory> listAllAccessories() {
        return new ArrayList<>(accessories);
    }

    public List<Accessory> listAccessoriesByType(String type) {
        return accessories.stream()
                .filter(a -> {
                    if ("CONTROLLER".equalsIgnoreCase(type)) return a instanceof Controller;
                    if ("CABLE".equalsIgnoreCase(type)) return a instanceof Cable;
                    if ("MEMORY".equalsIgnoreCase(type)) return a instanceof Memory;
                    return false;
                })
                .collect(Collectors.toList());
    }

    public List<Accessory> findAccessoriesCompatibleWith(String consoleId) {
        return accessories.stream()
                .filter(a -> a.getCompatibleConsoleIds() != null && a.getCompatibleConsoleIds().contains(consoleId))
                .collect(Collectors.toList());
    }

    public Accessory findById(String id) {
        return accessories.stream().filter(a -> a.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public boolean updateStock(String accessoryId, int quantity) {
        Accessory accessory = findById(accessoryId);
        if (accessory != null) {
            accessory.setStock(accessory.getStock() - quantity);
            repository.saveAll(accessories);
            return true;
        }
        return false;
    }
    
    /**
     * Increases the stock of an accessory.
     * 
     * @param accessoryId the accessory identifier
     * @param quantity the quantity to restore; must be positive
     * @return false if not found or quantity not positive
     */
    public boolean restoreStock(String accessoryId, int quantity) {
        if (quantity <= 0) return false;
        
        Accessory accessory = findById(accessoryId);
        if (accessory != null) {
            accessory.setStock(accessory.getStock() + quantity); // Sumamos para restaurar stock
            repository.saveAll(accessories);
            return true;
        }
        return false;
    }
}