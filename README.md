# GameZoneUnicesar

## Description

GameZoneUnicesar is a Java-based console application developed as an integrated project for academic purposes at Universidad Popular del Cesar. It implements a layered architecture (Model, Persistence, Service, and UI) to manage the inventory, people, sales, and promotional discounts of a video game store, with file-based persistence.

The system manages video games, consoles, and gaming accessories (controllers, cables, and memories), as well as customers, sellers, sales, and dynamic promotions. Accessories can be sold together with video games and consoles in the same transaction, and the system can list which accessories are compatible with a given console.

## Features

- **Product management:** register video games and consoles, and list the available inventory.
- **Accessory management:**
  * Register controllers (connection type and compatible consoles).
  * Register cables (length in meters and connector type).
  * Register memories (capacity in GB and memory type).
  * List all accessories.
  * List accessories by type.
  * Query accessories compatible with a specific console.
- **Person management:** register customers, and list customers and sellers. Sellers are preloaded in `data/persons.csv`.
- **Sales management:**
  * Register a sale selecting a customer, a seller, and one or more items (video games, consoles, or accessories).
  * Query the complete sales history.
  * Query the purchase history of a specific customer.
  * Query the sales attended by a specific seller.
- **Promotion management:**
  * Register percentage-based discounts (`PercentageDiscount`).
  * Register category-based discounts for videogames or consoles (`CategoryDiscount`).
  * Register bulk purchase / wholesale volume discounts (`BulkPurchaseDiscount`).
  * List active promotions and apply the best discount automatically during sale registration.
- **Business rules:** a sale must contain at least one item, stock is validated for every item, inventory is updated automatically after a sale, and the total is calculated as the sum of item prices minus applicable promotional discounts.
- **Persistence:** all information is stored in CSV files and loaded automatically when the application starts. Changes are saved after each operation.

## Architecture & Package Structure

The project follows a standard Maven layered structure (`com.gamezone`):

- `com.gamezone.model`: Domain classes.
  * Abstract class `Person` with subclasses `Customer` and `Seller`.
  * Abstract class `Product` with subclasses `VideoGame`, `Console`, and `Accessory`.
  * Abstract class `Accessory` (extends `Product`) with subclasses `Controller`, `Cable`, and `Memory`. It keeps the list of compatible console IDs.
  * Abstract class `Promotion` with subclasses `PercentageDiscount`, `CategoryDiscount`, and `BulkPurchaseDiscount`.
  * `Sale`, which holds a customer, a seller, a list of items, the applied promotion name, and the discount amount.
  * Inheritance and polymorphism are applied through overridden methods like `getDescription` and `calculateDiscount`.
- `com.gamezone.persistence`: File-based reading and writing of data (`ProductRepository`, `AccessoryRepository`, `PersonRepository`, `SaleRepository`, `PromotionRepository`). `AccessoryRepository` uses a type discriminator column to rebuild the correct subclass when loading. `SaleRepository` stores the IDs of the sold items and rebuilds references from loaded lists.
- `com.gamezone.service`: Business rules, data lists, and stock/promotion validations (`ProductService`, `AccessoryService`, `PersonService`, `SaleService`, `PromotionService`). `SaleService` delegates inventory updates to `ProductService` or `AccessoryService`, and evaluates the best discount via `PromotionService`.
- `com.gamezone.ui`: Command-line interactive menu interface (`ConsoleUI`).
- `com.gamezone`: Application entry point (`Main`), which creates the repositories, services, and menu, and injects the dependencies.

Dependencies between layers follow one direction: `ui → service → persistence → model`.

Design documents are in the `docs/` folder: `analysis.md`, `hierarchy-diagram.md`, `class-diagram.md`, `layers-diagram.md`, and module-specific guides.

## Data Files

The `data/` folder contains the files used for persistence:

| File | Content |
|---|---|
| `data/persons.csv` | Customers and sellers (at least 3 sellers are preloaded) |
| `data/products.csv` | Video games and consoles |
| `data/accessories.csv` | Accessories (one controller, one cable, and one memory are preloaded) |
| `data/sales.csv` | Sales history, including totals, applied promotion names, and discount amounts |
| `data/promotions.csv` | Registered discount promotions and their active date ranges |

If a file does not exist, the system starts with an empty list for that data. Run the application from the repository root so the relative `data/` path resolves correctly.

## Menu Options

**Main menu**

1. List all products and accessories
2. Add VideoGame
3. Add Console
4. Register Controller (Accessory)
5. Register Cable (Accessory)
6. Register Memory (Accessory)
7. List Persons (Customers & Sellers)
8. Add Customer
9. Add Seller
10. List Sales
11. Register New Sale (Multi-item)
12. List Active Promotions
13. Register Percentage Discount
14. Register Category Discount
15. Register Bulk Purchase Discount
0. Exit

User-facing messages in the console menu are fully localized in Spanish.

## Prerequisites

- Java Development Kit (JDK) 17 or higher.
- Apache Maven.
- Apache NetBeans IDE or any Java-compatible IDE.

## How to Run

1. Clone the repository and checkout the target branch.
2. Open the project in Apache NetBeans as a Maven project.
3. Locate the `Main.java` file inside `com.gamezone`.
4. Run the file (Shift + F6 in NetBeans) to launch the interactive console system.

From the command line, from the repository root:

```bash
mvn clean compile
mvn exec:java