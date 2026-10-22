GameZoneUnicesar - Analysis Document About the People in the System

What attributes are common to all people who interact with the store, and which ones are specific to each type of person? How is this distinction reflected in a class hierarchy?
Common Attributes: All individuals interacting with the system share core identifying and contact properties, such as id , name, email, and phone.
Specific Attributes: Customer : customerType, acummulatedPoints. Employee : employeeId, salary, role, hireDate
*Reflection in Class Hierarchy: This distinction is implemented using inheritance ("is-a" relationship). An abstract base class Person is created to encapsulate all shared attributes. Specialized subclass entities (Customer and Seller) extend Person,inheriting all common fields and behaviors while explicitly adding their own distinct properties and methods

Should a class representing a "generic person" without specifying a role exist? Why or why not? What implication does this decision have on the possibility of instantiating this class?
*Yes, a generic class representing a person (e.g., Person) should exist, but it must be declared as an abstract class (abstract class Person). *Why yes?: It promotes code reusability by encapsulating common attributes and behaviors (such as id, name, email, and phone) shared by all individuals interacting with the system. Furthermore, it enables polymorphism, allowing the system to process customers and sellers uniformly when only basic personal information is required. *Implication on Instantiation: Declaring Person as an abstract class strictly prevents direct instantiation. Calling new Person(...) will result in a compilation error. The system will only permit the instantiation of concrete subclasses (Customer or Seller), thereby preserving domain integrity.

About the system products 3. What characteristics are shared by all products sold by the store, regardless of their type? What characteristics are specific to each product type? *Common Characteristics (Base Class Product): All inventory items share fundamental attributes required for catalog and sales management:

code / id: Unique product identification code or SKU. name: Commercial name or title of the product. originalPrice: Base selling price. stock: Current quantity available in stock. description: General description of the product.

*Specific Characteristics by Product Type (Subclasses): -VideoGames: platform genre ageRating -Console: Brand Model Generation -Accesories: accessoryType compatibility

Each product type must present a description integrating its specific characteristics. How should this behavior be declared in the base class to guarantee that all subclasses implement it in their own way? What OOP mechanism allows this? *Declaration in the Base Class (Product): This behavior should be declared in the base class as an abstract method with no implementation body (e.g., public abstract String getDetails(); or public abstract String getDescription();).
*Enforcing Implementation in Subclasses: By declaring the method as abstract inside an abstract base class, object-oriented languages (such as Java) enforce a compile-time contract. This guarantees that every concrete subclass (VideoGame, Console, Accessory) must provide its own @Override implementation to format its distinct attributes. If a subclass fails to implement the abstract method, the project will fail to compile.

*OOP Mechanism: This feature is enabled through Polymorphism (specifically dynamic method dispatch / method overriding) and Abstraction.

About sales and relationships between entities 5. A sale involves a customer, a seller, and one or more products. What types of relationships exist between the sale class and other system classes?
Are these inheritance, association, composition, or another type? Justify.

*Between Sale and Customer / Seller:Relationship Type: Association. Justification: A sale connects the customer making the purchase and the seller processing the transaction. This is a simple association because all three entities have independent lifecycles. Removing a sale record does not delete the customer or seller entities from the system.

*Between Sale and its detail items (SaleDetail / SaleItem):Relationship Type: Composition. Justification: A sale consists of one or more purchased product line items managed through detail objects. This represents a strong whole-part relationship with lifecycle dependency: line items belong exclusively to that specific sale. If a sale object is destroyed, its associated detail items are destroyed as well.

*Between SaleDetail and Product:Relationship Type: Association. Justification: Each line item references a product from the catalog to capture its price and details. The product entity exists independently in the store's inventory regardless of whether a sale transaction takes place.
Are there Inheritance relationships?No. There is no inheritance (generalization) involved here, as a Sale is not an extension or subtype of a Customer, Seller, or Product.

Should the sale be responsible for calculating its own total, or should this responsibility belong to another class? Argument your decision. The Sale class owns the collection of line items (SaleDetail / SaleItem), which contain the sold quantity and the unit price of each product. Therefore, Sale is the expert entity containing all the detailed data required to iterate over its items, calculate subtotals, and compute the aggregate total price.
Offloading this calculation to an external class (such as the User Interface or Persistence layer) would violate encapsulation and introduce tight coupling.

How is it guaranteed in the design that a sale cannot be registered without at least one product? At what point in the system should this rule be validated?

