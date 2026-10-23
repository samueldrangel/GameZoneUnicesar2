# Informe de Auditoría de Código: Principios GRASP
**Auditor:** Sistema de Evaluación de Arquitectura de Software  
**Desarrollador Evaluado:** Samuel David Rangel Martínez (Developer 2 & Líder Técnico)  
**Proyecto:** GameZone - Sistema de Gestión e Inventario  
**Fecha:** 22 de Octubre, 2026  

---

## 1. Alcance de la Auditoría

La presente auditoría evalúa la arquitectura general, componentes de orquestación y módulos desarrollados por **Samuel David Rangel Martínez (Developer 2 & Líder Técnico)**:
* **Módulos de Negocio (Developer 2):** `PersonService`, `AccessoryService`, `PromotionService`, `ReturnService`, `WarrantyService`, repositorios y validadores asociados (`AccessoryValidator`, `PromotionValidator`, `WarrantyValidator`).
* **Módulos de Líder Técnico y Core:** `Sale`, `SaleDetail`, `SaleService`, `SaleRepository`, `SaleValidator`, paquete UI (`Main`, `Menu`, `SubMenu`).

---

## 2. Trazabilidad con el Historial de Commits

A continuación se detalla cómo los *commits* del repositorio respaldan las refactorizaciones y estructuración arquitectónica lideradas por Samuel Rangel:

| Hash Commit | Autor | Módulos / Archivos Afectados | Relevancia en Auditoría GRASP |
| :--- | :--- | :--- | :--- |
| `237669c7` | Samuel R. | `Menu`, `SaleService`, `exceptions` | Centralización del manejo de excepciones de flujo de negocio (**B-1**, **FD-2**). |
| `34b46212` | Samuel R. | `Menu.displayMenu`, `SubMenu` | Enrutamiento e interceptación centralizada de errores en la interfaz de consola (**V-04**, **V-05**). |
| `a062a1b8` | Samuel R. | `SaleService`, `SaleValidator` | Desacoplamiento de la lógica de validación de ventas hacia `SaleValidator` (**V-01**, **V-02**). |
| `a99e48e2` | Samuel R. | `PersonService`, `PersonValidator` | Delegación de reglas de validación de personas hacia `PersonValidator` (**V-10**, **V-11**). |

---

## 3. Matriz de Hallazgos GRASP y Propuestas de Solución

| ID | Patrón GRASP | Módulo / Clases | Diagnóstico del Problema | Solución Técnica (Acoplada al Repositorio) |
| :--- | :--- | :--- | :--- | :--- |
| **V-01 (Leader)** | **Polymorphism** | `SaleService`, `SubMenu` | Múltiples verificaciones con `instanceof` para ajustar inventario o asignar garantías según el tipo de producto. | Definir contratos polimórficos en `Product` para la actualización de stock y aplicación de reglas de venta. |
| **V-02 (Leader)** | **Information Expert** | `SaleService` | Búsqueda manual iterativa en catálogos de productos y accesorios desde el servicio de ventas. | Delegar la búsqueda a `ProductService.findById` y `AccessoryService.findById` y consumir solo el resultado. |
| **V-03 (Leader)** | **Creator** | `SubMenu` | La capa de presentación instancia directamente objetos `SaleDetail` calculando precios unitarios. | Aplicar el patrón Creator en `Sale`, proveyendo el método `addItem(Product, int)` para crear los detalles internamente. |
| **V-04 (Leader)** | **High Cohesion** | `SubMenu` | Concentración masiva de responsabilidades de flujo de UI en una sola clase (>600 líneas de código). | Descomponer `SubMenu` en controladores especializados por dominio (`ProductMenu`, `SaleMenu`, `ReturnMenu`, `PersonMenu`). |
| **V-05 (Leader)** | **Pure Fabrication** | `Menu`, `SubMenu` | Duplicación de lógica de captura y conversión de entradas por consola (`readInt`, `readDouble`). | Construir la clase utilitaria de infraestructura `ConsoleInput` dentro del paquete `com.gamezone.ui`. |
| **V-06 (Leader)** | **Low Coupling** | `Menu`, `SubMenu` | `Menu` recibe en su constructor 7 servicios que no utiliza directamente, solo los redirige a `SubMenu`. | Inyectar a cada menú especializado únicamente las dependencias de servicio estrictamente necesarias. |
| **V-07 (Leader/Dev 2)** | **Low Coupling** | `SaleRepository` | El método `loadAll` exige recibir la lista de promociones (`List<? extends Promotion>`) sin llegar a usarla. | Eliminar el parámetro de promociones no utilizado en `SaleRepository`, `SaleService` y `WarrantyRepository`. |
| **V-08 (Leader)** | **High Cohesion** | `Sale` | La entidad `Sale` asume la responsabilidad de formatear el recibo textual de la transacción (`generateReceipt`). | Trasladar la generación de texto a la clase de presentación `SaleReceiptFormatter` en el paquete `ui`. |
| **V-09 (Leader/Dev 2)** | **Indirection** | Servicios de Dominio | Conexión directa entre servicios de negocio (`SaleService`, `ReturnService`, etc.) e implementaciones CSV concretas. | Interponer interfaces de almacenamiento (`SaleStorage`, `ReturnStorage`, etc.) para desacoplar el motor de persistencia. |
| **V-10 (Dev 2)** | **Pure Fabrication** | Repositorios (`Person`, `Accessory`, etc.) | Duplicidad del código de lectura/escritura de archivos y manejo de buffers en múltiples repositorios. | Centralizar la gestión genérica de archivos CSV en un componente `CsvSupport`. |

---

## 4. Dictamen y Plan de Acción

1. **Descomposición de la Capa de Interfaz (UI):** Dividir `SubMenu` en controladores por módulo y extraer la captura de entrada a `ConsoleInput`.
2. **Inyección de Abstracciones de Persistencia:** Aplicar *Indirection* introduciendo interfaces `Storage` en todos los servicios de dominio.
3. **Refactorización del Flujo de Venta:** Transferir la creación de `SaleDetail` a `Sale` y mover el formateo de recibos fuera del modelo de dominio.
```