package com.gamezone.ui;

import com.gamezone.model.*;
import com.gamezone.service.*;
import java.util.Scanner;

/**
 * Interactive console menu interface.
 * 
 * @author Samuel Angulo
 */
public class ConsoleUI {
    private ProductService productService;
    private PersonService personService;
    private SaleService saleService;
    private Scanner scanner;

    public ConsoleUI() {
        this.productService = new ProductService();
        this.personService = new PersonService();
        this.saleService = new SaleService(personService, productService);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int option;
        do {
            System.out.println("\n========================================");
            System.out.println("   GAMEZONE UNICESAR 10 - MAIN MENU     ");
            System.out.println("========================================");
            System.out.println("1. List Products");
            System.out.println("2. Add VideoGame");
            System.out.println("3. Add Console");
            System.out.println("4. List Persons (Customers & Sellers)");
            System.out.println("5. Add Customer");
            System.out.println("6. Add Seller");
            System.out.println("7. List Sales");
            System.out.println("8. Register New Sale");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
            
            option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    listProducts();
                    break;
                case 2:
                    addVideoGame();
                    break;
                case 3:
                    addConsole();
                    break;
                case 4:
                    listPersons();
                    break;
                case 5:
                    addCustomer();
                    break;
                case 6:
                    addSeller();
                    break;
                case 7:
                    listSales();
                    break;
                case 8:
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

    private void listProducts() {
        System.out.println("\n--- PRODUCT INVENTORY ---");
        for (Product p : productService.listAllProducts()) {
            System.out.println(p.getDescription());
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

        System.out.print("Enter Product ID: "); String prodId = scanner.nextLine();
        Product product = productService.findById(prodId);
        if (product == null) {
            System.out.println("Product not found!");
            return;
        }

        System.out.print("Enter Quantity: "); int qty = scanner.nextInt();
        scanner.nextLine();

        boolean success = saleService.registerSale(saleId, customer, seller, product, qty);
        if (success) {
            System.out.println("Sale registered successfully!");
        }
    }
}