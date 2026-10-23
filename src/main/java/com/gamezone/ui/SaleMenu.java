package com.gamezone.ui;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Seller;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import java.util.ArrayList;
import java.util.List;

public class SaleMenu {
    private final SaleService saleService;
    private final PersonService personService;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final ConsoleInput input;
    private final ConsolePrinter printer;

    public SaleMenu(SaleService saleService, PersonService personService, ProductService productService,
                      AccessoryService accessoryService, ConsoleInput input, ConsolePrinter printer) {
        this.saleService = saleService;
        this.personService = personService;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.input = input;
        this.printer = printer;
    }
    
    public void show() {
    int option;
    do {
        System.out.println("\n--- GESTIÓN DE VENTAS ---");
        System.out.println("1. Listar ventas");
        System.out.println("2. Registrar nueva venta (Multiproducto)");
        System.out.println("0. Volver");
        option = input.readInt("Seleccione una opción: ");

        switch (option) {
            case 1: listSales(); break;
            case 2: registerSale(); break;
            case 0: break;
            default: System.out.println("Opción inválida.");
        }
    } while (option != 0);
}

    public void listSales() {
        printer.printSales(saleService.listSales());
    }

    public void registerSale() {
        String saleId = input.readLine("ID de la venta: ");
        Customer customer = personService.findCustomerById(input.readLine("ID del cliente: "));
        if (customer == null) {
            System.out.println("Cliente no encontrado. Venta cancelada.");
            return;
        }
        printer.printSellers(personService.listAllPersons());
        Seller seller = personService.findSellerById(input.readLine("ID del vendedor: "));
        if (seller == null) {
            System.out.println("Vendedor no encontrado. Venta cancelada.");
            return;
        }

        List<Product> items = new ArrayList<>();
        boolean addMore = true;
        while (addMore) {
            String kind = input.readLine("¿Agregar accesorio (A) o producto (P)? ");
            Product item;
            if (kind.equalsIgnoreCase("A")) {
                printer.printAccessories("ACCESORIOS DISPONIBLES", accessoryService.listAllAccessories());
                item = accessoryService.findById(input.readLine("ID del accesorio: "));
            } else {
                printer.printProducts(productService.listAllProducts());
                item = productService.findById(input.readLine("ID del producto: "));
            }
            if (item == null) {
                System.out.println("Ítem no encontrado.");
            } else {
                items.add(item);
                System.out.println("Ítem agregado: " + item.getTitle());
            }
            addMore = input.readLine("¿Agregar otro ítem? (s/n): ").equalsIgnoreCase("s");
        }
        if (items.isEmpty()) {
            System.out.println("La venta debe tener al menos un ítem.");
            return;
        }

        List<String> extendedIds = new ArrayList<>();
        for (Product item : items) {
            if (item.supportsWarranty() && !extendedIds.contains(item.getId())) {
                if (input.readLine("¿Agregar garantía extendida a " + item.getTitle() + "? (s/n): ")
                        .equalsIgnoreCase("s")) {
                    extendedIds.add(item.getId());
                }
            }
        }

        if (saleService.registerSale(saleId, customer, seller, items, extendedIds)) {
            System.out.println("¡Venta registrada exitosamente!");
            System.out.println(saleService.findSaleById(saleId).generateReceipt());
        } else {
            System.out.println("No se pudo registrar la venta (revise el stock o el ID de la venta).");
        }
    }
}