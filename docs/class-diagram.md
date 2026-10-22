```mermaid
classDiagram
    class Warranty {
        <<abstract>>
        -String id
        -Product product
        -Sale sale
        -LocalDate startDate
        -LocalDate endDate
        +getDurationInMonths() int
        +getWarrantyType() String
        +getAdditionalCost() double
        +isActive(LocalDate date) boolean
        +generateWarrantyCertificate() String
    }
    class BasicWarranty
    class ExtendedWarranty
    class WarrantyRepository {
        +saveAll(List~Warranty~ warranties) void
        +loadAll(List~Sale~ sales, List~Product~ products) List~Warranty~
    }
    class WarrantyService {
        -WarrantyRepository repository
        +loadStored(List~Sale~ sales, List~Product~ products) void
        +assignBasicWarranty(Product p, Sale s, LocalDate d) BasicWarranty
        +assignExtendedWarranty(Product p, Sale s, LocalDate d) ExtendedWarranty
        +findWarrantyByProduct(String productId, String saleId) Warranty
        +listAllWarranties() List~Warranty~
        +listActiveWarranties() List~Warranty~
        +listWarrantiesExpiringSoon(int daysAhead) List~Warranty~
    }
    class Sale {
        -double extendedWarrantyCost
        +getTotalAmount() double
    }
    class SaleService {
        -WarrantyService warrantyService
        +registerSale(Customer c, Seller s, List~Product~ items, List~String~ extendedIds) boolean
    }
    class Product {
        <<abstract>>
    }
    class Console
    class ConsoleMenu

    Warranty <|-- BasicWarranty
    Warranty <|-- ExtendedWarranty
    Warranty "0..*" --> "1" Product : covers
    Warranty "0..*" --> "1" Sale : issued in
    Product <|-- Console
    WarrantyRepository ..> Warranty
    WarrantyService --> WarrantyRepository
    SaleService --> WarrantyService
    SaleService ..> Console : instanceof
    ConsoleMenu --> WarrantyService
    ConsoleMenu --> SaleService