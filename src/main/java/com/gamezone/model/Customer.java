package com.gamezone.model;

/**
 * Represents a store customer.
 * 
 * @author Samuel David Rangel Martinez
 */
public class Customer extends Person {
    private String email;

    public Customer(String id, String name, String phone, String email) {
        super(id, name, phone);
        this.email = email;
    }

    @Override
    public String getDetails() {
        return "Customer [ID: " + getId() + ", Name: " + getName() + ", Phone: " + getPhone() + ", Email: " + email + "]";
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
