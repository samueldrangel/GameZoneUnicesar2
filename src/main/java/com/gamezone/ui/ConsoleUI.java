package com.gamezone.ui;

import com.gamezone.model.*;
import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.persistence.PromotionRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de menú interactivo por consola completa con gestión de productos, 
 * accesorios, personas, ventas y promociones.
 * 
 * @author Samuel Angulo
 */
public class ConsoleUI {
    private ProductService productService;
    private AccessoryService accessoryService;
    private PersonService personService;
    private PromotionService promotionService;
    private SaleService saleService;
    private Scanner scanner;

    public ConsoleUI() {
        this.productService = new ProductService();
        this.accessoryService = new AccessoryService(new AccessoryRepository());
        this.personService = new PersonService();
        this.promotionService = new PromotionService(new PromotionRepository());
        this.saleService = new SaleService(new SaleRepository(), productService, accessoryService, personService, promotionService);
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
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            
            option = scanner.nextInt();
            scanner.nextLine(); // Consumir salto de línea

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
                case 0:
                    System.out.println("Saliendo de la aplicación. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (option != 0);
    }

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
        System.out.print("Ingrese ID: "); String id = scanner.nextLine();
        System.out.print("Ingrese Título: "); String title = scanner.nextLine();
        System.out.print("Ingrese Precio: "); double price = scanner.nextDouble();
        System.out.print("Ingrese Stock: "); int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese Plataforma: "); String platform = scanner.nextLine();
        System.out.print("Ingrese Género: "); String genre = scanner.nextLine();
        System.out.print("Ingrese Clasificación por edad: "); String rating = scanner.nextLine();

        VideoGame vg = new VideoGame(id, title, price, stock, platform, genre, rating);
        productService.addProduct(vg);
        System.out.println("¡Videojuego agregado exitosamente!");
    }

    private void addConsole() {
        System.out.print("Ingrese ID: "); String id = scanner.nextLine();
        System.out.print("Ingrese Título: "); String title = scanner.nextLine();
        System.out.print("Ingrese Precio: "); double price = scanner.nextDouble();
        System.out.print("Ingrese Stock: "); int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese Marca: "); String brand = scanner.nextLine();
        System.out.print("Ingrese Modelo: "); String model = scanner.nextLine();
        System.out.print("Ingrese Generación: "); String generation = scanner.nextLine();

        com.gamezone.model.Console c = new com.gamezone.model.Console(id, title, price, stock, brand, model, generation);
        productService.addProduct(c);
        System.out.println("¡Consola agregada exitosamente!");
    }

    private void registerController() {
        System.out.print("Ingrese ID: "); String id = scanner.nextLine();
        System.out.print("Ingrese Título: "); String title = scanner.nextLine();
        System.out.print("Ingrese Precio: "); double price = scanner.nextDouble();
        System.out.print("Ingrese Stock: "); int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese Tipo de Conexión (Wireless/Wired): "); String connType = scanner.nextLine();

        List<String> compatibleConsoles = promptCompatibleConsoles();

        accessoryService.registerController(id, title, price, stock, compatibleConsoles, connType);
        System.out.println("¡Control registrado exitosamente!");
    }

    private void registerCable() {
        System.out.print("Ingrese ID: "); String id = scanner.nextLine();
        System.out.print("Ingrese Título: "); String title = scanner.nextLine();
        System.out.print("Ingrese Precio: "); double price = scanner.nextDouble();
        System.out.print("Ingrese Stock: "); int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese Longitud (Metros): "); double length = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Ingrese Tipo de Conector: "); String connector = scanner.nextLine();

        List<String> compatibleConsoles = promptCompatibleConsoles();