*Design Guarantee (Domain / Model Layer):It is enforced at the domain level by designing the Sale class constructor or its item addition methods to strictly prevent an empty state. The Sale object should require a non-empty collection of detail items upon creation.
Furthermore, the internal validation in the Sale class ensures that its collection of items is not null and has a size greater than zero (items.size() > 0). If the collection is empty, the entity throws a domain/business exception (e.g., IllegalArgumentException or EmptySaleException).

*Validation Point in the System:Service Layer (SaleService): This is the primary authority for enforcing business rules. Before persisting the transaction, SaleService validates that the sale contains at least one product. If the rule is violated, it halts execution and throws an exception before calling the persistence layer. User Interface Layer (UI): Acts as an early input validation step. The console menu interface prevents sending an invalid or empty sale request to the service layer when prompting the user to select items.

How is the automatic inventory update reflected in the design when a sale is registered? Which classes are involved in this operation?
The inventory update is designed as an orchestrated flow within the service layer. During the sale registration workflow, the service iterates through each item in the sale, invokes stock-reduction methods on the corresponding Product domain entities, and then requests the ProductRepository to persist the updated stock levels into the file storage.

clases involved: -SaleService -Product -ProductRepository / ProductPersistence -Sale and SaleDetail

The system must be organized into four layers: model, persistence, services, and user interface. What type of classes belong to each layer? What criterion allows deciding where a class should be placed?
-classes per layers and responsabilities:

-Model Layer (model / domain): Business entity classes (example: Person, Customer, Seller, Product, VideoGame, Console, Accessory, Sale, SaleDetail). -Persistence Layer (persistence / repository): Data access classes and repositories (example: ProductRepository, SaleRepository, PersonRepository). -Service Layer (service): Workflow and application logic orchestrators (example: SaleService, InventoryService, PersonService). -User Interface Layer (ui / view): Console menus, screens, or GUI views (example: MainConsoleUI, SaleMenuUI).

Placement Criterion (Single Responsibility Principle - SRP): The main criterion is driven by the class's single primary responsibility and abstraction level: -Does it represent a core domain concept or internal entity rule? Model. -Does it manage raw file reading, writing, or disk persistence? Persistence. -Does it orchestrate a business process, connect model with storage, or validate transactions? Services. -Does it render text/menus or process user keyboard inputs? User Interface.

Why should file storage and retrieval logic not be inside domain classes? What problems occur when these responsibilities are mixed?
La lógica de almacenamiento no debe incluirse en las clases del dominio para respetar el Principio de Responsabilidad Única (SRP) y mantener las entidades del negocio completamente desacopladas de la infraestructura técnica. Mezclar estas responsabilidades genera un alto acoplamiento que dificulta cambiar el formato de persistencia en el futuro, complica las pruebas unitarias al obligar la creación de archivos reales en disco, reduce la reusabilidad del modelo en otros entornos y provoca código duplicado al tener que reimplementar la gestión de archivos y el manejo de excepciones de I/O en cada entidad.

What dependencies are allowed between layers and which ones are forbidden? Justify the rationale behind allowed dependencies.
Allowed dependencies flow strictly in a unidirectional, top-to-bottom direction (UI - Service - Persistence - Model), where the UI invokes services to execute use cases, services call persistence to manage data, and all outer layers reference the model to operate on domain entities; conversely, circular dependencies, upward calls (Persistence - Service / UI), and any outward dependency from the Model are strictly forbidden. This structure is justified by the Clean Architecture Dependency Rule, ensuring that core business logic remains completely independent, highly reusable, and shielded from technical changes in the user interface or file storage implementations.

# Accessory Module Analysis - GameZoneUnicesar
 
1. **Hierarchy Integration**: Accessories should extend the abstract class `Product` to maximize code reuse (id, title, price, stock) and maintain consistency across the catalog.
2. **Accessory Attributes**: Common attributes are handled by `Product` and `Accessory`. Specific attributes are: connection type and compatible consoles for controllers; length and connector type for cables; storage capacity and memory type for memories.
3. **Compatibility Representation**: Compatibility is a many-to-many relationship between accessories (specifically controllers and some memories) and consoles, represented as a list of compatible console IDs (`List<String> compatibleConsoleIds`) inside the `Accessory` class.
4. **SaleService Modifications**: `SaleService.registerSale` must accept a unified list of sellable items (`Product`), validating stock and delegating inventory updates to either `ProductService` or `AccessoryService` dynamically based on the item type.
5. **Layer Placement**: New classes are distributed across the four layers: `model` (`Accessory`, `Controller`, `Cable`, `Memory`), `persistence` (`AccessoryRepository`), `service` (`AccessoryService`), and integrated into `ui` and `service` (`SaleService`, `ConsoleUI`).


