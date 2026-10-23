classDiagram
    class Product {
        <<abstract>>
        #String id
        #String title
        #double price
    }
    class VideoGame {
        -String platform
    }
    class Console {
        -int storageCapacity
    }
    class Accessory {
        <<abstract>>
    }

    Product <|-- VideoGame
    Product <|-- Console
    Product <|-- Accessory

    class SaleService {
        +registerSale()
    }
    
    SaleService ..> VideoGame : usa instanceof
    SaleService ..> Console : usa instanceof