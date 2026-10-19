package com.gamezone.persistence;

import com.gamezone.model.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Persistence for persons (customers and sellers).
 * 
 * @author Samuel David Rangel Martinez
 */
public class PersonRepository {
    private static final String FILE_PATH = "data/persons.csv";

    public void saveAll(List<Person> persons) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
                for (Person p : persons) {
                    if (p instanceof Customer) {
                        Customer c = (Customer) p;
                        writer.println("CUSTOMER," + c.getId() + "," + c.getName() + "," + c.getPhone() + "," + c.getEmail());
                    } else if (p instanceof Seller) {
                        Seller s = (Seller) p;
                        writer.println("SELLER," + s.getId() + "," + s.getName() + "," + s.getPhone() + "," + s.getEmployeeCode() + "," + s.getWorkShift());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving persons: " + e.getMessage());
        }
    }

    public List<Person> loadAll() {
        List<Person> persons = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return persons;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;
                if ("CUSTOMER".equals(parts[0])) {
                    persons.add(new Customer(parts[1], parts[2], parts[3], parts[4]));
                } else if ("SELLER".equals(parts[0]) && parts.length >= 6) {
                    persons.add(new Seller(parts[1], parts[2], parts[3], parts[4], parts[5]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading persons: " + e.getMessage());
        }
        return persons;
    }
}
