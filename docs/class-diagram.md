# Class Diagram - GameZoneUnicesar

```mermaid
classDiagram
    direction TB
    class Person { <<abstract>> }
    class Customer { -String email }
    class Seller { -String employeeCode }
    class Product { <<abstract>> }
    class VideoGame { -String platform }
    class Console { -String brand }
    class Sale { -String id }
    class ProductRepository { +saveAll() }
    class PersonRepository { +saveAll() }
    class SaleRepository { +saveAll() }
    class ProductService { +updateStock() }
    class PersonService { +findCustomerById() }
    class SaleService { +registerSale() }
    class ConsoleMenu { +showMenu() }

    Sale --> Customer : 1
    Sale --> Seller : 1
    Sale --> Product : 1..*
    ProductService --> ProductRepository
    PersonService --> PersonRepository
    SaleService --> SaleRepository
    ConsoleMenu --> ProductService
    ConsoleMenu --> PersonService
    ConsoleMenu --> SaleService