# Developer 1 - AI Usage Log

## Project: GameZoneUnicesar
**Developer:** Desarrollador 1  
**Module:** Product Model, Persistence, Services, and Console UI  

---

## Summary of AI Assistance
During the development of the product module and console user interface, AI was utilized as a collaborative technical assistant for the following tasks:
1. **Architecture Guidance:** Structuring the layered architecture (`model`, `persistence`, `service`, `ui`).
2. **Boilerplate and Syntax Generation:** Writing initial implementations for inheritance structures (`Product`, `VideoGame`, `Console`), file serialization in `ProductRepository`, and business logic rules in `ProductService`.
3. **Documentation & Git Workflows:** Formatting JavaDoc comments in English and generating descriptive commit messages following Conventional Commits guidelines.

---

## Detailed AI Interactions Log

### Interaction 1: Product Hierarchy & Inheritance
- **Objective:** Design the abstract base class and its subclasses adhering to object-oriented principles.
- **AI Support:** Provided structural code for `Product` (abstract), `VideoGame`, and `Console` with proper encapsulation and polymorphism.
- **Developer Action:** Reviewed, integrated into NetBeans, and verified compilation and behavior.

### Interaction 2: File Persistence & Service Logic
- **Objective:** Implement flat-file storage and inventory business rules.
- **AI Support:** Generated logic for `ProductRepository` using `BufferedReader`/`PrintWriter` with semicolon delimiters, and `ProductService` for stock validations.
- **Developer Action:** Tested data persistence locally and validated error handling.

### Interaction 3: Console UI & Application Entry Point
- **Objective:** Create an interactive command-line interface for the system.
- **AI Support:** Designed `ConsoleUI` menu loops and the `Main` execution class.
- **Developer Action:** Tested menu navigation options and ensured seamless integration with the service layer.
