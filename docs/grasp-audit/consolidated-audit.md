# CONSOLIDATED GRASP AUDIT REPORT

**Project:** GameZone Unicesar – Commercial Management and Inventory System  
**Organization:** Popular University of Cesar (UPC) – Faculty of Engineering and Technology  
**Repository:** https://github.com/samueldrangel/GameZoneUnicesar2  
**Branch:** `develop`  
**Audited Commit:** `237669c74114f207ebd8e2dba84ecad0dcb6ebd7` (Merge PR #31)  
**Date:** October 22, 2026  
**Consolidated By:** Samuel David Rangel Martínez (`samueldrangel`) – Technical Leader / Developer 2

---

# 1. Audit Scope

This document consolidates all GRASP audits performed by the project team. It serves as the official reference for Phase 2 corrections and supersedes the individual audit reports.

The consolidation process included:

- Coverage verification of all audited classes.
- Validation of supporting evidence.
- Consolidation of duplicated findings.
- Identification of boundary findings affecting multiple modules.
- Assignment of correction responsibilities.
- Issuance of final team verdicts.

---

# 2. Coverage Verification

## Coverage Result

✅ COMPLETE COVERAGE VERIFIED

All classes owned by the two team members were audited exactly once.

No class was omitted and no class was duplicated.

---

## Coverage Matrix

| Package | Classes | Auditor |
|----------|----------|----------|
| model | Product, VideoGame, Console | Samuel Angulo (Developer 1) |
| model | Accessory, Controller, Cable, Memory, ConsoleCompatible | Samuel Angulo |
| model | Promotion, PercentageDiscount, CategoryDiscount, BulkPurchaseDiscount | Samuel Angulo |
| model | Return | Samuel Angulo |
| model | Warranty, BasicWarranty, ExtendedWarranty | Samuel Angulo |
| model | Person, Client, Seller | Samuel David Rangel Martínez |
| model | Sale, SaleDetail | Samuel David Rangel Martínez |
| validation | ProductValidator | Samuel Angulo |
| validation | PersonValidator | Samuel David Rangel Martínez |
| validation | AccessoryValidator, PromotionValidator, WarrantyValidator, SaleValidator | Samuel David Rangel Martínez |
| persistence | ProductRepository | Samuel Angulo |
| persistence | PersonRepository | Samuel David Rangel Martínez |
| persistence | SaleRepository | Samuel David Rangel Martínez |
| persistence | AccessoryRepository | Samuel David Rangel Martínez |
| persistence | PromotionRepository | Samuel David Rangel Martínez |
| persistence | ReturnRepository | Samuel David Rangel Martínez |
| persistence | WarrantyRepository | Samuel David Rangel Martínez |
| service | ProductService | Samuel Angulo |
| service | PersonService | Samuel David Rangel Martínez |
| service | SaleService | Samuel David Rangel Martínez |
| service | AccessoryService | Samuel David Rangel Martínez |
| service | PromotionService | Samuel David Rangel Martínez |
| service | ReturnService | Samuel David Rangel Martínez |
| service | WarrantyService | Samuel David Rangel Martínez |
| ui | Menu, SubMenu | Samuel David Rangel Martínez |
| root | Main | Samuel David Rangel Martínez |
| exceptions | GameZoneException, BusinessRuleException, InvalidDataException, ResourceNotFoundException, PersistenceException | Shared Audit |

---

# 3. Evidence Validation

All accepted findings contain:

- Clear code-level evidence.
- Exact class or method location.
- Pattern classification.
- Architectural justification.
- Refactoring proposal.

The Technical Leader reviewed all findings for consistency and removed duplicated reports describing the same architectural issue.

---

# 4. Final Team Verdict by GRASP Pattern

| # | GRASP Pattern | Final Verdict |
|---|--------------|--------------|
| 1 | Information Expert | Violation |
| 2 | Creator | Violation |
| 3 | Controller | Compliant |
| 4 | Low Coupling | Violation |
| 5 | High Cohesion | Violation |
| 6 | Polymorphism | Violation |
| 7 | Pure Fabrication | Violation |
| 8 | Indirection | Violation |
| 9 | Protected Variations | Violation |

---

# 5. Definitive Violations List

---

## V-01 — Catalog Lookup Performed by SaleService

**Pattern:** Information Expert

**Evidence**

```java
productService.listAll().stream()
...
accessoryService.listAllAccessories().stream()
```

**Location**

`SaleService.findProductOrAccessoryById()`

**Explanation**

SaleService performs catalog searches using complete catalog collections rather than delegating the responsibility to the services that own the data.

**Consequence**

Search logic becomes duplicated and tightly coupled to catalog implementation details.

**Agreed Solution**

Introduce direct lookup methods:

```java
ProductService.findById()
AccessoryService.findById()
```

**Origin**

Leader Audit – LEAD-V-05

**Responsible**

Samuel David Rangel Martínez

---

## V-02 — Total Units Calculated Outside Sale

**Pattern:** Information Expert

**Evidence**

```java
for (SaleDetail detail : sale.getDetails()) {
    totalUnits += detail.getQuantity();
}
```

inside:

```java
BulkPurchaseDiscount.calculateDiscount()
```

**Explanation**

Sale owns its details and therefore should provide the total number of units sold.

**Solution**

Add:

```java
Sale.getTotalUnits()
```

**Origin**

Developer 1 Audit – V-07

**Responsible**

Samuel Angulo

**Boundary Finding:** Yes

---

## V-03 — Discount Ratio Calculated in Multiple Locations

**Pattern:** Information Expert

**Location**

Return refund calculations.

**Explanation**

Discount ratio logic is implemented repeatedly instead of being delegated to Sale.

**Solution**

Add:

```java
Sale.getDiscountRatio()
```

and reuse it from Return.

**Origin**

Developer 1 Audit – V-08

**Responsible**

Samuel Angulo

**Boundary Finding:** Yes

---

## V-04 — Stock Validation Duplicated Outside Product

**Pattern:** Information Expert

**Evidence**

```java
if(product.getStock() < requestedQuantity)
```

inside ProductValidator.

**Explanation**

Product already owns stock information.

**Solution**

Add:

```java
Product.hasStockFor(quantity)
```

**Origin**

Developer 1 Audit – V-11

**Responsible**

Samuel Angulo

---

## V-05 — Product Search in Sales Duplicated

**Pattern:** Information Expert

**Location**

- ReturnService
- ReturnRepository

**Solution**

Introduce:

```java
Sale.findProductById()
```

**Origin**

Developer 2 Findings

**Responsible**

Samuel David Rangel Martínez

---

## V-06 — Warranty Expiration Rule Outside Warranty

**Pattern:** Information Expert

**Explanation**

WarrantyService evaluates expiration dates directly.

**Solution**

Introduce:

```java
Warranty.expiresBetween()
```

**Origin**

Developer 2 Findings

**Responsible**

Samuel David Rangel Martínez

---

## V-07 — SaleDetail Created Directly by UI

**Pattern:** Creator

**Evidence**

```java
items.add(new SaleDetail(...))
```

inside:

```java
SubMenu.processSaleForm()
```

**Explanation**

Sale aggregates SaleDetail and should therefore create it.

**Consequence**

UI becomes coupled to SaleDetail construction logic.

**Solution**

Create:

```java
Sale.addItem(Product, int)
```

**Origin**

Leader Audit

**Responsible**

Samuel David Rangel Martínez

---

## V-08 — Menu Receives Services It Does Not Use

**Pattern:** Low Coupling

**Evidence**

Menu receives seven services only to forward them.

**Solution**

Instantiate specialized menus from Main.

**Origin**

Leader Audit LEAD-V-07

**Responsible**

Samuel David Rangel Martínez

---

## V-09 — Unused Promotions Parameter

**Pattern:** Low Coupling

**Evidence**

```java
loadAll(... promotions)
```

parameter unused.

**Solution**

Remove obsolete parameter.

**Origin**

Leader Audit LEAD-V-08

**Responsible**

Samuel David Rangel Martínez

---

## V-10 — WarrantyRepository Depends on Multiple Repositories

**Pattern:** Low Coupling

**Explanation**

WarrantyRepository orchestrates loading through several repository dependencies.

**Solution**

Pass loaded domain objects instead of repositories.

**Origin**

Developer 2 Findings

**Responsible**

Samuel David Rangel Martínez

---

## V-11 — Concrete Repository Dependencies in Services

**Pattern:** Indirection

**Evidence**

```java
private final ProductRepository repository;
private final SaleRepository repository;
```

and equivalent service dependencies.

**Explanation**

Business services depend directly on CSV implementations.

**Solution**

Introduce:

- ProductStorage
- SaleStorage
- PersonStorage

and equivalent abstractions.

**Origin**

Leader Audit LEAD-V-03  
Developer 1 Audit V-02

**Responsible**

Samuel Angulo and Samuel David Rangel Martínez

---

## V-12 — Monolithic User Interface

**Pattern:** High Cohesion

**Evidence**

`SubMenu.java` exceeds 600 lines and handles all application modules.

**Solution**

Split into specialized menus:

- ProductMenu
- SaleMenu
- PersonMenu
- ReturnMenu
- WarrantyMenu

**Origin**

Leader Audit LEAD-V-01

**Responsible**

Samuel David Rangel Martínez

---

## V-13 — Monolithic Sale Processing Logic

**Pattern:** High Cohesion

**Location**

```java
SaleService.processSale()
```

**Explanation**

The method performs validation, inventory management, promotions, warranties, persistence and updates.

**Solution**

Delegate responsibilities to specialized services.

**Origin**

Leader Audit LEAD-V-04

**Responsible**

Samuel David Rangel Martínez

---

## V-14 — Receipt Formatting Inside Sale

**Pattern:** High Cohesion / Pure Fabrication

**Solution**

Create:

```java
SaleReceiptFormatter
```

**Origin**

Leader Findings

**Responsible**

Samuel David Rangel Martínez

---

## V-15 — Receipt Formatting Inside Return

**Pattern:** High Cohesion / Pure Fabrication

**Solution**

Create:

```java
ReturnReceiptFormatter
```

**Origin**

Developer 1 Audit V-09

**Responsible**

Samuel Angulo

---

## V-16 — Certificate Formatting Inside Warranty

**Pattern:** High Cohesion / Pure Fabrication

**Solution**

Create:

```java
WarrantyCertificateFormatter
```

**Origin**

Developer 1 Audit V-10

**Responsible**

Samuel Angulo

---

## V-17 — Financial Reporting Inside ReturnService

**Pattern:** High Cohesion

**Solution**

Extract:

```java
MonthlyBalanceService
```

**Origin**

Developer 2 Findings

**Responsible**

Samuel David Rangel Martínez

---

## V-18 — Type Checks Using instanceof

**Pattern:** Polymorphism

**Evidence**

```java
if(product instanceof Console)
```

```java
if(product instanceof Accessory)
```

**Locations**

- SaleService
- SubMenu

**Solution**

Move behavior into Product hierarchy.

**Origin**

Leader Audit LEAD-V-02

**Responsible**

Samuel David Rangel Martínez / Samuel Angulo

---

## V-19 — Category Selection Using instanceof

**Pattern:** Polymorphism

**Location**

CategoryDiscount

**Solution**

Implement:

```java
ProductCategory
Product.getCategory()
```

**Origin**

Developer 1 Audit V-06

**Responsible**

Samuel Angulo

---

## V-20 — Accessory Type Matching Using instanceof

**Pattern:** Polymorphism

**Location**

AccessoryService

**Solution**

Implement:

```java
Accessory.getAccessoryKind()
```

**Origin**

Developer 2 Findings

**Responsible**

Samuel David Rangel Martínez

---

## V-21 — ProductRepository Type Conditionals

**Pattern:** Polymorphism

**Solution**

Introduce:

```java
ProductCsvMapper
ConsoleCsvMapper
VideoGameCsvMapper
```

**Origin**

Developer 1 Audit V-01

**Responsible**

Samuel Angulo

---

## V-22 — AccessoryRepository Type Conditionals

**Pattern:** Polymorphism

**Responsible**

Samuel David Rangel Martínez

---

## V-23 — PromotionRepository Type Conditionals

**Pattern:** Polymorphism

**Responsible**

Samuel David Rangel Martínez

---

## V-24 — WarrantyRepository Type Conditionals

**Pattern:** Polymorphism

**Responsible**

Samuel David Rangel Martínez

---

## V-25 — Duplicated Console Input Logic

**Pattern:** Pure Fabrication

**Evidence**

Duplicated `readInt()` and `readDouble()` methods.

**Solution**

Create:

```java
ConsoleInput
```

**Origin**

Leader Audit LEAD-V-06

**Responsible**

Samuel David Rangel Martínez

---

## V-26 — Duplicated CSV Infrastructure

**Pattern:** Pure Fabrication

**Explanation**

File reading and writing responsibilities are repeated across repositories.

**Solution**

Centralize reusable functionality within:

```java
CsvSupport
```

**Origin**

Consolidated Repository Findings

**Responsible**

Shared Responsibility

---

## V-27 — Product Catalog Exposure

**Pattern:** Protected Variations

**Evidence**

```java
return products;
```

inside ProductService.

**Solution**

Return:

```java
Collections.unmodifiableList(products);
```

**Origin**

Developer 1 Audit V-03

**Responsible**

Samuel Angulo

---

## V-28 — Console Compatibility Logic Duplicated

**Pattern:** Protected Variations

**Solution**

Create:

```java
CompatibleAccessory
```

**Origin**

Developer 1 Audit V-04

**Responsible**

Samuel Angulo

---

## V-29 — Percentage Validation Logic Duplicated

**Pattern:** Protected Variations

**Solution**

Create:

```java
PercentageBasedPromotion
```

**Origin**

Developer 1 Audit V-05

**Responsible**

Samuel Angulo

---

# 6. Boundary Findings

The following findings involve code owned by both team members and therefore require coordinated implementation.

| ID | Shared Module | Coordination |
|----|--------------|--------------|
| V-02 | Sale / Promotions | Leader adds Sale.getTotalUnits(), Developer 1 consumes it |
| V-03 | Sale / Return | Leader adds Sale.getDiscountRatio(), Developer 1 consumes it |
| V-05 | Sale / Return | Leader adds Sale.findProductById(), Developer 2 uses it |
| V-06 | Warranty / WarrantyService | Developer 1 adds method, Developer 2 uses it |
| V-11 | Services / Repositories | Both developers introduce persistence abstractions |
| V-18 | Product hierarchy / SaleService | Product abstractions supplied by Developer 1, consumed by Leader |

---

# 7. Out-of-Scope Findings

The following functional defects were discovered during the audit but are not GRASP violations and therefore are excluded from Requirement 7 correction scope:

- Failed-sale rollback leaves inconsistent memory state.
- PersistenceException hierarchy inconsistency.
- Silent input parsing failures.
- Missing exception handling for some runtime failures.
- Application startup fails when CSV files are corrupted.
- Sales persisted with incorrect total value.
- Sale receipts generated but never shown to users.
- Stock restoration may desynchronize memory and persistence.
- Returns always restore one unit regardless of quantity sold.
- Some services use generic exceptions instead of typed exceptions.
- Persistence folders are not automatically created in all scenarios.
- Refund calculations use the current product price instead of the sale price.
- Inconsistent validation behavior between entities and validators.

---

# Final Team Verdict

The GameZone Unicesar system demonstrates correct application of the **Controller** pattern but presents significant architectural debt in the areas of:

- Information Expert
- Creator
- Low Coupling
- High Cohesion
- Polymorphism
- Pure Fabrication
- Indirection
- Protected Variations

A total of **29 consolidated violations** were identified and approved by the Technical Leader. These violations constitute the official Phase 2 correction baseline and must be addressed according to the responsibilities and implementation order defined in this document.