package com.gamezone.ui;

import com.gamezone.model.*;
import java.util.List;

public class ConsolePrinter {

    public void printProducts(List<Product> products) {
        System.out.println("\n--- PRODUCTOS DISPONIBLES ---");
        for (Product p : products) {
            System.out.println(" - ID: " + p.getId() + " | " + p.getTitle() + " ($" + p.getPrice() + ")");
        }
    }

    public void printAccessories(String title, List<Accessory> accessories) {
        System.out.println("\n--- " + title + " ---");
        if (accessories.isEmpty()) {
            System.out.println("No hay accesorios disponibles.");
        } else {
            for (Accessory a : accessories) {
                System.out.println(" - ID: " + a.getId() + " | " + a.getTitle() + " ($" + a.getPrice() + ")");
            }
        }
    }

    public void printSellers(List<Person> persons) {
        System.out.println("\n--- VENDEDORES DISPONIBLES ---");
        for (Person p : persons) {
            if (p instanceof Seller) {
                System.out.println(" - ID: " + p.getId() + " | Nombre: " + p.getName());
            }
        }
    }

    public void printSales(List<Sale> sales) {
        System.out.println("\n--- TRANSACCIONES DE VENTAS ---");
        for (Sale s : sales) {
            System.out.println(s.generateReceipt());
            System.out.println("----------------------------------------");
        }
    }
}