# Promotion Module Analysis

## 1. Class hierarchy and polymorphism

The three promotions share data (id, name, start date, end date) and behavior
(`isActive`), but they calculate the discount differently. This is modeled with
an abstract base class `Promotion` and three concrete subclasses:
`PercentageDiscount`, `CategoryDiscount` and `BulkPurchaseDiscount`. The common
attributes and `isActive` live once in the base class, and each subclass adds
only its own attributes and its own calculation.

The mechanism that lets each type calculate differently is polymorphism through
method overriding (dynamic binding). The rest of the system works with
`Promotion` and calls `calculateDiscount(sale)`; the JVM runs the version of the
real object at runtime. `PromotionService` and `SaleService` never check the
concrete type, so adding a fourth promotion type does not require changing them.

## 2. The abstract method

In `Promotion` the method is declared without a body:

```java
public abstract double calculateDiscount(Sale sale);
```

An abstract method can only exist inside an abstract class. The declaration
guarantees that every concrete subclass must implement it, otherwise the code
does not compile. It also fixes a single contract (input `Sale`, output the
discount amount) that every client can rely on. Subclasses mark their
implementation with `@Override`, so the compiler also detects signature
mistakes.

## 3. Where is the "best promotion" selection?

The selection lives in `PromotionService.findBestPromotionFor(Sale sale)`, in
the service layer. The service already owns the list of promotions and applies
business rules, so it is the right place to compare them and choose the one with
the highest monetary discount.

It is consistent with the layered architecture: the service sits between the ui
and persistence/model and holds rules that involve several entities.

It must not be in `Sale` because the model entity should not know the catalog of
promotions. If it did, `Sale` would depend on `Promotion` and would have two
reasons to change (sale data and promotion policy), which hurts cohesion. It
must not be in the console menu because the ui layer only collects input and
prints output. A rule placed there would be duplicated by any other interface
and could not be reused or tested without the console.

## 4. Changes in `Sale` and the receipt

- `Sale` gets two private attributes, `appliedPromotionName` (String) and
  `discountAmount` (double), with getters and setters.
- `getSubtotal()` returns the sum of item prices. `getTotalAmount()` now returns
  `subtotal - discountAmount`.
- `generateReceipt()` prints the subtotal, the discount with the promotion name
  and the final total.
- `SaleRepository` stores the promotion name and the discount as two extra
  columns.
- `SaleService.registerSale` asks `PromotionService` for the best promotion and
  sets the two attributes.

These changes do not break existing behavior. `discountAmount` defaults to 0, so
a sale without promotion has the same total as before. Old lines in
`sales.csv` have fewer columns, and the loader treats the missing columns as
"no promotion". `Sale` does not depend on `Promotion`; it only stores the name.

## 5. Where is the validity check?

In both classes, with different responsibilities.

- `Promotion.isActive(LocalDate date)` knows its own start and end dates, so it
  is the expert on whether a date falls inside its range (Information Expert).
- `PromotionService` decides which date to use (the current date or the sale
  date) and filters the whole list by calling `isActive` on each promotion. This
  is the logic of `listActivePromotions` and of `findBestPromotionFor`.

Putting the range check only in the service would force it to read the dates of
each promotion; putting the filtering only in the model would make `Promotion`
aware of the whole catalog.

# Return Module Analysis

## 1. Relationship between Return and Sale
It is an association. A `Return` holds a reference to the `Sale` it refers to
(multiplicity 1), and a sale can have zero or more returns. It is not inheritance,
because a return is not a kind of sale. It is not aggregation or composition,
because the sale exists independently and is not owned by the return: the
return only points to it, and `Sale` does not know its returns.

## 2. Partial returns
`Return` stores a `List<Product>` with only the products being returned, taken
from the items of the original sale. Each occurrence in the list represents one
returned unit. The refund is calculated from that list, not from the whole sale.
The service also checks that the units requested do not exceed the units sold
minus the units already returned in previous returns of the same sale.

## 3. The 30-day rule
It is a business rule, so it is validated in the service layer
(`ReturnService.registerReturn`). The date comparison is exposed by the model
helper `Sale.canBeReturned()`, which uses `java.time` (`LocalDate.plusDays` or
`ChronoUnit.DAYS.between`). The service decides to reject the operation, and the
ui only shows the message.

