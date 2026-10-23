package com.gamezone.ui;

import com.gamezone.model.Warranty;
import com.gamezone.service.WarrantyService;
import java.util.List;

public class WarrantyMenu {
    private final WarrantyService warrantyService;
    private final ConsoleInput input;

    public WarrantyMenu(WarrantyService warrantyService, ConsoleInput input) {
        this.warrantyService = warrantyService;
        this.input = input;
    }

    public void show() {
        int option;
        do {
            System.out.println("\n--- GESTIÓN DE GARANTÍAS ---");
            System.out.println("1. Consultar garantía de un producto en una venta");
            System.out.println("2. Listar todas las garantías");
            System.out.println("3. Listar garantías vigentes");
            System.out.println("4. Listar garantías próximas a vencer");
            System.out.println("0. Volver");
            option = input.readInt("Seleccione una opción: ");
            try {
                switch (option) {
                    case 1:
                        String saleId = input.readLine("ID de la venta: ");
                        String productId = input.readLine("ID del producto: ");
                        Warranty w = warrantyService.findWarrantyByProduct(productId, saleId);
                        System.out.println(w == null ? "No existe garantía para ese producto en esa venta."
                                : w.generateWarrantyCertificate());
                        break;
                    case 2:
                        printWarranties("TODAS LAS GARANTÍAS", warrantyService.listAllWarranties());
                        break;
                    case 3:
                        printWarranties("GARANTÍAS VIGENTES", warrantyService.listActiveWarranties());
                        break;
                    case 4:
                        printWarranties("GARANTÍAS PRÓXIMAS A VENCER",
                                warrantyService.listWarrantiesExpiringSoon(input.readInt("Días de anticipación: ")));
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("No se pudo completar la operación: " + e.getMessage());
            }
        } while (option != 0);
    }

    private void printWarranties(String title, List<Warranty> list) {
        System.out.println("\n--- " + title + " ---");
        if (list.isEmpty()) {
            System.out.println("No hay garantías para mostrar.");
        }
        for (Warranty w : list) {
            System.out.println("- " + w.getId() + " | " + w.getWarrantyType() + " | "
                    + w.getProduct().getTitle() + " | Venta " + w.getSale().getSaleId()
                    + " | Vence: " + w.getEndDate());
        }
    }
}