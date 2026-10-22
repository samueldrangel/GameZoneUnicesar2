# GameZoneUnicesar

## Description
GameZoneUnicesar is a Java-based console application developed as an integrated project for academic purposes at Universidad Popular del Cesar. It implements a layered architecture (Model, Persistence, Service, and UI) to manage the inventory, people, sales, promotional discounts, product returns, and warranty tracking of a video game store, with file-based persistence.

The system manages video games, consoles, and gaming accessories (controllers, cables, and memories), as well as customers, sellers, sales, dynamic promotions, returns, and warranties. Accessories can be sold together with video games and consoles in the same transaction, and the system can list which accessories are compatible with a given console.

## Features
- **Product management**: register video games and consoles, and list the available inventory with strict validation rules.
- **Accessory management**:
  - Register controllers (connection type and compatible consoles).
  - Register cables (length in meters and connector type).
  - Register memories (capacity in GB and memory type).
  - List all accessories, list by type, and query compatibility.
- **Person management**: register customers, and list customers and sellers. Sellers are preloaded in `data/persons.csv`.
- **Sales management**:
  - Register a sale selecting a customer, a seller, and one or more items with automated validation of stock and business rules.
  - Query complete sales history, purchase history by customer, and sales by seller.
- **Promotion management**:
  - Register percentage-based discounts, category-based discounts, and bulk purchase/wholesale volume discounts.
  - List active promotions and evaluate/apply the best discount automatically during sale registration.
- **Return and Refund management**:
  - Register returns for specific items belonging to a sale within the 30-day return policy limit.
  - Automatically restore inventory stock upon successful return processing.
  - Query returns by customer or sale and generate monthly financial net balance reports.
- **Warranty management**:
  - Automatically assign basic warranties to purchased consoles and optionally add extended warranties.
  - Query warranty details, list active warranties, and warranties expiring within a specified number of days.
- **Robust Exception & Validation Layer**:
  - Standardized custom exception handling via `PersistenceException` (wrapping low-level I/O errors) and `BusinessRuleException`.
  - Centralized validation rules delegated to dedicated validators (`ProductValidator`, `PersonValidator`, `SaleValidator`).
  - Differentiated error handling helper integrated into `ConsoleMenu` for safe user interaction and graceful error recovery.
- **Persistence**: all information is stored in CSV files, protected by custom exception context, and loaded automatically when the application starts.

## Architecture & Package Structure
The project follows a standard Maven layered structure (`com.gamezone`):
- **`com.gamezone.model`**: Domain classes (`Person`, `Product`, `Accessory`, `Promotion`, `Sale`, `Return`, `Warranty`, and their respective subclasses). Inheritance and polymorphism are applied through overridden methods.
- **`com.gamezone.persistence`**: File-based reading and writing of data (`ProductRepository`, `AccessoryRepository`, `PersonRepository`, `SaleRepository`, `PromotionRepository`, `ReturnRepository`, `WarrantyRepository`). Standard `IOException` handling is wrapped into custom `PersistenceException` to provide enhanced error context.
- **`com.gamezone.service`**: Business rules, validation delegation, and data coordination (`ProductService`, `AccessoryService`, `PersonService`, `SaleService`, `PromotionService`, `ReturnService`, `WarrantyService`).
- **`com.gamezone.validator`**: Specialized validation components enforcing business and data integrity rules (`ProductValidator`, `PersonValidator`, `SaleValidator`).
- **`com.gamezone.exceptions`**: Custom runtime exceptions (`PersistenceException`, `BusinessRuleException`).
- **`com.gamezone.ui`**: Command-line interactive menu interface (`ConsoleMenu`) featuring a unified error-handling helper routing user operations securely.
- **`com.gamezone`**: Application entry point (`Main`).

Dependencies between layers follow one direction: `ui → service → persistence → model`.

## Design & Exception Documentation
The `docs/` folder contains comprehensive documentation and architectural diagrams:
- `analysis.md`, `hierarchy-diagram.md`, `class-diagram.md`, `layers-diagram.md`
- **`exception-analysis.md`**: Detailed analysis on exception hierarchy, behavior preservation, and menu routing strategies.
- **`exception-class-diagram.md`**: Mermaid class diagram representing the exception and validation layers.

## Data Files
The `data/` folder contains CSV files used for persistence:
| File | Content |
| :--- | :--- |
| `data/persons.csv` | Customers and sellers (at least 3 sellers preloaded) |
| `data/products.csv` | Video games and consoles |
| `data/accessories.csv` | Accessories (controllers, cables, memories) |
| `data/sales.csv` | Sales history, totals, promotions, and extended warranty costs |
| `data/promotions.csv` | Registered discount promotions and active date ranges |
| `data/returns.csv` | Product returns, reasons, and refund amounts |
| `data/warranties.csv` | Basic and extended warranties with validity dates |

If a file does not exist, the system starts with an empty list. Run the application from the repository root so the relative `data/` path resolves correctly.

## Menu Options
### Main Menu
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
11. Register New Sale (Multi-item & Extended Warranty Option)
12. List Active Promotions
13. Register Percentage Discount
14. Register Category Discount
15. Register Bulk Purchase Discount
16. Gestión de devoluciones (Returns Submenu)
17. Gestión de garantías (Warranty Submenu)
18. Exit

### Returns Submenu (Option 16)
1. Registrar una nueva devolución
2. Consultar todas las devoluciones
3. Consultar devoluciones por cliente
4. Consultar devoluciones por venta
5. Consultar balance mensual
6. Volver

### Warranty Submenu (Option 17)
1. Consultar garantía de un producto en una venta
2. Listar todas las garantías
3. Listar garantías vigentes
4. Listar garantías próximas a vencer
5. Volver

User-facing messages in the console menu are fully localized in Spanish.

## Prerequisites
- Java Development Kit (JDK) 17 or higher.
- Apache Maven.
- Apache NetBeans IDE or any Java-compatible IDE.

## How to Run
1. Clone the repository and checkout the target branch (e.g., `feature/exception-handling`).
2. Open the project in Apache NetBeans as a Maven project.
3. Locate the `Main.java` file inside `com.gamezone`.
4. Run the file (Shift + F6 in NetBeans) to launch the interactive console system.

From the command line (repository root):
```bash
mvn clean compile
mvn exec:java