## 4. Stock restoration
`ProductService.restoreStock(productId, quantity)` is added and reuses the existing
`updateStock` method with a positive delta, so the rule "stock cannot be negative"
and the file saving stay in one place. `ReturnService` calls it for every
returned product. Reusing existing logic avoids two implementations that can
diverge and keeps one point of change.

## 5. Monthly balance
`ReturnService.generateMonthlyBalance(month, year)` consolidates sales and
returns, so it belongs to the service layer: it coordinates two modules without
adding that knowledge to the model or the ui. It needs `SaleService` (to read the
sales of the month) and its own list of returns loaded through `ReturnRepository`.

# Warranty Module Analysis

## 1. Hierarchy and mechanism
Both warranties share product, sale, start and end dates, but differ in duration,
type name and cost. The shared part lives in the abstract class `Warranty`; the
differences are abstract methods (`getDurationInMonths`, `getWarrantyType`,
`getAdditionalCost`) implemented by `BasicWarranty` and `ExtendedWarranty`.
Polymorphism through overriding lets each type provide its own duration without
duplicating the end-date logic, which is written once in the base constructor.

## 2. Automatic basic warranty for consoles only
The decision is a business rule, so it is placed in the service layer
(`SaleService.registerSale`). The real type of a product is checked with
`instanceof Console`. Video games do not match, so they get no warranty.
A possible improvement is a polymorphic method in `Product`
(for example `hasDefaultWarranty()`), which would remove the type check.

## 3. End date calculation
`Warranty` computes `endDate = startDate.plusMonths(getDurationInMonths())` in its
constructor. Subclasses only return their duration (6 or 12). Doing it in the
constructor guarantees that an object is never created with an inconsistent end
date, and the attributes can be final. Calling an overridable method from a
constructor is risky when it depends on subclass fields; here the
implementations return constants, so it is safe.

## 4. Extended warranty cost
The cost is calculated in `SaleService.registerSale`, after validating stock and
creating the sale, when each extended warranty is assigned:
`ExtendedWarranty.getAdditionalCost()` returns 10% of the product price and the
service adds it to `Sale.extendedWarrantyCost`. `registerSale` receives an extra
parameter, `List<String> productIdsWithExtendedWarranty`. The old four-argument
signature stays as an overload that passes an empty list, so existing calls keep
working. The sale total is `subtotal - discount + extendedWarrantyCost`.

## 5. Expiring-soon query
`WarrantyService.listWarrantiesExpiringSoon(int daysAhead)` iterates over the
stored warranties and keeps the active ones whose end date is not after
`today + daysAhead`. It only needs its own warranty list and `WarrantyRepository`
for persistence. It belongs to the service layer because it is a business query
over a collection of entities; the ui only calls it and prints the result, and
the model does not know the catalog.

# Integration Analysis

| ID | Problem | Cause | Solution | Verification |
|---|---|---|---|---|
| A1 | Promotions could not target accessories | `CategoryDiscount` only knew VIDEOGAME and CONSOLE | Added the ACCESSORY category (matches `Accessory` instances), validated the three categories in `PromotionService`, added a menu option and a preloaded promotion | A sale with an accessory receives the accessory promotion |
| A2 | `SaleService -> WarrantyService -> WarrantyRepository -> SaleService` cycle prevented constructor injection | The repository resolved sales during loading | The repository persists only ids; `WarrantyService` resolves references through `SaleRepository` and the product, accessory and person services; `Main` builds objects in a new order. Deviation: two extra constructor parameters, needed to rebuild sales that contain accessories | The application starts and warranties reload after a restart |
| A3 | The order of operations in `registerSale` changed the result | Requirements 1, 2 and 4 modified the method independently | One eight-step flow; the discount is computed over the item subtotal only; total = subtotal - discount + extended warranties | Receipt totals match a manual calculation |
| A4 | Returning an accessory did not restore its stock | `ReturnService` only called `ProductService.restoreStock` | `AccessoryService.restoreStock` and delegation by item type | The stock of a returned accessory increases |
| A5 | The refund exceeded what the customer paid | The refund used list prices | Refund per item = price x (1 - discount / subtotal) | The refund of a discounted sale equals the paid proportion |
| A6 | The monthly report only showed the net value | `generateMonthlyBalance` returned a single number | `calculateMonthlySales` and `calculateMonthlyReturns` added; sales use the final sale total | The menu shows three values |
| A7 | A returned console kept its warranty | No requirement defined it | `WarrantyService.cancelWarranties`; the extended cost is refunded and included in the return | The warranty disappears after returning the console |

## Known limitation (out of scope)
Returning one of two identical consoles in the same sale cancels the warranties of both.