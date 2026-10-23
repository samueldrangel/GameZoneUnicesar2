package com.gamezone.persistence;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Persistence for promotions. The first column is a type discriminator:
 * PERCENTAGE, CATEGORY or BULK.
 */
public class PromotionRepository {
    private static final String FILE_PATH = "data/promotions.csv";

    /**
     * Saves all promotions to the CSV file.
     *
     * @param promotions the promotions to save
     */
    public void saveAll(List<Promotion> promotions) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
                for (Promotion p : promotions) {
                    String common = p.getId() + "," + p.getName() + ","
                            + p.getStartDate() + "," + p.getEndDate();
                    if (p instanceof PercentageDiscount) {
                        PercentageDiscount d = (PercentageDiscount) p;
                        writer.println("PERCENTAGE," + common + "," + d.getPercentage());
                    } else if (p instanceof CategoryDiscount) {
                        CategoryDiscount d = (CategoryDiscount) p;
                        writer.println("CATEGORY," + common + "," + d.getPercentage()
                                + "," + d.getTargetCategory());
                    } else if (p instanceof BulkPurchaseDiscount) {
                        BulkPurchaseDiscount d = (BulkPurchaseDiscount) p;
                        writer.println("BULK," + common + "," + d.getMinimumQuantity()
                                + "," + d.getPercentage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving promotions: " + e.getMessage());
        }
    }

    /**
     * Loads all promotions from the CSV file.
     *
     * @return the loaded promotions, or an empty list if the file does not exist
     */
    public List<Promotion> loadAll() {
        List<Promotion> promotions = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return promotions;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split(",", -1);
                try {
                    LocalDate start = LocalDate.parse(p[3]);
                    LocalDate end = LocalDate.parse(p[4]);
                    if ("PERCENTAGE".equals(p[0]) && p.length >= 6) {
                        promotions.add(new PercentageDiscount(p[1], p[2], start, end,
                                Double.parseDouble(p[5])));
                    } else if ("CATEGORY".equals(p[0]) && p.length >= 7) {
                        promotions.add(new CategoryDiscount(p[1], p[2], start, end,
                                Double.parseDouble(p[5]), p[6]));
                    } else if ("BULK".equals(p[0]) && p.length >= 7) {
                        promotions.add(new BulkPurchaseDiscount(p[1], p[2], start, end,
                                Integer.parseInt(p[5]), Double.parseDouble(p[6])));
                    }
                } catch (RuntimeException e) {
                    System.err.println("Skipping invalid promotion line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading promotions: " + e.getMessage());
        }
        return promotions;
    }
}