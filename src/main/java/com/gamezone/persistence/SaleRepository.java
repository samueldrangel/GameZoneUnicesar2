package com.gamezone.persistence;

import com.gamezone.model.*;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Persistence for sales transactions.
 * 
 * @author Samuel David Rangel Martinez
 */
public class SaleRepository {
    private static final String FILE_PATH = "data/sales.csv";

    public void saveAll(List<Sale> sales) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
                for (Sale s : sales) {
                    writer.println(s.getSaleId() + "," + s.getDate() + "," + 
                                   s.getCustomer().getId() + "," + 
                                   s.getSeller().getId() + "," + 
                                   s.getProduct().getId() + "," + 
                                   s.getQuantity() + "," + s.getTotalAmount());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving sales: " + e.getMessage());
        }
    }

    public SaleRepository() {
    }
    
    

    public List<Sale> loadAll(PersonService personService, ProductService productService) {
        List<Sale> sales = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return sales;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 7) continue;
                String saleId = parts[0];
                LocalDate date = LocalDate.parse(parts[1]);
                Customer customer = personService.findCustomerById(parts[2]);
                Seller seller = personService.findSellerById(parts[3]);
                Product product = productService.findById(parts[4]);
                int quantity = Integer.parseInt(parts[5]);

                if (customer != null && seller != null && product != null) {
                    sales.add(new Sale(saleId, date, customer, seller, product, quantity));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading sales: " + e.getMessage());
        }
        return sales;
    }
}
