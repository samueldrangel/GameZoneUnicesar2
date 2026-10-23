package com.gamezone.ui;

import com.gamezone.model.Return;
import com.gamezone.service.ReturnService;
import java.util.ArrayList;
import java.util.List;

public class ReturnMenu {
    private final ReturnService returnService;
    private final ConsoleInput input;

    public ReturnMenu(ReturnService returnService, ConsoleInput input) {
        this.returnService = returnService;
        this.input = input;
    }

    public void showReturnMenu() {
        int option;
        do {
            System.out.println("\n--- GESTIÓN DE DEVOLUCIONES ---");
            System.out.println("1. Registrar una nueva devolución");
            System.out.println("2. Consultar todas las devoluciones");
            System.out.println("3. Consultar devoluciones por cliente");
            System.out.println("4. Consultar devoluciones por venta");
            System.out.println("5. Consultar balance mensual");
            System.out.println("0. Volver");
            option = input.readInt("Seleccione una opción: ");
            try {
                switch (option) {
                    case 1:
                        registerReturn();
                        break;
                    case 2:
                        printReturns("TODAS LAS DEVOLUCIONES", returnService.viewAllReturns());
                        break;
                    case 3:
                        printReturns("DEVOLUCIONES DEL CLIENTE",
                                returnService.viewReturnsByCustomer(input.readLine("ID del cliente: ")));
                        break;
                    case 4:
                        printReturns("DEVOLUCIONES DE LA VENTA",
                                returnService.viewReturnsBySale(input.readLine("ID de la venta: ")));
                        break;
                    case 5:
                        int month = input.readInt("Mes (1-12): ");
                        int year = input.readInt("Año: ");
                        System.out.println(String.format("Total de ventas: $%.2f", returnService.calculateMonthlySales(month, year)));
                        System.out.println(String.format("Total de devoluciones: $%.2f", returnService.calculateMonthlyReturns(month, year)));
                        System.out.println(String.format("Balance neto: $%.2f", returnService.generateMonthlyBalance(month, year)));
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

    private void registerReturn() {
        String saleId = input.readLine("ID de la venta: ");
        List<String> ids = new ArrayList<>();
        while (true) {
            String id = input.readLine("ID del producto a devolver (Enter para terminar): ");
            if (id.isEmpty()) {
                break;
            }
            ids.add(id);
        }
        String reason = input.readLine("Motivo de la devolución: ");
        Return r = returnService.registerReturn(saleId, ids, reason);
        System.out.println("¡Devolución registrada exitosamente!");
        System.out.println(r.generateReturnReceipt());
    }

    private void printReturns(String title, List<Return> list) {
        System.out.println("\n--- " + title + " ---");
        if (list.isEmpty()) {
            System.out.println("No hay devoluciones para mostrar.");
        } else {
            for (Return r : list) {
                System.out.println(r.generateReturnReceipt() + "\n");
            }
        }
    }
}
