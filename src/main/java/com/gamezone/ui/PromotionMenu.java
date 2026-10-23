package com.gamezone.ui;

import com.gamezone.model.Promotion;
import com.gamezone.service.PromotionService;
import java.time.LocalDate;
import java.util.List;

public class PromotionMenu {
    private final PromotionService promotionService;
    private final ConsoleInput input;

    public PromotionMenu(PromotionService promotionService, ConsoleInput input) {
        this.promotionService = promotionService;
        this.input = input;
    }

    public void listActivePromotions() {
        System.out.println("\n--- PROMOCIONES ACTIVAS ---");
        List<Promotion> promos = promotionService.listActivePromotions();
        if (promos.isEmpty()) {
            System.out.println("No hay promociones activas en este momento.");
        } else {
            for (Promotion p : promos) {
                System.out.println(" - ID: " + p.getId() + " | Nombre: " + p.getName() + " | Válida hasta: " + p.getEndDate());
            }
        }
    }

    public void registerPercentageDiscount() {
        try {
            String id = input.readLine("Ingrese ID de la promoción: ");
            String name = input.readLine("Ingrese Nombre de la promoción: ");
            LocalDate start = LocalDate.parse(input.readLine("Ingrese Fecha de inicio (YYYY-MM-DD): "));
            LocalDate end = LocalDate.parse(input.readLine("Ingrese Fecha de fin (YYYY-MM-DD): "));
            double percentage = input.readDouble("Ingrese Porcentaje de descuento (ej. 15.0): ");

            boolean success = promotionService.registerPercentageDiscount(id, name, start, end, percentage);
            if (success) {
                System.out.println("¡Descuento porcentual registrado exitosamente!");
            } else {
                System.out.println("Error: El ID de la promoción ya existe.");
            }
        } catch (Exception e) {
            System.out.println("Error en los datos ingresados: " + e.getMessage());
        }
    }

    public void registerCategoryDiscount() {
        try {
            String id = input.readLine("Ingrese ID de la promoción: ");
            String name = input.readLine("Ingrese Nombre de la promoción: ");
            LocalDate start = LocalDate.parse(input.readLine("Ingrese Fecha de inicio (YYYY-MM-DD): "));
            LocalDate end = LocalDate.parse(input.readLine("Ingrese Fecha de fin (YYYY-MM-DD): "));
            double percentage = input.readDouble("Ingrese Porcentaje de descuento: ");
            String category = input.readLine("Categoría (VIDEOGAME / CONSOLE / ACCESSORY): ").toUpperCase();

            boolean success = promotionService.registerCategoryDiscount(id, name, start, end, percentage, category);
            if (success) {
                System.out.println("¡Descuento por categoría registrado exitosamente!");
            } else {
                System.out.println("Error: El ID ya existe o la categoría es inválida.");
            }
        } catch (Exception e) {
            System.out.println("Error en los datos ingresados: " + e.getMessage());
        }
    }

    public void registerBulkPurchaseDiscount() {
        try {
            String id = input.readLine("Ingrese ID de la promoción: ");
            String name = input.readLine("Ingrese Nombre de la promoción: ");
            LocalDate start = LocalDate.parse(input.readLine("Ingrese Fecha de inicio (YYYY-MM-DD): "));
            LocalDate end = LocalDate.parse(input.readLine("Ingrese Fecha de fin (YYYY-MM-DD): "));
            int minQty = input.readInt("Ingrese Cantidad mínima de ítems: ");
            double percentage = input.readDouble("Ingrese Porcentaje de descuento: ");

            boolean success = promotionService.registerBulkPurchaseDiscount(id, name, start, end, minQty, percentage);
            if (success) {
                System.out.println("¡Descuento por compra al por mayor registrado exitosamente!");
            } else {
                System.out.println("Error: El ID ya existe o los valores son inválidos.");
            }
        } catch (Exception e) {
            System.out.println("Error en los datos ingresados: " + e.getMessage());
        }
    }
}