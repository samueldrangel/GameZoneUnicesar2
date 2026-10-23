package com.gamezone.ui;

import com.gamezone.model.Customer;
import com.gamezone.model.Person;
import com.gamezone.model.Seller;
import com.gamezone.service.PersonService;

public class PersonMenu {
    private final PersonService personService;
    private final ConsoleInput input;

    public PersonMenu(PersonService personService, ConsoleInput input) {
        this.personService = personService;
        this.input = input;
    }
    
    public void show() {
    int option;
    do {
        System.out.println("\n--- GESTIÓN DE PERSONAS ---");
        System.out.println("1. Listar personas (Clientes y vendedores)");
        System.out.println("2. Agregar cliente");
        System.out.println("3. Agregar vendedor");
        System.out.println("0. Volver");
        option = input.readInt("Seleccione una opción: ");

        switch (option) {
            case 1: listPersons(); break;
            case 2: addCustomer(); break;
            case 3: addSeller(); break;
            case 0: break;
            default: System.out.println("Opción inválida.");
        }
    } while (option != 0);
}

    public void listPersons() {
        System.out.println("\n--- DIRECTORIO DE PERSONAS ---");
        for (Person p : personService.listAllPersons()) {
            System.out.println(p.getDetails());
        }
    }

    public void addCustomer() {
        String id = input.readLine("Ingrese ID: ");
        String name = input.readLine("Ingrese Nombre: ");
        String phone = input.readLine("Ingrese Teléfono: ");
        String email = input.readLine("Ingrese Correo Electrónico: ");

        Customer customer = new Customer(id, name, phone, email);
        personService.addPerson(customer);
        System.out.println("¡Cliente agregado exitosamente!");
    }

    public void addSeller() {
        String id = input.readLine("Ingrese ID: ");
        String name = input.readLine("Ingrese Nombre: ");
        String phone = input.readLine("Ingrese Teléfono: ");
        String code = input.readLine("Ingrese Código de Empleado: ");
        String shift = input.readLine("Ingrese Turno de Trabajo: ");

        Seller seller = new Seller(id, name, phone, code, shift);
        personService.addPerson(seller);
        System.out.println("¡Vendedor agregado exitosamente!");
    }
}
