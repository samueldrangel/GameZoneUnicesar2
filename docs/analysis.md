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