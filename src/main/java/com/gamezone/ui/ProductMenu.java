package com.gamezone.ui;

import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.service.ProductService;
import java.util.ArrayList;
import java.util.List;

public class ProductMenu {
    private final ProductService productService;
    private final ConsoleInput input;
    private final ConsolePrinter printer;

    public ProductMenu(ProductService productService, ConsoleInput input, ConsolePrinter printer) {
        this.productService = productService;
        this.input = input;
        this.printer = printer;
    }

    public void listProductsAndAccessories(com.gamezone.service.AccessoryService accessoryService) {
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
