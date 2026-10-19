```mermaid
graph TD
    subgraph UI ["User Interface Layer (ui)"]
        ConsoleMenu
    end

    subgraph Service ["Service Layer (service)"]
        ProductService
        AccessoryService
        PersonService
        PromotionService
        SaleService
    end

    subgraph Persistence ["Persistence Layer (persistence)"]
        ProductRepository
        AccessoryRepository
        PersonRepository
        PromotionRepository
        SaleRepository
    end

    subgraph Model ["Model Layer (model)"]
        Person
        Customer
        Seller
        Product
        VideoGame
        Console
        Accessory
        Controller
        Cable
        Memory
        Promotion
        PercentageDiscount
        CategoryDiscount
        BulkPurchaseDiscount
        Sale
    end

    ConsoleMenu --> ProductService
    ConsoleMenu --> AccessoryService
    ConsoleMenu --> PersonService
    ConsoleMenu --> PromotionService
    ConsoleMenu --> SaleService

    ProductService --> ProductRepository
    AccessoryService --> AccessoryRepository
    PersonService --> PersonRepository
    PromotionService --> PromotionRepository
    SaleService --> SaleRepository

    SaleService --> ProductService
    SaleService --> AccessoryService
    SaleService --> PersonService
    SaleService --> PromotionService

    Service --> Model
    Persistence --> Model
```