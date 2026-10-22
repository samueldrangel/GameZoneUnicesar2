package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

/** Persistence for returns. Line: id,date,saleId,productId1;productId2,reason,refund */
public class ReturnRepository {
    private static final String FILE_PATH = "data/returns.csv";

    /** @param returns the returns to save */
    public void saveAll(List<Return> returns) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
                for (Return r : returns) {
                    List<String> ids = new ArrayList<>();
                    for (Product p : r.getReturnedProducts()) ids.add(p.getId());
                    writer.println(r.getReturnId() + "," + r.getDate() + "," + r.getSale().getSaleId()
                            + "," + String.join(";", ids) + "," + r.getReason()
                            + "," + r.getRefundAmount()+ "," + r.getWarrantyRefund());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving returns: " + e.getMessage());
        }
    }

    /**
     * @param sales all sales; products are resolved from their items
     * @return the stored returns, or an empty list if the file does not exist
     */
    public List<Return> loadAll(List<Sale> sales) {
    List<Return> result = new ArrayList<>();
    File file = new File(FILE_PATH);
    if (!file.exists()) return result;
    Map<String, Sale> salesById = new HashMap<>();
    for (Sale s : sales) salesById.put(s.getSaleId(), s);
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isBlank()) continue;
            String[] p = line.split(",", -1);
            if (p.length < 6) continue;
            Sale sale = salesById.get(p[2]);
            if (sale == null) continue;
            Map<String, Product> items = new HashMap<>();
            for (Product item : sale.getItems()) items.putIfAbsent(item.getId(), item);
            List<Product> returned = new ArrayList<>();
            for (String id : p[3].split(";")) {
                Product product = items.get(id);
                if (product != null) returned.add(product);
            }
            if (returned.isEmpty()) continue;
            try {
                // Leer el warrantyRefund de forma segura desde la posición 6 si existe
                double wr = (p.length >= 7 && !p[6].isEmpty()) ? Double.parseDouble(p[6]) : 0;
                
                // Agregar la devolución pasando el 'wr' al constructor
                result.add(new Return(p[0], LocalDate.parse(p[1]), sale, returned, p[4], wr));
            } catch (RuntimeException e) {
                System.err.println("Skipping invalid return line: " + line);
            }
        }
    } catch (IOException e) {
        System.err.println("Error loading returns: " + e.getMessage());
    }
    return result;
}
}