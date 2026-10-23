# Acople de Auditorías GRASP al Historial de Commits

- **Equipo / Roles:**
  - **Samuel David Rangel Martínez (Samuel R):** Technical Leader & Developer 1 (Dev1)
  - **Samuel Antonio Acevedo Laurens (Samuel A):** Developer 2 (Dev2)
- **Rama de auditoría:** `feature/grasp-audit`
- **Estado base auditado:** `develop` (commit `22e6df84fc809dd25f7869e22ab2c58f40e15d78` / PR #37)
- **Fecha:** 22 de octubre de 2026

---

## 1. Mapeo de Desarrolladores, Módulos y Commits Clave

El historial de commits (Merge PR #31, PR #37 y commits previos en `develop`) mapea las responsabilidades y la autoría directa sobre el código auditado de la siguiente manera:

| Integrante | Rol | Módulos / Clases Bajo Autoría | Commits Relevantes en Historial |
|---|---|---|---|
| **Samuel David Rangel Martínez** | **Leader & Dev1** | • UI y Orquestación: `Menu`, `SubMenu`, `Main`<br>• Ventas: `Sale`, `SaleDetail`, `SaleService`, `SaleRepository`<br>• Productos: `Product`, `VideoGame`, `Console`, `ProductService`, `ProductRepository`<br>• Accesorios: Jerarquía `Accessory`, `Controller`, `Cable`, `Memory`, `ConsoleCompatible`<br>• Promociones: `Promotion`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount`<br>• Garantías y Devoluciones: `Warranty`, `BasicWarranty`, `ExtendedWarranty`, `Return`<br>• Manejo de Excepciones Base: `GameZoneException`, `BusinessRuleException`, `InvalidDataException`, `ResourceNotFoundException`<br>• Validaciones Base: `ProductValidator`, `PersonValidator` | • `71fe50c` docs: update README<br>• `34b4621` refactor: route menu operations through exception handler<br>• `a062a1b` refactor: delegate SaleService validations<br>• `5a4089d` refactor: delegate ProductService validations<br>• `a99e48e` refactor: delegate PersonService validations<br>• Commits `237669c`, `bb8770e`, `dd3e866` |
| **Samuel Antonio Acevedo Laurens** | **Dev2** | • Personas: `Person`, `Client`, `Seller`, `PersonRepository`, `PersonService`<br>• Accesorios (Servicio/Persistencia): `AccessoryService`, `AccessoryRepository`<br>• Promociones (Servicio/Persistencia): `PromotionService`, `PromotionRepository`<br>• Devoluciones (Servicio/Persistencia): `ReturnService`, `ReturnRepository`<br>• Garantías (Servicio/Persistencia): `WarrantyService`, `WarrantyRepository`<br>• Validadores Específicos: `AccessoryValidator`, `PromotionValidator`, `WarrantyValidator`, `SaleValidator` | Commits integrados en `feature/exception-handling` y PR #37 (`8d6ca41`, `8ccbfe8`, `36a4da1`, `cc3903d`) |

---

## 2. Auditoría GRASP — Samuel Antonio Acevedo Laurens (Dev2)

### 2.1 Cobertura de Código e Intersecciones
- **Clases del Scope:** `Person`, `Client`, `Seller`, `PersonRepository`, `PersonService`, `AccessoryRepository`, `AccessoryService`, `PromotionRepository`, `PromotionService`, `ReturnRepository`, `ReturnService`, `WarrantyRepository`, `WarrantyService`, `AccessoryValidator`, `PromotionValidator`, `WarrantyValidator`, `SaleValidator`.
- **Líneas escritas en código de Samuel R (Leader/Dev1):** Validaciones y manejo de excepciones en `SaleService` (líneas 3-5, 16-17, 65, 75, 81-82, 84-85, 89, 143-145, 169, 172-180) y `ProductService` (líneas 3-5, 7, 40, 53-60, 96, 119, 170-185, 194-199, 213-218).

### 2.2 Resumen de Hallazgos GRASP (Dev2)

| ID | Patrón | Clase / Método Auditado | Tipo de Hallazgo / Solución Propuesta |
|---|---|---|---|
| **V-01** | Information Expert | `ReturnService.findProductInSale` / `ReturnRepository.findProductInSale` | **Violación:** Búsqueda manual iterando detalles de venta.<br>**Solución:** Solicitar a Samuel R la adición de `Sale.findProductById(productId)`. |
| **V-02** | Information Expert | `WarrantyService.listWarrantiesExpiringSoon` | **Violación:** Cálculo de vencimiento fuera de la entidad.<br>**Solución:** Delegar regla a `Warranty.expiresBetween(from, to)` (Samuel R). |
| **V-03** | Low Coupling | `WarrantyRepository` (constructor / `loadAll`) | **Violación:** Acoplamiento directo a 5 repositorios externos.<br>**Solución:** Recibir ventas y productos cargados. Samuel R ajusta orden en `Main`. |
| **V-04** | Low Coupling | `WarrantyService()` y `PersonService()` | **Violación:** Constructores sin argumentos instancian repositorios concretos inútilmente.<br>**Solución:** Eliminar constructores no utilizados. |
| **V-05** | High Cohesion | `ReturnService.generateMonthlyBalance` | **Violación:** Mezcla de procesamiento de devoluciones con reportes financieros.<br>**Solución:** Extraer reporte a `MonthlyBalanceService`. Samuel R actualiza `SubMenu` y `Main`. |
| **V-06** | Polymorphism | `AccessoryService.matchesType` | **Violación:** Uso de `instanceof` para filtrar tipos de accesorios.<br>**Solución:** Agregar `getAccessoryKind()` en `Accessory` (Samuel R) y evaluar genéricamente. |
| **V-07** | Polymorphism | `AccessoryRepository.accessoryToCsvLine` / `csvLineToAccessory` | **Violación:** Mapeo CSV dependiente de condicionales de tipo.<br>**Solución:** Crear mappers CSV específicos por tipo en `persistence`. |
| **V-08** | Polymorphism | `PromotionRepository.promotionToCsvLine` / `csvLineToPromotion` | **Violación:** Mapeo condicional por tipo de promoción.<br>**Solución:** Mappers CSV dedicados por tipo de promoción. |
| **V-09** | Polymorphism | `WarrantyRepository.warrantyToCsvLine` / `csvLineToWarranty` | **Violación:** Condicional ternario asume solo dos tipos de garantía.<br>**Solución:** Mappers CSV dedicados por tipo de garantía. |
| **V-10** | Pure Fabrication | Escritura y lectura en los 5 repositorios del scope | **Violación:** Repetición de bloques I/O de archivos.<br>**Solución:** Delegar a `CsvSupport` la lectura y escritura general de líneas. |
| **V-11** | Indirection | Servicios acoplados a repositorios concretos | **Violación:** Ausencia de interfaces de persistencia.<br>**Solución:** Definir interfaces (`AccessoryStore`, `PersonStore`, etc.) e inyectarlas en servicios. |

---

## 3. Auditoría GRASP — Samuel David Rangel Martínez (Dev1 y Leader)

### 3.1 Cobertura de Código e Intersecciones
- **Clases del Scope Dev1:** `Product`, `VideoGame`, `Console`, `ProductRepository`, `ProductService`, `Accessory`, `Controller`, `Cable`, `Memory`, `ConsoleCompatible`, `Promotion`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount`, `Return`, `Sale.canBeReturned`, `Warranty`, `BasicWarranty`, `ExtendedWarranty`, `GameZoneException` (y sus 4 subtipos), `ProductValidator`, `PersonValidator`.
- **Clases del Scope Leader:** `Sale`, `SaleDetail`, `SaleRepository`, `SaleService`, `Menu`, `SubMenu`, `Main`.

### 3.2 Resumen de Hallazgos GRASP (Samuel R - Dev1)

| ID | Patrón | Clase / Método Auditado | Tipo de Hallazgo / Solución Propuesta |
|---|---|---|---|
| **V-01** | Polymorphism | `ProductRepository.productToCsvLine` / `csvLineToProduct` | **Violación:** `instanceof` y códigos de texto para persistir productos.<br>**Solución:** Introducir `ProductCsvMapper` y subclases por tipo de producto. |
| **V-02** | Indirection | `ProductService` | **Violación:** Acoplado a `ProductRepository` concreto y constructor vació que lo instancia.<br>**Solución:** Extraer interfaz `ProductStorage` y eliminar constructor sin parámetros. |
| **V-03** | Protected Variations | `ProductService.listAll` | **Violación:** Expone la lista mutable interna de productos.<br>**Solución:** Retornar `Collections.unmodifiableList(products)`. |
| **V-04** | Protected Variations | `Controller` y `Memory` | **Violación:** Duplicación de lógica de compatibilidad de consolas.<br>**Solución:** Crear clase abstracta `CompatibleAccessory` e integrar método por defecto de `ConsoleCompatible`. |
| **V-05** | Protected Variations | Promociones porcentuales | **Violación:** Atributo y validación de porcentaje repetido en 3 clases.<br>**Solución:** Crear clase abstracta `PercentageBasedPromotion`. |
| **V-06** | Polymorphism | `CategoryDiscount.matchesCategory` | **Violación:** Comparación condicional con `instanceof` para categorizar productos.<br>**Solución:** Crear enum `ProductCategory` y método abstracto `getCategory()` en `Product`. |
| **V-07** | Information Expert | `BulkPurchaseDiscount.calculateDiscount` | **Violación:** Recalcula total de unidades recorriendo los detalles de la venta.<br>**Solución:** Agregar `Sale.getTotalUnits()` y consumirlo en la promoción. |
| **V-08** | Information Expert | `Return.calculateRefundAmount` / `generateReturnReceipt` | **Violación:** Duplicación del cálculo del ratio de descuento y monto a reembolsar.<br>**Solución:** Agregar `Sale.getDiscountRatio()` y encapsular la lógica en `Return`. |
| **V-09** | High Cohesion | `Return.generateReturnReceipt` | **Violación:** Mezcla de lógica de dominio con formato de presentación gráfica.<br>**Solución:** Extraer a `ReturnReceiptFormatter` en la capa UI. |
| **V-10** | High Cohesion | `Warranty.generateWarrantyCertificate` | **Violación:** Formato de texto de certificado dentro de la entidad de dominio.<br>**Solución:** Extraer a `WarrantyCertificateFormatter` en la capa UI. |
| **V-11** | Information Expert | `ProductValidator.validateStockAvailability` | **Violación:** Revalida afuera la regla de disponibilidad que pertenece a `Product`.<br>**Solución:** Exponer `Product.hasStockFor(quantity)` y reutilizarlo en el validador. |

### 3.3 Resumen de Hallazgos GRASP (Samuel R - Technical Leader)

| ID | Patrón | Clase / Método Auditado | Tipo de Hallazgo / Solución Propuesta |
|---|---|---|---|
| **V-01** | Polymorphism | `SaleService.processSale` / `restoreStock` y `SubMenu` | **Violación:** Evaluaciones `instanceof` para procesar stock y garantías según el tipo de producto.<br>**Solución:** Delegar capacidad de garantía y actualización a la jerarquía `Product`. |
| **V-02** | Information Expert | `SaleService.findProductOrAccessoryById` | **Violación:** Extrae listas completas de productos y accesorios para buscar por ID.<br>**Solución:** Delegar búsqueda directa a los servicios respectivos. |
| **V-03** | Creator | `SubMenu.processSaleForm` | **Violación:** La vista instancia directamente los objetos `SaleDetail`.<br>**Solución:** Encapsular la creación de detalles dentro de `Sale.addItem(...)`. |
| **V-04** | High Cohesion | `SubMenu` | **Violación:** Clase monolítica (627+ líneas) gestionando los flujos de 7 módulos.<br>**Solución:** Modularizar en sub-menús especializados por módulo (`ProductSubMenu`, `SaleSubMenu`, etc.). |
| **V-05** | Pure Fabrication | `Menu.readInt` / `SubMenu.readInt` / `readDouble` | **Violación:** Lógica de lectura de consola duplicada.<br>**Solución:** Encapsular lectura de consola en un componente helper `ConsoleInput`. |
| **V-06** | Low Coupling | `Menu` y `SubMenu` | **Violación:** Inyección masiva de 7 servicios que no todos los componentes utilizan.<br>**Solución:** Inyectar en cada sub-menú únicamente los servicios requeridos. |
| **V-07** | Low Coupling | `SaleRepository.loadAll` | **Violación:** Parámetro `promotions` no utilizado en la firma del método.<br>**Solución:** Remover el parámetro de la firma y actualizar invocadores. |
| **V-08** | High Cohesion | `Sale.generateReceipt` | **Violación:** Formateador de recibo embedido en la entidad de dominio.<br>**Solución:** Extraer formateador de recibos a la capa UI. |
| **V-09** | Indirection | `SaleService` | **Violación:** Acoplamiento directo con la implementación concreta de `SaleRepository`.<br>**Solución:** Definir e implementar la interfaz `SaleStorage`. |

---

## 4. Matriz de Invocación y Puntos Frontera

Los hallazgos que requieren trabajo coordinado entre Samuel A (Dev2) y Samuel R (Dev1 / Leader) se resumen a continuación para garantizar la compilación continua durante las correcciones:

```
[Samuel A / Dev2]                               [Samuel R / Dev1 & Leader]
----------------                                --------------------------
V-01 (ReturnService)        ---> requiere --->  Sale.findProductById()
V-02 (WarrantyService)      ---> requiere --->  Warranty.expiresBetween()
V-03 (WarrantyRepository)   ---> requiere --->  Ajuste de orden de instanciación en Main
V-05 (ReturnService)        ---> requiere --->  Invocación de MonthlyBalanceService desde SubMenu
V-06 (AccessoryService)     ---> requiere --->  Accessory.getAccessoryKind()
V-07 (SaleRepository call)  <--- requiere <---  Remoción de parámetro en SaleRepository.loadAll (V-07 Leader)
V-09 (SubMenu receipt)      <--- requiere <---  Creación de ReturnReceiptFormatter / WarrantyCertificateFormatter
```

---

## 5. Defectos Funcionales e Incidencias Fuera de Alcance (Out of Scope)

Los siguientes puntos identificados durante el proceso de auditoría corresponden a comportamiento funcional o excepciones no capturadas. Se registran para corrección posterior sin alterar la evaluación del cumplimiento GRASP:

1. **FD-1 (Persistencia de Venta):** `SaleService.processSale` almacena la venta en memoria antes de persistir a disco; en caso de fallo en I/O, el estado en memoria no deshace completamente las asignaciones.
2. **FD-2 (Manejo Global de Excepciones en UI):** Excepciones tipo `RuntimeException` envueltas durante persistencia o `DateTimeParseException` en lectura de fechas pueden saltar el bloque de captura principal del `Menu`.
3. **FD-3 (Recibos no mostrados):** El método `Sale.generateReceipt` contiene lógica de desglose de promociones no invocada desde la vista `processSaleForm`.
4. **FD-4 (Creación de Directorios):** `ProductRepository` y `PersonRepository` asumen la existencia del directorio `data/`; si no existe, la primera ejecución falla previo a la creación del archivo.