# Team Organization and Activity Plan - GameZone Unicesar

## Team Information

- **Course:** Programación III
- **Institution:** Universidad Popular del Cesar

---

# Team Members and Roles

## Member A: Technical Leader, Sales and Architecture Developer

- **Name:** Samuel David Rangel Martinez
- **Student ID:** samueldrangel
- **Assigned Role:** Technical Leader, Sales Module Developer, Architecture & Reporting
- **Assigned Feature Branch:** `feature/sale-module`

## Member B: Product, Inventory and Interface Developer

- **Name:** Samuel Angulo Meza
- **Student ID:** sangulom
- **Assigned Role:** Products, Inventory, Accessories & Interface Developer
- **Assigned Feature Branch:** `feature/product-person-ui-module`

---

# Class Distribution by Layer

| Layer | Member A (Technical Leader) | Member B (Developer) |
|---------|---------|---------|
| **Model** | `Sale`, `SaleItem` | `Product` (Abstract), `VideoGame`, `Console`, `Accessory` |
| **Persistence** | `SaleRepository`, `SaleCsvMapper` | `ProductRepository`, `AccessoryRepository`, `ProductCsvMapper`, `AccessoryCsvMapper` |
| **Service** | `SaleService`, `SaleCatalog`, `InventoryCoordinator`, `ReportService` | `ProductService`, `AccessoryService`, `InventoryService` |
| **Interfaces / Contracts** | Architecture coordination and integration | `InventoryService` implementation |
| **UI** | Integration review and architecture supervision | `ConsoleUI`, `ProductMenu`, `AccessoryMenu`, `ConsoleInput`, `ConsolePrinter`, `Main` |
| **Documentation** | README, TEAM, Audit Reports, AI Logs, Diagrams | Technical support and implementation documentation |

---

# Activity Commitments

## Member A (Technical Leader)

1. **[Leader Act. 1]** Create the GitHub repository with the initial project configuration (`README.md`, `.gitignore`, license).

2. **[Leader Act. 2]** Configure repository branches (`main`, `develop`) and define collaboration standards.

3. **[Leader Act. 3]** Configure the Maven project structure (`pom.xml`) and define the package hierarchy.

4. **[Leader Act. 4]** Maintain project documentation (`TEAM.md`, audit reports, diagrams, and AI logs).

5. **[Leader Act. 5]** Implement the `Sale` domain model and related sales entities.

6. **[Leader Act. 6]** Implement sales calculations and transaction processing logic.

7. **[Leader Act. 7]** Implement `SaleRepository` and sales persistence mechanisms.

8. **[Leader Act. 8]** Develop `SaleService` and sales validation rules.

9. **[Leader Act. 9]** Develop `ReportService` to generate revenue and sales statistics.

10. **[Leader Act. 10]** Lead the GRASP audit process and coordinate the correction strategy.

11. **[Leader Act. 11]** Implement architectural refactorings including `SaleCatalog` and `InventoryCoordinator`.

12. **[Leader Act. 12]** Review and integrate pull requests from team members.

13. **[Leader Act. 13]** Prepare the final `README.md` and project delivery documentation.

14. **[Leader Act. 14]** Produce and maintain UML and Mermaid diagrams before and after refactoring.

---

## Member B (Developer)

1. **[Dev Act. 1]** Implement the abstract `Product` class and common product behavior.

2. **[Dev Act. 2]** Implement `VideoGame` and `Console` subclasses.

3. **[Dev Act. 3]** Implement the `Accessory` domain model.

4. **[Dev Act. 4]** Implement `ProductRepository` and `AccessoryRepository`.

5. **[Dev Act. 5]** Implement CSV mappers and persistence support classes.

6. **[Dev Act. 6]** Implement `ProductService` and inventory-related business rules.

7. **[Dev Act. 7]** Implement `AccessoryService` and accessory management operations.

8. **[Dev Act. 8]** Implement the `InventoryService` contract and related inventory behaviors.

9. **[Dev Act. 9]** Develop the console application interface (`ConsoleUI`).

10. **[Dev Act. 10]** Refactor the UI into specialized menus (`ProductMenu`, `AccessoryMenu`).

11. **[Dev Act. 11]** Implement input/output utility components (`ConsoleInput`, `ConsolePrinter`).

12. **[Dev Act. 12]** Integrate inventory services with sales and reporting workflows.

13. **[Dev Act. 13]** Support GRASP correction implementation and validation activities.

14. **[Dev Act. 14]** Create and execute functional tests for products, accessories, and inventory operations.

---

# Collaboration Rules

- All development must be performed through feature branches.
- Pull Requests must be reviewed before merging into `develop`.
- The Technical Leader is responsible for architecture consistency and repository integration.
- Both members are responsible for validating functionality before integration.
- Documentation must be updated whenever architectural or functional changes are introduced.
- GRASP audit artifacts, diagrams, and AI usage logs must be maintained throughout the project lifecycle.