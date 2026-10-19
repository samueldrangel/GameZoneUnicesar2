package com.gamezone.persistence;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Repository for managing accessory persistence using flat files (CSV format).
 */
public class AccessoryRepository {
    private static final String FILE_PATH = "data/accessories.csv";

    public List<Accessory> loadAll() {
        List<Accessory> accessories = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return accessories;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 6) continue;

                String type = parts[0];
                String id = parts[1];
                String title = parts[2];
                double price = Double.parseDouble(parts[3]);
                int stock = Integer.parseInt(parts[4]);
                List<String> compatibleConsoles = parts[5].isEmpty() ? new ArrayList<>() : Arrays.asList(parts[5].split(";"));

                if ("CONTROLLER".equals(type) && parts.length >= 7) {
                    accessories.add(new Controller(id, title, price, stock, compatibleConsoles, parts[6]));
                } else if ("CABLE".equals(type) && parts.length >= 8) {
                    accessories.add(new Cable(id, title, price, stock, compatibleConsoles, Double.parseDouble(parts[6]), parts[7]));
                } else if ("MEMORY".equals(type) && parts.length >= 8) {
                    accessories.add(new Memory(id, title, price, stock, compatibleConsoles, Integer.parseInt(parts[6]), parts[7]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading accessories: " + e.getMessage());
        }
        return accessories;
    }

    public void saveAll(List<Accessory> accessories) {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Accessory acc : accessories) {
                StringBuilder sb = new StringBuilder();
                String consoles = String.join(";", acc.getCompatibleConsoleIds());

                if (acc instanceof Controller) {
                    Controller c = (Controller) acc;
                    sb.append("CONTROLLER,").append(c.getId()).append(",").append(c.getTitle()).append(",")
                      .append(c.getPrice()).append(",").append(c.getStock()).append(",").append(consoles).append(",").append(c.getConnectionType());
                } else if (acc instanceof Cable) {
                    Cable cb = (Cable) acc;
                    sb.append("CABLE,").append(cb.getId()).append(",").append(cb.getTitle()).append(",")
                      .append(cb.getPrice()).append(",").append(cb.getStock()).append(",").append(consoles).append(",")
                      .append(cb.getLengthMeters()).append(",").append(cb.getConnectorType());
                } else if (acc instanceof Memory) {
                    Memory m = (Memory) acc;
                    sb.append("MEMORY,").append(m.getId()).append(",").append(m.getTitle()).append(",")
                      .append(m.getPrice()).append(",").append(m.getStock()).append(",").append(consoles).append(",")
                      .append(m.getCapacityGb()).append(",").append(m.getMemoryType());
                }
                writer.println(sb.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving accessories: " + e.getMessage());
        }
    }
}