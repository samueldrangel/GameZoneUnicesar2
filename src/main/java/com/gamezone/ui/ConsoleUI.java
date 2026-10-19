package com.gamezone.ui;

import com.gamezone.model.*;
import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interactive console menu interface updated to match AccessoryService repository dependency,
 * specific accessory registration methods, and multi-item SaleService logic.
 * 
 * @author Samuel Angulo
 */
public class ConsoleUI {
    private ProductService productService;
    private AccessoryService accessoryService;
    private PersonService personService;
    private SaleService saleService;
    private Scanner scanner;

    public ConsoleUI() {
        this.productService = new ProductService();
        this.accessoryService = new AccessoryService(new AccessoryRepository());
        this.personService = new PersonService();
        this.saleService = new SaleService(productService, accessoryService, personService, new SaleRepository());
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int option;
        do {
            System.out.println("\n========================================");
            System.out.println("    GAMEZONE UNICESAR - MAIN MENU       ");
            System.out.println("========================================");
            System.out.println("1. List All Products & Accessories");
            System.out.println("2. Add VideoGame");
            System.out.println("3. Add Console");
            System.out.println("4. Register Controller (Accessory)");
            System.out.println("5. Register Cable (Accessory)");
            System.out.println("6. Register Memory (Accessory)");
            System.out.println("7. List Persons (Customers & Sellers)");
            System.out.println("8. Add Customer");
            System.out.println("9. Add Seller");
            System.out.println("10. List Sales");
            System.out.println("11. Register New Sale (Multi-item)");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
            
            option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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
                case 0:
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (option != 0);
    }

    private void listProductsAndAccessories() {
        System.out.println("\n--- PRODUCT & ACCESSORY INVENTORY ---");
        System.out.println("Products:");
        for (Product p : productService.listAllProducts()) {
            System.out.println(" - " + p.getDescription());
        }
        System.out.println("Accessories:");
        for (Accessory a : accessoryService.listAllAccessories()) {
            System.out.println(" - " + a.getDescription());
        }
    }

    private void addVideoGame() {
        System.out.print("Enter ID: "); String id = scanner.nextLine();
        System.out.print("Enter Title: "); String title = scanner.nextLine();
        System.out.print("Enter Price: "); double price = scanner.nextDouble();
        System.out.print("Enter Stock: "); int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Platform: "); String platform = scanner.nextLine();
        System.out.print("Enter Genre: "); String genre = scanner.nextLine();
        System.out.print("Enter Age Rating: "); String rating = scanner.nextLine();

        VideoGame vg = new VideoGame(id, title, price, stock, platform, genre, rating);
        productService.addProduct(vg);
        System.out.println("VideoGame added successfully!");
    }

    private void addConsole() {
        System.out.print("Enter ID: "); String id = scanner.nextLine();
        System.out.print("Enter Title: "); String title = scanner.nextLine();
        System.out.print("Enter Price: "); double price = scanner.nextDouble();
        System.out.print("Enter Stock: "); int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Brand: "); String brand = scanner.nextLine();
        System.out.print("Enter Model: "); String model = scanner.nextLine();
        System.out.print("Enter Generation: "); String generation = scanner.nextLine();

        com.gamezone.model.Console c = new com.gamezone.model.Console(id, title, price, stock, brand, model, generation);
        productService.addProduct(c);
        System.out.println("Console added successfully!");
    }

    private void registerController() {
        System.out.print("Enter ID: "); String id = scanner.nextLine();
        System.out.print("Enter Title: "); String title = scanner.nextLine();
        System.out.print("Enter Price: "); double price = scanner.nextDouble();
        System.out.print("Enter Stock: "); int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Connection Type (Wireless/Wired): "); String connType = scanner.nextLine();

        List<String> compatibleConsoles = promptCompatibleConsoles();

        accessoryService.registerController(id, title, price, stock, compatibleConsoles, connType);
        System.out.println("Controller registered successfully!");
    }

    private void registerCable() {
        System.out.print("Enter ID: "); String id = scanner.nextLine();
        System.out.print("Enter Title: "); String title = scanner.nextLine();
        System.out.print("Enter Price: "); double price = scanner.nextDouble();
        System.out.print("Enter Stock: "); int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Length (Meters): "); double length = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Connector Type: "); String connector = scanner.nextLine();

        List<String> compatibleConsoles = promptCompatibleConsoles();

        accessoryService.registerCable(id, title, price, stock, compatibleConsoles, length, connector);
        System.out.println("Cable registered successfully!");
    }

