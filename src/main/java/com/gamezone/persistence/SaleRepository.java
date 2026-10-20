package com.gamezone.persistence;

import com.gamezone.model.Customer;
import com.gamezone.model.Person;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Persistence for sales transactions.
 * Each line: id,date,customerId,sellerId,itemId1;itemId2,total,promotionName,discount,extendedWarrantyCost
 */
public class SaleRepository {
    private static final String FILE_PATH = "data/sales.csv";

    /**
     * Creates the repository.
     */
    public SaleRepository() {
    }

    /**
     * Saves all sales to the CSV file.
     *
     * @param sales the sales to save
     */
    public void saveAll(List<Sale> sales) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
                for (Sale s : sales) {
                    List<String> ids = new ArrayList<>();
                    for (Product item : s.getItems()) {
                        ids.add(item.getId());
                    }
                    writer.println(s.getSaleId() + "," + s.getDate() + ","
                            + s.getCustomer().getId() + ","
                            + s.getSeller().getId() + ","
                            + String.join(";", ids) + ","
                            + s.getTotalAmount() + ","
                            + (s.getAppliedPromotionName() == null ? "" : s.getAppliedPromotionName()) + ","
                            + s.getDiscountAmount() + "," 
                            + s.getExtendedWarrantyCost());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving sales: " + e.getMessage());
        }
    }

    /**
     * Loads all sales, rebuilding the references from the given lists.
     *
     * @param persons  all customers and sellers
     * @param products all products, including accessories
     * @return the loaded sales, or an empty list if the file does not exist
     */
    public List<Sale> loadAll(List<Person> persons, List<Product> products) {
        List<Sale> sales = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return sales;

        Map<String, Person> personsById = new HashMap<>();
        for (Person p : persons) personsById.put(p.getId(), p);
        Map<String, Product> productsById = new HashMap<>();
        for (Product p : products) productsById.put(p.getId(), p);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 6) continue;

                try {
                    Person customer = personsById.get(parts[2]);
                    Person seller = personsById.get(parts[3]);
                    if (!(customer instanceof Customer) || !(seller instanceof Seller)) continue;

                    List<Product> items = new ArrayList<>();
                    boolean complete = true;
                    for (String id : parts[4].split(";")) {
                        Product item = productsById.get(id);
                        if (item == null) { complete = false; break; }
                        items.add(item);
                    }
                    if (!complete || items.isEmpty()) {
                        System.err.println("Skipping sale with missing data: " + parts[0]);
                        continue;
                    }

                    Sale sale = new Sale(parts[0], LocalDate.parse(parts[1]),
                            (Customer) customer, (Seller) seller, items);
                    if (parts.length >= 8 && !parts[6].isEmpty()) {
                        sale.setAppliedPromotionName(parts[6]);
                        sale.setDiscountAmount(Double.parseDouble(parts[7]));
                    }
                    if (parts.length >= 9 && !parts[8].isEmpty()) {
                        sale.setExtendedWarrantyCost(Double.parseDouble(parts[8]));
                    }
                    sales.add(sale);
                } catch (RuntimeException e) {
                    System.err.println("Skipping invalid sale line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading sales: " + e.getMessage());
        }
        return sales;
    }
}