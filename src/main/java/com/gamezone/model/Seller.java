package com.gamezone.model;

/**
 * Represents a store employee/seller.
 * 
 * @author Samuel David Rangel Martinez
 */
public class Seller extends Person {
    private String employeeCode;
    private String workShift;

    public Seller(String id, String name, String phone, String employeeCode, String workShift) {
        super(id, name, phone);
        this.employeeCode = employeeCode;
        this.workShift = workShift;
    }

    @Override
    public String getDetails() {
        return "Seller [ID: " + getId() + ", Name: " + getName() + ", Phone: " + getPhone() + 
               ", Employee Code: " + employeeCode + ", Work Shift: " + workShift + "]";
    }

    public String getEmployeeCode() { return employeeCode; }
    public String getWorkShift() { return workShift; }
}