    private void registerMemory() {
        System.out.print("Enter ID: "); String id = scanner.nextLine();
        System.out.print("Enter Title: "); String title = scanner.nextLine();
        System.out.print("Enter Price: "); double price = scanner.nextDouble();
        System.out.print("Enter Stock: "); int stock = scanner.nextInt();
        System.out.print("Enter Capacity (GB): "); int capacity = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Memory Type: "); String memoryType = scanner.nextLine();

        List<String> compatibleConsoles = promptCompatibleConsoles();

        accessoryService.registerMemory(id, title, price, stock, compatibleConsoles, capacity, memoryType);
        System.out.println("Memory registered successfully!");
    }

    private List<String> promptCompatibleConsoles() {
        List<String> compatibleConsoles = new ArrayList<>();
        System.out.print("Add compatible console ID? (y/n): ");
        String opt = scanner.nextLine();
        while (opt.equalsIgnoreCase("y")) {
            System.out.print("Enter Console ID: ");
            compatibleConsoles.add(scanner.nextLine());
            System.out.print("Add another compatible console ID? (y/n): ");
            opt = scanner.nextLine();
        }
        return compatibleConsoles;
    }

    private void listPersons() {
        System.out.println("\n--- PERSONS DIRECTORY ---");
        for (Person p : personService.listAllPersons()) {
            System.out.println(p.getDetails());
        }
    }

    private void addCustomer() {
        System.out.print("Enter ID: "); String id = scanner.nextLine();
        System.out.print("Enter Name: "); String name = scanner.nextLine();
        System.out.print("Enter Phone: "); String phone = scanner.nextLine();
        System.out.print("Enter Email: "); String email = scanner.nextLine();

        Customer customer = new Customer(id, name, phone, email);
        personService.addPerson(customer);
        System.out.println("Customer added successfully!");
    }

    private void addSeller() {
        System.out.print("Enter ID: "); String id = scanner.nextLine();
        System.out.print("Enter Name: "); String name = scanner.nextLine();
        System.out.print("Enter Phone: "); String phone = scanner.nextLine();
        System.out.print("Enter Employee Code: "); String code = scanner.nextLine();
        System.out.print("Enter Work Shift: "); String shift = scanner.nextLine();

        Seller seller = new Seller(id, name, phone, code, shift);
        personService.addPerson(seller);
        System.out.println("Seller added successfully!");
    }

    private void listSales() {
        System.out.println("\n--- SALES TRANSACTIONS ---");
        for (Sale s : saleService.listAllSales()) {
            System.out.println(s.getSaleSummary());
        }
    }

    private void registerSale() {
        System.out.print("Enter Sale ID: "); String saleId = scanner.nextLine();
        
        System.out.print("Enter Customer ID: "); String custId = scanner.nextLine();
        Customer customer = personService.findCustomerById(custId);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }

        System.out.print("Enter Seller ID: "); String sellId = scanner.nextLine();
        Seller seller = personService.findSellerById(sellId);
        if (seller == null) {
            System.out.println("Seller not found!");
            return;
        }

        System.out.print("Enter Product or Accessory ID: "); String itemId = scanner.nextLine();
        Product product = productService.findById(itemId);
        if (product == null) {
            product = accessoryService.findById(itemId);
        }

        if (product == null) {
            System.out.println("Product or Accessory not found!");
            return;
        }

        System.out.print("Enter Quantity: "); int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        boolean success = saleService.registerSale(saleId, customer, seller, product, quantity);
        if (success) {
            System.out.println("Sale registered successfully!");
        } else {
            System.out.println("Failed to register sale. Check stock availability.");
        }
    }
}