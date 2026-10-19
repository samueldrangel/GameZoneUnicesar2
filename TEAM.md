# Team Organization and Activity Plan - GameZone Unicesar

## Team Information
- **Course:** Programación III
- **Institution:** Universidad Popular del Cesar

## Team Members and Roles

### Member A: Technical Leader & Sales Module Developer
- **Name:** Samuel David Rangel Martinez
- **Student ID:** samueldrangel
- **Assigned Role:** Technical Leader & Sales Module Developer & Reporting
- **Assigned Feature Branch:** `feature/sale-module`

### Member B: Core Modules & Interface Developer
- **Name:** Samuel Angulo Meza
- **Student ID:** sangulom
- **Assigned Role:** Products, Persons, Exceptions & Interface Developer
- **Assigned Feature Branch:** `feature/product-person-ui-module`

---

## Class Distribution by Layer

| Layer | Member A (Technical Leader) | Member B (Developer) |
| :--- | :--- | :--- |
| **Model** | `Sale.java` | `Product.java` (Abstract), `VideoGame.java`, `Console.java`, `Person.java` (Abstract), `Customer.java`, `Seller.java` |
| **Persistence** | `SaleRepository.java` | `ProductRepository.java`, `PersonRepository.java` |
| **Service** | `SaleService.java`, `ReportService.java` | `ProductService.java`, `PersonService.java` |
| **Exceptions** | - | `ProductNotFoundException.java`, `InsufficientStockException.java`, `InvalidSaleException.java` |
| **UI & Execution**| - | `ConsoleUI.java`, `Main.java` |

---

## Activity Commitments

### Member A (Technical Leader)
1. **[Leader Act. 1]** Create the initial GitHub project repository with essential configuration (`README.md`, `.gitignore`, license).
2. **[Leader Act. 2]** Configure project branches (`main` and `develop`) and set up branch protection rules.
3. **[Leader Act. 3]** Configure the Maven project structure (`pom.xml`) and set up the package hierarchy for all layers.
4. **[Leader Act. 4]** Draft and maintain the `TEAM.md` documentation detailing roles, classes, and activities.
5. **[Leader Act. 5]** Implement the `Sale` domain class with attributes, constructors, getters, and setters.
6. **[Leader Act. 6]** Implement the total price calculation method in the `Sale` domain class.
7. **[Leader Act. 7]** Implement the `SaleRepository` class for file-based persistence of completed sales.
8. **[Leader Act. 8]** Implement `SaleService` including validation rules (minimum one product, stock verification, and inventory updates).
9. **[Leader Act. 8.1]** Implement `ReportService` to calculate total revenue and sales statistics.
10. **[Leader Act. 12]** Review and merge incoming Pull Requests from Member B into the `develop` integration branch.
11. **[Leader Act. 13]** Draft the final `README.md` with instructions for compilation, setup, and execution.

### Member B (Developer)
1. **[Product Act. 1-3]** Create feature branch and implement abstract class `Product` with common attributes, getters/setters, and abstract description method.
2. **[Product Act. 4-5]** Implement derived classes `VideoGame` and `Console` with specific attributes and overridden description methods.
3. **[Product Act. 6]** Implement `ProductRepository` for saving and retrieving products from files.
4. **