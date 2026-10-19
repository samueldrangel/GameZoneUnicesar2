# Hierarchy Diagram - GameZoneUnicesar

```mermaid
classDiagram
    class Person {
        <<abstract>>
        -String id
        -String name
        -String phone
    }
    class Customer {
        -String email
    }
    class Seller {
        -String employeeCode
        -String workShift
    }
    Person <|-- Customer
    Person <|-- Seller

    class Product {
        <<abstract>>
        -String id
        -String title
        -double price
        -int stock
        +getDescription()*
    }
    class VideoGame {
        -String platform
        -String genre
        -String ageRating
    }
    class Console {
        -String brand
        -String model
        -String generation
    }
    Product <|-- VideoGame
    Product <|-- Console
    