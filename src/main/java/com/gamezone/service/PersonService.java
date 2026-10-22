package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.*;
import java.util.List;
import validation.PersonValidator;

/**
 * Service for managing persons.
 * 
 * @author Samuel David Rangel Martinez
 */
public class PersonService {
    private PersonRepository repository;
    private List<Person> persons;

    public PersonService() {
        this.repository = new PersonRepository();
        this.persons = repository.loadAll();
    }

    public List<Person> listAllPersons() { return persons; }

    public void addPerson(Person person) {
        persons.add(person);
        repository.saveAll(persons);
    }

    public boolean registerCustomer(String id, String name, String phone, String email) {
        PersonValidator.validatePersonData(id, name, email, phone);
        PersonValidator.validateUniquePerson(persons, id);
        persons.add(new Customer(id, name, phone, email));
        repository.saveAll(persons);
        return true;
    }

    public Person findById(String id) {
        for (Person p : persons) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    public Customer findCustomerById(String id) {
        Person p = findById(id);
        if (p instanceof Customer) return (Customer) p;
        return null;
    }

    public Seller findSellerById(String id) {
        Person p = findById(id);
        if (p instanceof Seller) return (Seller) p;
        return null;
    }
}