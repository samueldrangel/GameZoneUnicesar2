package com.gamezone.ui;


import java.util.List;

public class ConsolePrinter {
    public void printProducts(List<?> products) {
        System.out.println("--- LISTA DE PRODUCTOS ---");
        products.forEach(System.out::println);
    }

    public void printAccessories(List<?> accessories) {
        System.out.println("--- LISTA DE ACCESORIOS ---");
        accessories.forEach(System.out::println);
    }

    public void printSellers(List<?> sellers) {
        System.out.println("--- LISTA DE PERSONAS / VENDEDORES ---");
        sellers.forEach(System.out::println);
    }

    public void printSales(List<?> sales) {
        System.out.println("--- LISTA DE VENTAS ---");
        sales.forEach(System.out::println);
    }
}