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
