classDiagram
    direction TB

    namespace Model {
        class Person {
            <<abstract>>
            #String id
            #String name
            #String phone
            +getDetails()* String
        }
        class Customer {
            -String email
        }
        class Seller {
            -String employeeCode
            -String shift
        }
        class Product {
            <<abstract>>
            #String id
            #String title
            #double price
            #int stock
            +getDescription()* String
        }
        class VideoGame {
            -String platform
            -String genre
            -String rating
        }
        class Console {
            -String brand
            -String model
            -String generation
        }
        class Accessory {
            <<abstract>>
            #List~String~ compatibleConsoles
            #String connectionType
        }
        class Controller {
            -String connectionType
        }
        class Cable {
            -double length
            -String connector
        }
        class Memory {
            -int capacity
            -String memoryType
        }
        class Promotion {
            <<abstract>>
            #String id
            #String name
            #LocalDate startDate
            #LocalDate endDate
            +calculateDiscount(Sale)* double
            +isCurrentlyActive() boolean
        }
        class PercentageDiscount
        class CategoryDiscount {
            -String category
        }
        class BulkPurchaseDiscount {
            -int minQuantity
        }
        class Warranty {
            <<abstract>>
            #String id
            #LocalDate startDate
            #LocalDate endDate
            +getDurationInMonths()* int
            +getAdditionalCost()* double
        }
        class BasicWarranty
        class ExtendedWarranty
        class Sale {
            -String saleId
            -LocalDate date
            -Customer customer
            -Seller seller
            -List~Product~ items
            -double extendedWarrantyCost
            +getSubtotal() double
            +getTotalAmount() double
            +canBeReturned() boolean
            +generateReceipt() String
        }
        class Return {
            -String returnId
            -LocalDate date
            -Sale sale
            -List~Product~ returnedProducts
            -String reason
            +calculateRefundAmount() double
            +generateReturnReceipt() String
        }
    }

    namespace Persistence {
        class ProductRepository
        class AccessoryRepository
        class PersonRepository
        class PromotionRepository
        class WarrantyRepository
        class SaleRepository
        class ReturnRepository
    }

    namespace Service {
        class ProductService
        class AccessoryService
        class PersonService
        class PromotionService {
            +findBestPromotionFor(Sale) Promotion
            +listActivePromotions() List~Promotion~
        }
        class WarrantyService {
            +cancelWarranties(String, String) double
            +findWarrantyByProduct(String, String) Warranty
        }
        class SaleService {
            +registerSale(...) boolean
            +listAllSales() List~Sale~
        }
        class ReturnService {
            +registerReturn(...) Return
            +calculateMonthlySales(int, int) double
            +calculateMonthlyReturns(int, int) double
            +generateMonthlyBalance(int, int) double
        }
    }

    namespace UI {
        class ConsoleUI {
            +start()
            -showReturnMenu()
            -showWarrantyMenu()
            -registerSale()
        }
    }

    Person <|-- Customer
    Person <|-- Seller
    Product <|-- VideoGame
    Product <|-- Console
    Product <|-- Accessory
    Accessory <|-- Controller
    Accessory <|-- Cable
    Accessory <|-- Memory
    Promotion <|-- PercentageDiscount
    Promotion <|-- CategoryDiscount
    Promotion <|-- BulkPurchaseDiscount
    Warranty <|-- BasicWarranty
    Warranty <|-- ExtendedWarranty

    Sale "1" --> "1" Customer
    Sale "1" --> "1" Seller
    Sale "1" --> "1..*" Product
    Warranty "*" --> "1" Product
    Warranty "*" --> "1" Sale
    Return "*" --> "1" Sale
    Return "1" --> "1..*" Product
    Promotion ..> Sale
    Accessory ..> Console : compatible ids

    ProductService --> ProductRepository
    AccessoryService --> AccessoryRepository
    PersonService --> PersonRepository
    PromotionService --> PromotionRepository
    WarrantyService --> WarrantyRepository
    WarrantyService --> SaleRepository
    SaleService --> SaleRepository
    ReturnService --> ReturnRepository
    SaleService --> ProductService
    SaleService --> AccessoryService
    SaleService --> PersonService
    SaleService --> PromotionService
    SaleService --> WarrantyService
    ReturnService --> SaleService
    ReturnService --> ProductService
    ReturnService --> AccessoryService
    ReturnService --> WarrantyService
    ConsoleUI --> ProductService
    ConsoleUI --> AccessoryService
    ConsoleUI --> PersonService
    ConsoleUI --> PromotionService
    ConsoleUI --> WarrantyService
    ConsoleUI --> SaleService
    ConsoleUI --> ReturnService