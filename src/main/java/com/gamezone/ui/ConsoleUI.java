package com.gamezone.ui;

import com.gamezone.model.*;
import com.gamezone.persistence.*;
import com.gamezone.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final ConsoleInput input;
    private final ProductMenu productMenu;
    private final AccessoryMenu accessoryMenu;
    private final PersonMenu personMenu;
    private final SaleMenu saleMenu;
    private final PromotionMenu promotionMenu;
    private final WarrantyMenu warrantyMenu;
    private final ReturnMenu returnMenu;

    // Constructor por defecto que inicializa todo internamente
    public ConsoleUI() {
        // 1. Servicios base y repositorios
        ProductService productService = new ProductService();
        AccessoryService accessoryService = new AccessoryService(new AccessoryRepository());
        PersonService personService = new PersonService();
        PromotionService promotionService = new PromotionService(new PromotionRepository());

        // 2. Catálogo y coordinador de inventario
        List<Product> allItems = new ArrayList<>(productService.listAllProducts());
        allItems.addAll(accessoryService.listAllAccessories());

        SaleCatalog catalog = new SaleCatalog(
            new SaleRepository(), 
            personService.listAllPersons(), 
            allItems
        );
        
        List<InventoryService> services = new ArrayList<>();
        services.add((InventoryService) productService);
        services.add((InventoryService) accessoryService);
        InventoryCoordinator inventory = new InventoryCoordinator(services);

        // 3. Servicios dependientes
        WarrantyService warrantyService = new WarrantyService(
            new WarrantyRepository(), 
            catalog, 
            allItems
        );
        
        SaleService saleService = new SaleService(
            catalog, 
            inventory, 
            promotionService, 
            warrantyService
        );
        
        ReturnRepository returnRepository = new ReturnRepository();
        ReturnService returnService = new ReturnService(
            returnRepository, 
            catalog, 
            inventory, 
            warrantyService
        );

        // 4. Utilidades de consola y menús
        Scanner scanner = new Scanner(System.in);
        this.input = new ConsoleInput(scanner);
        ConsolePrinter printer = new ConsolePrinter();

        this.productMenu = new ProductMenu(productService, accessoryService, input, printer);
        this.accessoryMenu = new AccessoryMenu(accessoryService, input);
        this.personMenu = new PersonMenu(personService, input);
        this.saleMenu = new SaleMenu(saleService, personService, productService, accessoryService, input, printer);
        this.promotionMenu = new PromotionMenu(promotionService, input);
        this.warrantyMenu = new WarrantyMenu(warrantyService, input);
        this.returnMenu = new ReturnMenu(returnService, input);
    }

    /** Método de arranque llamado desde Main. */
    public void start() {
        showMainMenu();
    }

    /** Muestra el menú principal. */
    public void showMainMenu() {
        int option;
        do {
            System.out.println("\n========================================");
            System.out.println("    GAMEZONE UNICESAR - MENÚ PRINCIPAL  ");
            System.out.println("========================================");
            System.out.println("1. Productos");
            System.out.println("2. Accesorios");
            System.out.println("3. Personas");
            System.out.println("4. Ventas");
            System.out.println("5. Promociones");
            System.out.println("6. Garantías");
            System.out.println("7. Devoluciones");
            System.out.println("0. Salir");
            
            option = input.readInt("Seleccione una opción: ");
            
            switch (option) {
                case 1: productMenu.show(); break;
                case 2: accessoryMenu.show(); break;
                case 3: personMenu.show(); break;
                case 4: saleMenu.show(); break;
                case 5: promotionMenu.show(); break;
                case 6: warrantyMenu.show(); break;
                case 7: returnMenu.show(); break;
                case 0: System.out.println("¡Gracias por usar GameZone Unicesar!"); break;
                default: System.out.println("Opción inválida.");
            }
        } while (option != 0);
    }
}
