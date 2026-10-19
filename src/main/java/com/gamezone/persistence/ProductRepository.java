package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Persistence for products.
 * 
 * @author Samuel David Rangel Martinez
 */
public class ProductRepository {
    private static final String FILE_PATH = "data/products.csv";

    public void saveAll(List<Product> products) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
                for (Product p : products) {
                    if (p instanceof VideoGame) {
                        VideoGame vg = (VideoGame) p;
                        writer.println("VIDEOGAME," + vg.getId() + "," + vg.getTitle() + "," + vg.getPrice() + "," + vg.getStock() + "," + vg.getPlatform() + "," + vg.getGenre() + "," + vg.getAgeRating());
                    } else if (p instanceof com.gamezone.model.Console) {
                        com.gamezone.model.Console c = (com.gamezone.model.Console) p;
                        writer.println("CONSOLE," + c.getId() + "," + c.getTitle() + "," + c.getPrice() + "," + c.getStock() + "," + c.getBrand() + "," + c.getModel() + "," + c.getGeneration());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    public List<Product> loadAll() {
        List<Product> products = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return products;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 8) continue;
                if ("VIDEOGAME".equals(parts[0])) {
                    products.add(new VideoGame(parts[1], parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[4]), parts[5], parts[6], parts[7]));
                } else if ("CONSOLE".equals(parts[0])) {
                    products.add(new com.gamezone.model.Console(parts[1], parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[4]), parts[5], parts[6], parts[7]));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }
        return products;
    }
}