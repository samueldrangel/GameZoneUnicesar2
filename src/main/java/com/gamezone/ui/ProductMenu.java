package com.gamezone.ui;

import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.ProductService;
import java.util.ArrayList;
import java.util.List;

public class ProductMenu {
    private final ProductService productService;
    private final AccessoryService accessoryService; // <-- Asegúrate de tener este atributo
    private final ConsoleInput input;
    private final ConsolePrinter printer;

    // El constructor DEBE recibir accessoryService
    public ProductMenu(ProductService productService, AccessoryService accessoryService, ConsoleInput input, ConsolePrinter printer) {
        this.productService = productService;
        this.accessoryService = accessoryService; // <-- Y asignarlo aquí
        this.input = input;
        this.printer = printer;
    }
    
    public void show() {
    int option;
    do {
        System.out.println("\n--- GESTIÓN DE PRODUCTOS ---");
        System.out.println("1. Listar productos y accesorios");
        System.out.println("2. Agregar videojuego");
        System.out.println("3. Agregar consola");
        System.out.println("0. Volver");
        option = input.readInt("Seleccione una opción: ");

        switch (option) {
            case 1: listProductsAndAccessories(this.accessoryService); break;
            case 2: addVideoGame(); break;
            case 3: addConsole(); break;
            case 0: break;
            default: System.out.println("Opción inválida.");
        }
    } while (option != 0);
}

    public void listProductsAndAccessories(AccessoryService accessoryService) {
    System.out.println("\n--- INVENTARIO DE PRODUCTOS Y ACCESORIOS ---");
    System.out.println("Productos:");
    for (Product p : productService.listAllProducts()) {
        System.out.println(" - " + p.getDescription());
    }
    System.out.println("Accesorios:");
    for (com.gamezone.model.Accessory a : accessoryService.listAllAccessories()) {
        System.out.println(" - " + a.getDescription());
    }
}

    public void addVideoGame() {
        String id = input.readLine("Ingrese ID: ");
        String title = input.readLine("Ingrese Título: ");
        double price = input.readDouble("Ingrese Precio: ");
        int stock = input.readInt("Ingrese Stock: ");
        String platform = input.readLine("Ingrese Plataforma: ");
        String genre = input.readLine("Ingrese Género: ");
        String rating = input.readLine("Ingrese Clasificación por edad: ");

        VideoGame vg = new VideoGame(id, title, price, stock, platform, genre, rating);
        productService.addProduct(vg);
        System.out.println("¡Videojuego agregado exitosamente!");
    }

    public void addConsole() {
        String id = input.readLine("Ingrese ID: ");
        String title = input.readLine("Ingrese Título: ");
        double price = input.readDouble("Ingrese Precio: ");
        int stock = input.readInt("Ingrese Stock: ");
        String brand = input.readLine("Ingrese Marca: ");
        String model = input.readLine("Ingrese Modelo: ");
        String generation = input.readLine("Ingrese Generación: ");

        com.gamezone.model.Console c = new com.gamezone.model.Console(id, title, price, stock, brand, model, generation);
        productService.addProduct(c);
        System.out.println("¡Consola agregada exitosamente!");
    }
}
