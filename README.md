# GameZoneUnicesar

## Description

GameZoneUnicesar is a Java-based console application developed as an integrated project for academic purposes at Universidad Popular del Cesar. It implements a layered architecture (Model, Persistence, Service, and UI) to manage the inventory, people, and sales of a video game store, with file-based persistence.

The system manages video games, consoles, and gaming accessories (controllers, cables, and memories), as well as customers, sellers, and sales. Accessories can be sold together with video games and consoles in the same transaction, and the system can list which accessories are compatible with a given console.

## Features

* **Product management:** register video games and consoles, and list the available inventory.
* **Accessory management:**
  * Register controllers (connection type and compatible consoles).
  * Register cables (length in meters and connector type).
  * Register memories (capacity in GB and memory type).
  * List all accessories.
  * List accessories by type.
  * Query accessories compatible with a specific console.
* **Person management:** register customers, and list customers and sellers. Sellers are preloaded in `data/persons.csv`.
* **Sales management:**
  * Register a sale selecting a customer, a seller, and one or more items (video games, consoles, or accessories).
  * Query the complete sales history.
  * Query the purchase history of a specific customer.
  * Query the sales attended by a specific seller.
* **Business rules:** a sale must contain at least one item, stock is validated for every item, inventory is updated automatically after a sale, and the total is the sum of the item prices.
* **Persistence:** all information is stored in CSV files and loaded automatically when the application starts. Changes are saved after each operation.

## Architecture & Package Structure

The project follows a standard Maven layered structure (`com.gamezone`):

* `com.gamezone.model`: Domain classes.
  * Abstract class `Person` with subclasses `Customer` and `Seller`.
  * Abstract class `Product` with subclasses `VideoGame`, `Console`, and `Accessory`.
  * Abstract class `Accessory` (extends `Product`) with subclasses `Controller`, `Cable`, and `Memory`. It keeps the list of compatible console IDs.
  * `Sale`, which holds a customer, a seller, and a list of items.
  * Inheritance and polymorphism are applied through the overridden `getDescription` method.
* `com.gamezone.persistence`: File-based reading and writing of data (`ProductRepository`, `AccessoryRepository`, `PersonRepository`, `SaleRepository`). `AccessoryRepository` uses a type discriminator column to rebuild the correct subclass when loading. `SaleRepository` stores the IDs of the sold items and rebuilds the references from the loaded lists.
* `com.gamezone.service`: Business rules, data lists, and stock validations (`ProductService`, `AccessoryService`, `PersonService`, `SaleService`). `SaleService` delegates inventory updates to `ProductService` or `AccessoryService` depending on the type of the item sold.
* `com.gamezone.ui`: Command-line interactive menu interface (`ConsoleMenu`).
* `com.gamezone`: Application entry point (`Main`), which creates the repositories, services, and menu, and injects the dependencies.

Dependencies between layers follow one direction: `ui → service → persistence → model`.

Design documents are in the `docs/` folder: `analysis.md`, `hierarchy-diagram.md`, `class-diagram.md`, `layers-diagram.md`, and the accessory module documents.

## Data Files

The `data/` folder contains the files used for persistence:

| File | Content |
|---|---|
| `data/persons.csv` | Customers and sellers (at least 3 sellers are preloaded) |
| `data/products.csv` | Video games and consoles |
| `data/accessories.csv` | Accessories (one controller, one cable, and one memory are preloaded) |
| `data/sales.csv` | Sales history |

If a file does not exist, the system starts with an empty list for that data. Run the application from the repository root so the relative `data/` path resolves correctly.

## Menu Options

**Main menu**

1. Product management
2. Accessory management
3. Person management
4. Sales management
0. Exit

**Product management:** register a video game, register a console, list all products.

**Accessory management:** register a controller, register a cable, register a memory, list all accessories, list accessories by type, query accessories compatible with a console.

**Person management:** register a customer, list customers, list sellers.

**Sales management:** register a sale, query the complete sales history, query the purchase history of a customer, query the sales attended by a seller.

User-facing messages in the menu are shown in Spanish.

## Prerequisites

* Java Development Kit (JDK) 17 or higher.
* Apache Maven.
* Apache NetBeans IDE or any Java-compatible IDE.

## How to Run

1. Clone the repository and checkout the target branch.
2. Open the project in Apache NetBeans as a Maven project.
3. Locate the `Main.java` file inside `com.gamezone`.
4. Run the file (Shift + F6 in NetBeans) to launch the interactive console system.

From the command line, from the repository root:

```bash
mvn clean compile
mvn exec:java
```

## Git Workflow

The team uses Git Flow with protected `main` and `develop` branches, `feature/*` branches, Pull Requests with cross review, and atomic commits following Conventional Commits.