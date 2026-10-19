package com.gamezone.model;

public class Console extends Product {
    private String brand;
    private String model;
    private String generation;

    public Console(String id, String title, double price, int stock, String brand, String model, String generation) {
        super(id, title, price, stock);
        this.brand = brand;
        this.model = model;
        this.generation = generation;
    }

    @Override
    public String getDescription() {
        return "Console [ID: " + getId() + ", Title: " + getTitle() + ", Price: $" + getPrice() + 
               ", Stock: " + getStock() + ", Brand: " + brand + ", Model: " + model + ", Generation: " + generation + "]";
    }

    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getGeneration() { return generation; }
}