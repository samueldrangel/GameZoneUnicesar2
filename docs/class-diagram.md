```mermaid
classDiagram
    direction TB

    class Person {
        <<abstract>>
    }
    class Customer {
        -String email
    }
    class Seller {
        -String employeeCode
    }
    class Product {
        <<abstract>>
        -String id
        -String title
        -double price
        -int stock
        +String getDescription()
    }
    class VideoGame {
        -String platform
    }
    class Console {
        -String brand
    }
    class Accessory {
        <<abstract>>
        -List<String> compatibleConsoleIds
        +String getDescription()
    }
    class Controller {
        -String connectionType
    }
    class Cable {
        -double lengthMeters
        -String connectorType
    }
    class Memory {
        -int capacityGb
        -String memoryType
    }
    class Sale {
        -String id
    }
    class ProductRepository {
        +saveAll()
    }
    class PersonRepository {
        +saveAll()
    }
    class SaleRepository {
        +saveAll()
    }
    class ProductService {
        +updateStock()
    }
    class PersonService {
        +findCustomerById()
    }
    class SaleService {
        +registerSale()
    }
    class ConsoleMenu {
        +showMenu()
    }

    Person <|-- Customer
    Person <|-- Seller
    Product <|-- VideoGame
    Product <|-- Console
    Product <|-- Accessory
    Accessory <|-- Controller
    Accessory <|-- Cable
    Accessory <|-- Memory

    Sale --> Customer : 1
    Sale --> Seller : 1
    Sale --> Product : 1..*
    ProductService --> ProductRepository
    PersonService --> PersonRepository
    SaleService --> SaleRepository
    ConsoleMenu --> ProductService
    ConsoleMenu --> PersonService
    ConsoleMenu --> SaleService