        accessoryService.registerCable(id, title, price, stock, compatibleConsoles, length, connector);
        System.out.println("¡Cable registrado exitosamente!");
    }

    private void registerMemory() {
        System.out.print("Ingrese ID: "); String id = scanner.nextLine();
        System.out.print("Ingrese Título: "); String title = scanner.nextLine();
        System.out.print("Ingrese Precio: "); double price = scanner.nextDouble();
        System.out.print("Ingrese Stock: "); int stock = scanner.nextInt();
        System.out.print("Ingrese Capacidad (GB): "); int capacity = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese Tipo de Memoria: "); String memoryType = scanner.nextLine();

        List<String> compatibleConsoles = promptCompatibleConsoles();

        accessoryService.registerMemory(id, title, price, stock, compatibleConsoles, capacity, memoryType);
        System.out.println("¡Memoria registrada exitosamente!");
    }

    private List<String> promptCompatibleConsoles() {
        List<String> compatibleConsoles = new ArrayList<>();
        System.out.print("¿Agregar ID de consola compatible? (s/n): ");
        String opt = scanner.nextLine();
        while (opt.equalsIgnoreCase("s") || opt.equalsIgnoreCase("y")) {
            System.out.print("Ingrese ID de la consola: ");
            compatibleConsoles.add(scanner.nextLine());
            System.out.print("¿Agregar otra consola compatible? (s/n): ");
            opt = scanner.nextLine();
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
        System.out.print("Ingrese ID: "); String id = scanner.nextLine();
        System.out.print("Ingrese Nombre: "); String name = scanner.nextLine();
        System.out.print("Ingrese Teléfono: "); String phone = scanner.nextLine();
        System.out.print("Ingrese Correo Electrónico: "); String email = scanner.nextLine();

        Customer customer = new Customer(id, name, phone, email);
        personService.addPerson(customer);
        System.out.println("¡Cliente agregado exitosamente!");
    }

    private void addSeller() {
        System.out.print("Ingrese ID: "); String id = scanner.nextLine();
        System.out.print("Ingrese Nombre: "); String name = scanner.nextLine();
        System.out.print("Ingrese Teléfono: "); String phone = scanner.nextLine();
        System.out.print("Ingrese Código de Empleado: "); String code = scanner.nextLine();
        System.out.print("Ingrese Turno de Trabajo: "); String shift = scanner.nextLine();

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
        System.out.print("Ingrese ID de la Venta: "); String saleId = scanner.nextLine();
        
        System.out.print("Ingrese ID del Cliente: "); String custId = scanner.nextLine();
        Customer customer = personService.findCustomerById(custId);
        if (customer == null) {
            System.out.println("¡Cliente no encontrado!");
            return;
        }

        System.out.print("Ingrese ID del Vendedor: "); String sellId = scanner.nextLine();
        Seller seller = personService.findSellerById(sellId);
        if (seller == null) {
            System.out.println("¡Vendedor no encontrado!");
            return;
        }

        List<Product> purchasedItems = new ArrayList<>();
        String choice;
        do {
            System.out.print("Ingrese ID del producto o accesorio a agregar: "); 
            String itemId = scanner.nextLine();
            
            Product product = productService.findById(itemId);
            if (product == null) {
                product = accessoryService.findById(itemId);
            }

            if (product != null) {
                purchasedItems.add(product);
                System.out.println("Ítem agregado al carrito de venta.");
            } else {
                System.out.println("¡Producto o accesorio no encontrado!");
            }

            System.out.print("¿Agregar otro ítem a esta venta? (s/n): ");
            choice = scanner.nextLine();
        } while (choice.equalsIgnoreCase("s") || choice.equalsIgnoreCase("y"));

        if (purchasedItems.isEmpty()) {
            System.out.println("Venta cancelada. No se agregaron ítems.");
            return;
        }

        boolean success = saleService.registerSale(saleId, customer, seller, purchasedItems);
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
            System.out.print("Ingrese ID de la promoción: "); String id = scanner.nextLine();
            System.out.print("Ingrese Nombre de la promoción: "); String name = scanner.nextLine();
            System.out.print("Ingrese Fecha de inicio (YYYY-MM-DD): "); LocalDate start = LocalDate.parse(scanner.nextLine());
            System.out.print("Ingrese Fecha de fin (YYYY-MM-DD): "); LocalDate end = LocalDate.parse(scanner.nextLine());
            System.out.print("Ingrese Porcentaje de descuento (ej. 15.0): "); double percentage = scanner.nextDouble();
            scanner.nextLine();

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
            System.out.print("Ingrese ID de la promoción: "); String id = scanner.nextLine();
            System.out.print("Ingrese Nombre de la promoción: "); String name = scanner.nextLine();
            System.out.print("Ingrese Fecha de inicio (YYYY-MM-DD): "); LocalDate start = LocalDate.parse(scanner.nextLine());
            System.out.print("Ingrese Fecha de fin (YYYY-MM-DD): "); LocalDate end = LocalDate.parse(scanner.nextLine());
            System.out.print("Ingrese Porcentaje de descuento: "); double percentage = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Ingrese Categoría (VIDEOGAME o CONSOLE): "); String category = scanner.nextLine();

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
            System.out.print("Ingrese ID de la promoción: "); String id = scanner.nextLine();
            System.out.print("Ingrese Nombre de la promoción: "); String name = scanner.nextLine();
            System.out.print("Ingrese Fecha de inicio (YYYY-MM-DD): "); LocalDate start = LocalDate.parse(scanner.nextLine());
            System.out.print("Ingrese Fecha de fin (YYYY-MM-DD): "); LocalDate end = LocalDate.parse(scanner.nextLine());
            System.out.print("Ingrese Cantidad mínima de ítems: "); int minQty = scanner.nextInt();
            System.out.print("Ingrese Porcentaje de descuento: "); double percentage = scanner.nextDouble();
            scanner.nextLine();

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