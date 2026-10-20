package com.gamezone.ui;

import com.gamezone.model.*;
import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.persistence.PromotionRepository;
import com.gamezone.persistence.ReturnRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.WarrantyRepository;
import com.gamezone.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de menú interactivo por consola completa con gestión de productos,
 * accesorios, personas, ventas, promociones, devoluciones y garantías.
 *
 * @author Samuel Angulo
 */
public class ConsoleUI {

    private ProductService productService;
    private AccessoryService accessoryService;
    private PersonService personService;
    private PromotionService promotionService;
    private WarrantyService warrantyService; // Servicio de garantías integrado
    private SaleService saleService;
    private ReturnService returnService; // Servicio de devoluciones integrado
    private Scanner scanner;

    public ConsoleUI() {
        this.productService = new ProductService();
        this.accessoryService = new AccessoryService(new AccessoryRepository());
        this.personService = new PersonService();
        this.promotionService = new PromotionService(new PromotionRepository());
        
        // Inicialización del servicio de garantías con su repositorio
        this.warrantyService = new WarrantyService(new WarrantyRepository());
        
        this.saleService = new SaleService(new SaleRepository(), productService, accessoryService, personService, promotionService, warrantyService);

        // Inicialización correcta de ReturnService con sus dependencias
        this.returnService = new ReturnService(new ReturnRepository(), saleService, productService, accessoryService);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int option;
        do {
            System.out.println("\n========================================");
            System.out.println("    GAMEZONE UNICESAR - MENÚ PRINCIPAL  ");
            System.out.println("========================================");
            System.out.println("1. Listar productos y accesorios");
            System.out.println("2. Agregar videojuego");
            System.out.println("3. Agregar consola");
            System.out.println("4. Registrar control (Accesorio)");
            System.out.println("5. Registrar cable (Accesorio)");
            System.out.println("6. Registrar memoria (Accesorio)");
            System.out.println("7. Listar personas (Clientes y vendedores)");
            System.out.println("8. Agregar cliente");
            System.out.println("9. Agregar vendedor");
            System.out.println("10. Listar ventas");
            System.out.println("11. Registrar nueva venta (Multiproducto)");
            System.out.println("12. Listar promociones activas");
            System.out.println("13. Registrar descuento por porcentaje");
            System.out.println("14. Registrar descuento por categoría");
            System.out.println("15. Registrar descuento por compra al por mayor");
            System.out.println("16. Gestión de devoluciones");
            System.out.println("17. Gestión de garantías");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            option = readInt("");

            switch (option) {
                case 1:
                    listProductsAndAccessories();
                    break;
                case 2:
                    addVideoGame();
                    break;
                case 3:
                    addConsole();
                    break;
                case 4:
                    registerController();
                    break;
                case 5:
                    registerCable();
                    break;
                case 6:
                    registerMemory();
                    break;
                case 7:
                    listPersons();
                    break;
                case 8:
                    addCustomer();
                    break;
                case 9:
                    addSeller();
                    break;
                case 10:
                    listSales();
                    break;
                case 11:
                    registerSale();
                    break;
                case 12:
                    listActivePromotions();
                    break;
                case 13:
                    registerPercentageDiscount();
                    break;
                case 14:
                    registerCategoryDiscount();
                    break;
                case 15:
                    registerBulkPurchaseDiscount();
                    break;
                case 16:
                    showReturnMenu();
                    break;
                case 17:
                    showWarrantyMenu();
                    break;
                case 0:
                    System.out.println("Saliendo de la aplicación. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (option != 0);
    }

    // --- MÉTODOS AUXILIARES DE ENTRADA ---
    private int readInt(String prompt) {
        while (true) {
            if (!prompt.isEmpty()) {
                System.out.print(prompt);
            }
            try {
                int val = Integer.parseInt(scanner.nextLine());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número entero válido.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(scanner.nextLine());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número decimal válido.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    // --- MÓDULO DE DEVOLUCIONES ---
    private void showReturnMenu() {
        int option;
        do {
            System.out.println("\n--- GESTIÓN DE DEVOLUCIONES ---");
            System.out.println("1. Registrar una nueva devolución");
            System.out.println("2. Consultar todas las devoluciones");
            System.out.println("3. Consultar devoluciones por cliente");
            System.out.println("4. Consultar devoluciones por venta");
            System.out.println("5. Consultar balance mensual");
            System.out.println("0. Volver");
            option = readInt("Seleccione una opción: ");
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
                                returnService.viewReturnsByCustomer(readLine("ID del cliente: ")));
                        break;
                    case 4:
                        printReturns("DEVOLUCIONES DE LA VENTA",
                                returnService.viewReturnsBySale(readLine("ID de la venta: ")));
                        break;
                    case 5:
                        int month = readInt("Mes (1-12): ");
                        int year = readInt("Año: ");
                        System.out.println(String.format("Balance neto de %02d/%d: $%.2f",
                                month, year, returnService.generateMonthlyBalance(month, year)));
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
        String saleId = readLine("ID de la venta: ");
        List<String> ids = new ArrayList<>();
        while (true) {
            String id = readLine("ID del producto a devolver (Enter para terminar): ");
            if (id.isEmpty()) {
                break;
            }
            ids.add(id);
        }
        String reason = readLine("Motivo de la devolución: ");
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

    // --- MÓDULO DE GARANTÍAS ---
    private void showWarrantyMenu() {
        int option;
        do {
            System.out.println("\n--- GESTIÓN DE GARANTÍAS ---");
            System.out.println("1. Consultar garantía de un producto en una venta");
            System.out.println("2. Listar todas las garantías");
            System.out.println("3. Listar garantías vigentes");
            System.out.println("4. Listar garantías próximas a vencer");
            System.out.println("0. Volver");
            option = readInt("Seleccione una opción: ");
            try {
                switch (option) {
                    case 1:
                        String saleId = readLine("ID de la venta: ");
                        String productId = readLine("ID del producto: ");
                        Warranty w = warrantyService.findWarrantyByProduct(productId, saleId);
                        System.out.println(w == null ? "No existe garantía para ese producto en esa venta."
                                : w.generateWarrantyCertificate());
                        break;
                    case 2: printWarranties("TODAS LAS GARANTÍAS", warrantyService.listAllWarranties()); break;
                    case 3: printWarranties("GARANTÍAS VIGENTES", warrantyService.listActiveWarranties()); break;
                    case 4: printWarranties("GARANTÍAS PRÓXIMAS A VENCER",
                            warrantyService.listWarrantiesExpiringSoon(readInt("Días de anticipación: "))); break;
                    case 0: break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("No se pudo completar la operación: " + e.getMessage());
            }
        } while (option != 0);
    }

    private void printWarranties(String title, List<Warranty> list) {
        System.out.println("\n--- " + title + " ---");
        if (list.isEmpty()) System.out.println("No hay garantías para mostrar.");
        for (Warranty w : list) {
            System.out.println("- " + w.getId() + " | " + w.getWarrantyType() + " | "
                    + w.getProduct().getTitle() + " | Venta " + w.getSale().getSaleId()
                    + " | Vence: " + w.getEndDate());
        }
    }

    // --- RESTO DE FUNCIONALIDADES EXISTENTES ---
    private void listProductsAndAccessories() {
        System.out.println("\n--- INVENTARIO DE PRODUCTOS Y ACCESORIOS ---");
        System.out.println("Productos:");
        for (Product p : productService.listAllProducts()) {
            System.out.println(" - " + p.getDescription());
        }
        System.out.println("Accesorios:");
        for (Accessory a : accessoryService.listAllAccessories()) {
            System.out.println(" - " + a.getDescription());
        }
    }

    private void addVideoGame() {
        String id = readLine("Ingrese ID: ");
        String title = readLine("Ingrese Título: ");
        double price = readDouble("Ingrese Precio: ");
        int stock = readInt("Ingrese Stock: ");
        String platform = readLine("Ingrese Plataforma: ");
        String genre = readLine("Ingrese Género: ");
        String rating = readLine("Ingrese Clasificación por edad: ");

        VideoGame vg = new VideoGame(id, title, price, stock, platform, genre, rating);
        productService.addProduct(vg);
        System.out.println("¡Videojuego agregado exitosamente!");
    }

    private void addConsole() {
        String id = readLine("Ingrese ID: ");
        String title = readLine("Ingrese Título: ");
        double price = readDouble("Ingrese Precio: ");
        int stock = readInt("Ingrese Stock: ");
        String brand = readLine("Ingrese Marca: ");
        String model = readLine("Ingrese Modelo: ");
        String generation = readLine("Ingrese Generación: ");

        com.gamezone.model.Console c = new com.gamezone.model.Console(id, title, price, stock, brand, model, generation);
        productService.addProduct(c);
        System.out.println("¡Consola agregada exitosamente!");
    }

    private void registerController() {
        String id = readLine("Ingrese ID: ");
        String title = readLine("Ingrese Título: ");
        double price = readDouble("Ingrese Precio: ");
        int stock = readInt("Ingrese Stock: ");
        String connType = readLine("Ingrese Tipo de Conexión (Wireless/Wired): ");

        List<String> compatibleConsoles = promptCompatibleConsoles();

        accessoryService.registerController(id, title, price, stock, compatibleConsoles, connType);
        System.out.println("¡Control registrado exitosamente!");
    }

    private void registerCable() {
        String id = readLine("Ingrese ID: ");
        String title = readLine("Ingrese Título: ");
        double price = readDouble("Ingrese Precio: ");
        int stock = readInt("Ingrese Stock: ");
        double length = readDouble("Ingrese Longitud (Metros): ");
        String connector = readLine("Ingrese Tipo de Conector: ");

        List<String> compatibleConsoles = promptCompatibleConsoles();

        accessoryService.registerCable(id, title, price, stock, compatibleConsoles, length, connector);
        System.out.println("¡Cable registrado exitosamente!");
    }

    private void registerMemory() {
        String id = readLine("Ingrese ID: ");
        String title = readLine("Ingrese Título: ");
        double price = readDouble("Ingrese Precio: ");
        int stock = readInt("Ingrese Stock: ");
        int capacity = readInt("Ingrese Capacidad (GB): ");
        String memoryType = readLine("Ingrese Tipo de Memoria: ");

        List<String> compatibleConsoles = promptCompatibleConsoles();

        accessoryService.registerMemory(id, title, price, stock, compatibleConsoles, capacity, memoryType);
        System.out.println("¡Memoria registrada exitosamente!");
    }

    private List<String> promptCompatibleConsoles() {
        List<String> compatibleConsoles = new ArrayList<>();
        String opt = readLine("¿Agregar ID de consola compatible? (s/n): ");
        while (opt.equalsIgnoreCase("s") || opt.equalsIgnoreCase("y")) {
            compatibleConsoles.add(readLine("Ingrese ID de la consola: "));
            opt = readLine("¿Agregar otra consola compatible? (s/n): ");
        }
        return compatibleConsoles;
    }

    private void listPersons() {
        System.out.println("\n--- DIRECTORIO DE PERSONAS ---");
        for (Person p : personService.listAllPersons()) {
            System.out.println(p.getDetails());
        }
    }

    private void addCustomer() {
        String id = readLine("Ingrese ID: ");
        String name = readLine("Ingrese Nombre: ");
        String phone = readLine("Ingrese Teléfono: ");
        String email = readLine("Ingrese Correo Electrónico: ");

        Customer customer = new Customer(id, name, phone, email);
        personService.addPerson(customer);
        System.out.println("¡Cliente agregado exitosamente!");
    }

    private void addSeller() {
        String id = readLine("Ingrese ID: ");
        String name = readLine("Ingrese Nombre: ");
        String phone = readLine("Ingrese Teléfono: ");
        String code = readLine("Ingrese Código de Empleado: ");
        String shift = readLine("Ingrese Turno de Trabajo: ");

        Seller seller = new Seller(id, name, phone, code, shift);
        personService.addPerson(seller);
        System.out.println("¡Vendedor agregado exitosamente!");
    }

    private void listSales() {
        System.out.println("\n--- TRANSACCIONES DE VENTAS ---");
        for (Sale s : saleService.listAllSales()) {
            System.out.println(s.generateReceipt());
            System.out.println("----------------------------------------");
        }
    }

    private void registerSale() {
        String saleId = readLine("Ingrese ID de la Venta: ");

        String custId = readLine("Ingrese ID del Cliente: ");
        Customer customer = personService.findCustomerById(custId);
        if (customer == null) {
            System.out.println("¡Cliente no encontrado!");
            return;
        }

        String sellId = readLine("Ingrese ID del Vendedor: ");
        Seller seller = personService.findSellerById(sellId);
        if (seller == null) {
            System.out.println("¡Vendedor no encontrado!");
            return;
        }

        List<Product> items = new ArrayList<>();
        String choice;
        do {
            String itemId = readLine("Ingrese ID del producto o accesorio a agregar: ");

            Product product = productService.findById(itemId);
            if (product == null) {
                product = accessoryService.findById(itemId);
            }

            if (product != null) {
                items.add(product);
                System.out.println("Ítem agregado al carrito de venta.");
            } else {
                System.out.println("¡Producto o accesorio no encontrado!");
            }

            choice = readLine("¿Agregar otro ítem a esta venta? (s/n): ");
        } while (choice.equalsIgnoreCase("s") || choice.equalsIgnoreCase("y"));

        if (items.isEmpty()) {
            System.out.println("Venta cancelada. No se agregaron ítems.");
            return;
        }

        // Selección de garantías extendidas para las consolas compradas
        List<String> extendedIds = new ArrayList<>();
        for (Product item : items) {
            if (item instanceof com.gamezone.model.Console && !extendedIds.contains(item.getId())) {
                if (readLine("¿Agregar garantía extendida a " + item.getTitle() + "? (s/n): ")
                        .equalsIgnoreCase("s")) {
                    extendedIds.add(item.getId());
                }
            }
        }

        boolean success = saleService.registerSale(saleId, customer, seller, items, extendedIds);
        if (success) {
            System.out.println("¡Venta registrada exitosamente!");
        } else {
            System.out.println("Error al registrar la venta. Verifique la disponibilidad de stock.");
        }
    }

    private void listActivePromotions() {
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

    private void registerPercentageDiscount() {
        try {
            String id = readLine("Ingrese ID de la promoción: ");
            String name = readLine("Ingrese Nombre de la promoción: ");
            LocalDate start = LocalDate.parse(readLine("Ingrese Fecha de inicio (YYYY-MM-DD): "));
            LocalDate end = LocalDate.parse(readLine("Ingrese Fecha de fin (YYYY-MM-DD): "));
            double percentage = readDouble("Ingrese Porcentaje de descuento (ej. 15.0): ");

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

    private void registerCategoryDiscount() {
        try {
            String id = readLine("Ingrese ID de la promoción: ");
            String name = readLine("Ingrese Nombre de la promoción: ");
            LocalDate start = LocalDate.parse(readLine("Ingrese Fecha de inicio (YYYY-MM-DD): "));
            LocalDate end = LocalDate.parse(readLine("Ingrese Fecha de fin (YYYY-MM-DD): "));
            double percentage = readDouble("Ingrese Porcentaje de descuento: ");
            String category = readLine("Ingrese Categoría (VIDEOGAME o CONSOLE): ");

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

    private void registerBulkPurchaseDiscount() {
        try {
            String id = readLine("Ingrese ID de la promoción: ");
            String name = readLine("Ingrese Nombre de la promoción: ");
            LocalDate start = LocalDate.parse(readLine("Ingrese Fecha de inicio (YYYY-MM-DD): "));
            LocalDate end = LocalDate.parse(readLine("Ingrese Fecha de fin (YYYY-MM-DD): "));
            int minQty = readInt("Ingrese Cantidad mínima de ítems: ");
            double percentage = readDouble("Ingrese Porcentaje de descuento: ");

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