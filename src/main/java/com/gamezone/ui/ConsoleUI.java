package com.gamezone.ui;

import com.gamezone.model.Accessory;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Seller;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main console user interface for managing GameZone Unicesar system operations,
 * including products, accessories, and sales.
 */
public class ConsoleUI {
    private Scanner scanner;
    private ProductService productService;
    private AccessoryService accessoryService;
    private SaleService saleService;

    public ConsoleUI(ProductService productService, AccessoryService accessoryService, SaleService saleService) {
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.saleService = saleService;
        this.scanner = new Scanner(System.in);
    }

    public void showMainMenu() {
        int option;
        do {
            System.out.println("\n=== GAME ZONE UNICESAR - MAIN MENU ===");
            System.out.println("1. Gestión de productos (Consolas y Videojuegos)");
            System.out.println("2. Gestión de accesorios");
            System.out.println("3. Gestión de ventas");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            
            while (!scanner.hasNextInt()) {
                System.out.print("Por favor, ingrese un número válido: ");
                scanner.next();
            }
            option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    // Submenú de productos existente (ejemplo placeholder o llamada a método existente)
                    System.out.println("[Módulo de productos existente]");
                    break;
                case 2:
                    showAccessorySubMenu();
                    break;
                case 3:
                    showSalesSubMenu();
                    break;
                case 0:
                    System.out.println("¡Gracias por usar GameZone Unicesar!");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (option != 0);
    }

    private void showAccessorySubMenu() {
        int option;
        do {
            System.out.println("\n--- GESTIÓN DE ACCESORIOS ---");
            System.out.println("1. Registrar un nuevo control");
            System.out.println("2. Registrar un nuevo cable");
            System.out.println("3. Registrar una nueva memoria");
            System.out.println("4. Listar todos los accesorios");
            System.out.println("5. Listar accesorios por tipo");
            System.out.println("6. Consultar accesorios compatibles con una consola");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Por favor, ingrese un número válido: ");
                scanner.next();
            }
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    registerControllerPrompt();
                    break;
                case 2:
                    registerCablePrompt();
                    break;
                case 3:
                    registerMemoryPrompt();
                    break;
                case 4:
                    listAllAccessories();
                    break;
                case 5:
                    listAccessoriesByTypePrompt();
                    break;
                case 6:
                    findAccessoriesByConsolePrompt();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (option != 0);
    }

    private void registerControllerPrompt() {
        System.out.print("Ingrese ID del control: ");
        String id = scanner.nextLine();
        System.out.print("Ingrese título del control: ");
        String title = scanner.nextLine();
        System.out.print("Ingrese precio: ");
        double price = scanner.nextDouble();
        System.out.print("Ingrese stock disponible: ");
        int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese IDs de consolas compatibles (separados por punto y coma ';'): ");
        String consolesInput = scanner.nextLine();
        List<String> consoles = List.of(consolesInput.split(";"));
        System.out.print("Ingrese tipo de conexión (Alámbrico / Inalámbrico): ");
        String connectionType = scanner.nextLine();

        accessoryService.registerController(id, title, price, stock, consoles, connectionType);
        System.out.println("¡Control registrado exitosamente!");
    }

    private void registerCablePrompt() {
        System.out.print("Ingrese ID del cable: ");
        String id = scanner.nextLine();
        System.out.print("Ingrese título del cable: ");
        String title = scanner.nextLine();
        System.out.print("Ingrese precio: ");
        double price = scanner.nextDouble();
        System.out.print("Ingrese stock disponible: ");
        int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese IDs de consolas compatibles (separados por ';'): ");
        String consolesInput = scanner.nextLine();
        List<String> consoles = List.of(consolesInput.split(";"));
        System.out.print("Ingrese longitud en metros (ej. 1.5): ");
        double length = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Ingrese tipo de conector (HDMI, USB, Óptico, etc.): ");
        String connector = scanner.nextLine();

        accessoryService.registerCable(id, title, price, stock, consoles, length, connector);
        System.out.println("¡Cable registrado exitosamente!");
    }

    private void registerMemoryPrompt() {
        System.out.print("Ingrese ID de la memoria: ");
        String id = scanner.nextLine();
        System.out.print("Ingrese título de la memoria: ");
        String title = scanner.nextLine();
        System.out.print("Ingrese precio: ");
        double price = scanner.nextDouble();
        System.out.print("Ingrese stock disponible: ");
        int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese IDs de consolas compatibles (separados por ';'): ");
        String consolesInput = scanner.nextLine();
        List<String> consoles = List.of(consolesInput.split(";"));
        System.out.print("Ingrese capacidad en GB: ");
        int capacity = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese tipo de memoria (SD, MicroSD, Interna, etc.): ");
        String memoryType = scanner.nextLine();

        accessoryService.registerMemory(id, title, price, stock, consoles, capacity, memoryType);
        System.out.println("¡Memoria registrada exitosamente!");
    }

    private void listAllAccessories() {
        List<Accessory> accessories = accessoryService.listAllAccessories();
        System.out.println("\n--- LISTADO COMPLETO DE ACCESORIOS ---");
        if (accessories.isEmpty()) {
            System.out.println("No hay accesorios registrados.");
        } else {
            for (Accessory acc : accessories) {
                System.out.println("- " + acc.getDescription());
            }
        }
    }

    private void listAccessoriesByTypePrompt() {
        System.out.print("Ingrese el tipo de accesorio a buscar (CONTROLLER / CABLE / MEMORY): ");
        String type = scanner.nextLine();
        List<Accessory> filtered = accessoryService.listAccessoriesByType(type);
        System.out.println("\n--- ACCESORIOS DE TIPO: " + type.toUpperCase() + " ---");
        if (filtered.isEmpty()) {
            System.out.println("No se encontraron accesorios para este tipo.");
        } else {
            for (Accessory acc : filtered) {
                System.out.println("- " + acc.getDescription());
            }
        }
    }

    private void findAccessoriesByConsolePrompt() {
        System.out.print("Ingrese el ID de la consola (ej. CONSOLE-01): ");
        String consoleId = scanner.nextLine();
        List<Accessory> compatible = accessoryService.findAccessoriesCompatibleWith(consoleId);
        System.out.println("\n--- ACCESORIOS COMPATIBLES CON " + consoleId + " ---");
        if (compatible.isEmpty()) {
            System.out.println("No hay accesorios compatibles con esta consola.");
        } else {
            for (Accessory acc : compatible) {
                System.out.println("- " + acc.getDescription());
            }
        }
    }

    private void showSalesSubMenu() {
        System.out.println("\n--- GESTIÓN DE VENTAS ---");
        System.out.print("Ingrese ID de la venta: ");
        String saleId = scanner.nextLine();
        
        // Simulación básica de captura de ítems unificados (Productos o Accesorios)
        List<Product> purchasedItems = new ArrayList<>();
        char addMore;
        do {
            System.out.print("¿Desea agregar un Accesorio (A) o un Producto general/consola (P)? [A/P]: ");
            String typeChoice = scanner.nextLine();

            if (typeChoice.equalsIgnoreCase("A")) {
                listAllAccessories();
                System.out.print("Ingrese el ID del accesorio a comprar: ");
                String accId = scanner.nextLine();
                Accessory acc = accessoryService.findById(accId);
                if (acc != null) {
                    purchasedItems.add(acc);
                    System.out.println("Accesorio agregado a la venta.");
                } else {
                    System.out.println("Accesorio no encontrado.");
                }
            } else {
                System.out.print("Ingrese el ID del producto general: ");
                String prodId = scanner.nextLine();
                Product prod = productService.findById(prodId);
                if (prod != null) {
                    purchasedItems.add(prod);
                    System.out.println("Producto agregado a la venta.");
                } else {
                    System.out.println("Producto no encontrado.");
                }
            }

            System.out.print("¿Desea agregar otro ítem a la venta? (s/n): ");
            addMore = scanner.nextLine().charAt(0);
        } while (addMore == 's' || addMore == 'S');

        if (!purchasedItems.isEmpty()) {
            // Nota: Se pueden instanciar o recibir Customer y Seller reales de los servicios correspondientes
            boolean success = saleService.registerSale(saleId, null, null, purchasedItems);
            if (success) {
                System.out.println("¡Venta registrada exitosamente con actualización de stock unificada!");
            } else {
                System.out.println("Error al registrar la venta (verifique stock disponible).");
            }
        } else {
            System.out.println("No se agregaron ítems a la venta.");
        }
    }
}