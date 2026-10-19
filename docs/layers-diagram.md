```mermaid
flowchart TD
    subgraph UI ["UI Layer"]
        ConsoleMenu
        Main
    end
    subgraph Service ["Service Layer"]
        ProductService
        PersonService
        SaleService
    end
    subgraph Persistence ["Persistence Layer"]
        ProductRepository
        PersonRepository
        SaleRepository
    end
    subgraph Model ["Model Layer"]
        Person
        Product
        Sale
    end
    UI --> Service
    Service --> Persistence
    Service --> Model
    Persistence --> Model