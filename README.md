# GameZoneUnicesar

## Description
**GameZoneUnicesar** is a Java-based console application developed as an integrated project for academic purposes at Universidad Popular del Cesar. It implements a robust layered architecture (Model, Persistence, Service, and UI) to manage a video game and console inventory system with file-based persistence.

## Architecture & Package Structure
The project follows a standard Maven layered structure (`com.gamezone`):
- **`com.gamezone.model`**: Domain classes containing the abstract base class `Product` and its subclasses `VideoGame` and `Console` (applying inheritance and polymorphism).
- **`com.gamezone.persistence`**: Handles file-based reading and writing of inventory data (`ProductRepository`).
- **`com.gamezone.service`**: Manages business logic rules, data lists, and stock validations (`ProductService`).
- **`com.gamezone.ui`**: Command-line interactive menu interface (`ConsoleUI`).
- **`com.gamezone`**: Application entry point (`Main`).

## Prerequisites
- Java Development Kit (JDK) 8 or higher.
- Apache Maven.
- Apache NetBeans IDE or any Java-compatible IDE.

## How to Run
1. Clone the repository and checkout the target branch.
2. Open the project in Apache NetBeans as a Maven project.
3. Locate the `Main.java` file inside `com.gamezone`.
4. Run the file (Shift + F6 in NetBeans) to launch the interactive console system.