Here is the English Markdown version ready to save as grasp_audit_samuel_rangel.md:

# GRASP ARCHITECTURE AUDIT AND COMMIT TRACEABILITY REPORT

**Project:** GameZone Unicesar – Commercial Management and Inventory System

**Organization:** Popular University of Cesar (UPC) – Faculty of Engineering and Technology

**Base Repository:** https://github.com/samueldrangel/GameZoneUnicesar2

**Audited State:** `develop` branch @ Commit `237669c74114f207ebd8e2dba84ecad0dcb6ebd7` (Merge PR #31)

**Date:** October 22, 2026

---

# 1. Audit Scope and Technical Overview

1. **Audited Developer:** Samuel David Rangel Martínez (`samueldrangel`) – Developer 2 & Technical Leader

2. **Ownership Criteria:**
   - Sales Core (`Sale`, `SaleDetail`, `SaleService`)
   - Global Persistence (`SaleRepository`, `PersonRepository`)
   - User Interface and Orchestration (`Menu`, `SubMenu`, `Main`)
   - Warranty Integration (`WarrantyService`)

---

## 1.1 Module-Class Matrix by Commit History

The following matrix validates the ownership and responsibilities of the Technical Leader according to Git Blame history and Pull Requests.

| Module / Requirement | Classes / Components Covered | Key Commit Hashes | Associated Commit Message |
|----------------------|-----------------------------|-------------------|---------------------------|
| **Sales Core (Workshop 1)** | `Sale`, `SaleDetail`, `SaleService`, `SaleRepository` | `78861837`, `64f51979`, `204cbcc4`, `84989378` | `refactor: unify sale registration flow and extend sale persistence` |
| **People Management (Workshop 1)** | `Person`, `Customer/Client`, `Seller`, `PersonService`, `PersonRepository` | `82b20d94`, `43ecd49c`, `d512715a` | `feat: implement PersonService and file persistence` |
| **Console UI (Req. 5)** | `ConsoleUI`, `Menu`, `SubMenu`, `Main` | `2df6f631`, `34b46212`, `71fe50cc` | `refactor: route menu operations through the exception handler` |
| **Warranties and Returns (Req. 3 & 4)** | Return cancellation and financial impact integration | `0db6bb13`, `126e84b6`, `02b81b97` | `feat: integrate WarrantyService into ReturnService and cancel warranties` |
| **Exceptions (Req. 6)** | Validation delegation and global exception handling | `a062a1b8`, `a99e48e2`, `16844295` | `refactor: delegate validations to validators and wrap IOException` |

---

# 2. Detailed GRASP Evaluation Results

| GRASP Pattern | Verdict | Summary Justification |
|--------------|----------|----------------------|
| Information Expert | MODERATE VIOLATION | Manual iterative catalog searches using streams inside SaleService. |
| Creator | MODERATE VIOLATION | SubMenu directly instantiates SaleDetail and calculates unit prices. |
| Controller | COMPLIANT | Menu and SubMenu correctly channel requests to the domain layer and manage user interaction. |
| Low Coupling | CRITICAL VIOLATION | Services are tightly coupled to concrete CSV persistence classes and obsolete parameters. |
| High Cohesion | CRITICAL VIOLATION | SubMenu is a monolithic class (>600 LOC) and processSale performs eight responsibilities. |
| Polymorphism | CRITICAL VIOLATION | Repeated use of instanceof to update stock and assign warranties. |
| Pure Fabrication | MODERATE VIOLATION | Console input logic duplicated between Menu and SubMenu. |
| Indirection | CRITICAL VIOLATION | Direct connections between business services and storage implementations. |
| Protected Variations | CRITICAL VIOLATION | Service layer is fragile against changes in technology or product types. |

---

# 3. Technical Violations and Refactoring Plan

## LEAD-V-01: View Layer Monolith with Multiple Responsibilities (`SubMenu`)

- **Violated GRASP Pattern:** High Cohesion
- **Related Commits:** `2df6f631`, `84989378`
- **Location:** `SubMenu.java` (627 lines of code)

### Evidence

```java
public void showProductSubMenu() { ... }
public void showAccessorySubMenu() { ... }
public void showPersonSubMenu() { ... }
public void showPromotionSubMenu() { ... }
public void showReturnSubMenu() { ... }
public void showWarrantySubMenu() { ... }
public void processSaleForm() { ... }
```

### Explanation

The class is responsible for presentation, input collection, validation, and navigation across seven system modules, violating the High Cohesion principle.

### Proposed Solution

Split `SubMenu` into specialized UI controllers:

- `ProductMenu`
- `SaleMenu`
- `PersonMenu`
- `ReturnMenu`
- `WarrantyMenu`

---

## LEAD-V-02: Manual Type Evaluation for Inventory and Warranties

- **Violated GRASP Pattern:** Polymorphism / Protected Variations
- **Related Commits:** `84781677`, `84989378`
- **Location:** `SaleService.java`, lines 108–112 and 154–158

### Evidence

```java
if (product instanceof Accessory) {
    accessoryService.updateStock(product.getId(), quantity);
} else {
    productService.updateStock(product.getId(), quantity);
}

if (product instanceof Console) {
    warrantyService.assignBasicWarranty(sale, (Console) product);
}
```

### Explanation

`SaleService` explicitly checks concrete types using `instanceof` to decide inventory update and warranty assignment behavior.

### Proposed Solution

Introduce an `InventoryCoordinator` facade and implement polymorphic behavior:

```java
product.supportsWarranty();
```

---

## LEAD-V-03: Direct Coupling to Concrete Persistence Classes

- **Violated GRASP Pattern:** Indirection / Low Coupling
- **Related Commits:** `78861837`, `82b20d94`

### Evidence

```java
public class SaleService {

    private final SaleRepository repository;

    public SaleService(SaleRepository repository) {
        this.repository = repository;
    }
}
```

### Explanation

Business services depend directly on CSV repositories, limiting unit testing and future database migrations.

### Proposed Solution

Introduce abstraction interfaces:

- `SaleStorage`
- `PersonStorage`

and inject them into domain services.

---

## LEAD-V-04: Monolithic Sales Processing Method

- **Violated GRASP Pattern:** High Cohesion
- **Related Commit:** `84989378`
- **Location:** `SaleService.processSale`

### Explanation

The method performs all of the following:

1. Customer lookup
2. Input validation
3. Stock verification
4. Promotion calculation
5. Warranty handling
6. Sale construction
7. Persistence
8. History updates

### Proposed Solution

Delegate concerns to specialized collaborators:

- `PromotionService`
- `WarrantyService`
- `InventoryCoordinator`

---

## LEAD-V-05: External Catalog Searches Using Streams

- **Violated GRASP Pattern:** Information Expert
- **Related Commit:** `78861837`

### Evidence

```java
return productService.listAll().stream()
    .filter(p -> p != null && productId.equals(p.getId()))
    .findFirst()
    .orElseGet(() ->
        accessoryService.listAllAccessories()
            .stream() ...
    );
```

### Explanation

`SaleService` retrieves entire collections and performs manual searches instead of delegating to the responsible service.

### Proposed Solution

Delegate directly:

```java
productService.findById(id);
```

---

## LEAD-V-06: Duplicated Console Input Logic

- **Violated GRASP Pattern:** Pure Fabrication
- **Related Commits:** `17f724d`, `2df6f63`

### Evidence

```java
private int readInt() {
    try {
        return Integer.parseInt(scanner.nextLine().trim());
    } catch (NumberFormatException e) {
        return -1;
    }
}
```

### Proposed Solution

Create a utility class:

```java
com.gamezone.ui.ConsoleInput
```

to centralize safe input parsing and validation.

---

## LEAD-V-07: Unnecessary Dependency Passing

- **Violated GRASP Pattern:** Low Coupling
- **Related Commit:** `2df6f631`

### Evidence

```java
public Menu(
    ProductService productService,
    AccessoryService accessoryService,
    PersonService personService,
    PromotionService promotionService,
    SaleService saleService,
    ReturnService returnService,
    WarrantyService warrantyService) {

    this.subMenu = new SubMenu(
        productService,
        accessoryService,
        ...
    );
}
```

### Explanation

`Menu` receives seven services exclusively to forward them to `SubMenu`.

### Proposed Solution

Instantiate and inject individual menu components directly from `Main`.

---

## LEAD-V-08: Obsolete Unused Parameter in Sale Persistence

- **Violated GRASP Pattern:** Low Coupling
- **Related Commit:** `64f51979`

### Evidence

```java
public List<Sale> loadAll(
    List<Customer> clients,
    List<Seller> sellers,
    List<Product> products,
    List<Accessory> accessories,
    List<? extends Promotion> promotions) {

    // promotions is never used
}
```

### Proposed Solution

Remove the `promotions` parameter and update all callers in:

- `SaleService`
- `WarrantyRepository`

---

# 4. Refactoring and Non-Regression Protocol

To ensure that refactoring activities do not alter system behavior, the following integration test suite must be executed after every Pull Request.

```bash
# Non-regression validation script
./docs/regression/run.sh
```

## Reference Validation Figures (Test Sale)

| Concept | Value |
|----------|----------|
| Gross Subtotal | $2,350,000 |
| Promotional Discount (15%) | -$352,500 |
| Extended Warranty Added | +$200,000 |
| **Expected Transaction Total** | **$2,197,500** |

---

# Overall Conclusion

The module owned by **Samuel David Rangel Martínez** contains a critical portion of the system architecture, particularly regarding sales management, persistence, warranty integration, and user interface orchestration.

While the implementation adequately satisfies the **Controller** pattern, significant violations were identified in **High Cohesion**, **Low Coupling**, **Polymorphism**, **Indirection**, and **Protected Variations**. These architectural weaknesses increase maintenance costs, reduce extensibility, and make the system more resistant to future technological changes.

Refactoring efforts should prioritize:

1. Decomposing `SubMenu` into specialized UI components.
2. Replacing concrete persistence dependencies with abstractions.
3. Eliminating `instanceof` structures through polymorphic designs.
4. Delegating sales-processing responsibilities to dedicated coordinators and services.
5. Improving testability through dependency inversion and interface-based architecture.

These changes would substantially improve compliance with GRASP principles and increase the long-term maintainability of the GameZone Unicesar system.