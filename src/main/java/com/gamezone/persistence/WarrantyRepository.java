package com.gamezone.persistence;

import com.gamezone.model.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

/** Persistence for warranties. Line: BASIC|EXTENDED,id,saleId,productId,startDate */
public class WarrantyRepository {
    private static final String FILE_PATH = "data/warranties.csv";

    /** @param warranties the warranties to save */
    public void saveAll(List<Warranty> warranties) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
                for (Warranty w : warranties) {
                    String type = (w instanceof ExtendedWarranty) ? "EXTENDED" : "BASIC";
                    writer.println(type + "," + w.getId() + "," + w.getSale().getSaleId() + ","
                            + w.getProduct().getId() + "," + w.getStartDate());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving warranties: " + e.getMessage());
        }
    }

    /**
     * @param sales    all sales, used to resolve references
     * @param products all products, used to resolve references
     * @return the stored warranties, or an empty list if the file does not exist
     */
    public List<Warranty> loadAll(List<Sale> sales, List<Product> products) {
        List<Warranty> result = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return result;
        Map<String, Sale> salesById = new HashMap<>();
        for (Sale s : sales) salesById.put(s.getSaleId(), s);
        Map<String, Product> productsById = new HashMap<>();
        for (Product p : products) productsById.put(p.getId(), p);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split(",", -1);
                if (p.length < 5) continue;
                Sale sale = salesById.get(p[2]);
                Product product = productsById.get(p[3]);
                if (sale == null || product == null) continue;
                try {
                    LocalDate start = LocalDate.parse(p[4]);
                    result.add("EXTENDED".equals(p[0])
                            ? new ExtendedWarranty(p[1], product, sale, start)
                            : new BasicWarranty(p[1], product, sale, start));
                } catch (RuntimeException e) {
                    System.err.println("Skipping invalid warranty line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading warranties: " + e.getMessage());
        }
        return result;
    }
}