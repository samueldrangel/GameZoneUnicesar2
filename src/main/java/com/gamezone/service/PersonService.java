package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.*;
import java.util.List;

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